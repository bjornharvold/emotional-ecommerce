/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.jobs.etl;

import com.lela.commons.LelaException;
import com.lela.commons.jobs.AbstractCountingExecutionContext;
import com.lela.commons.jobs.ExecutionContext;
import com.lela.commons.jobs.AbstractExecutionContext;
import com.lela.commons.service.IngestJobService;
import com.lela.domain.document.User;
import com.lela.domain.dto.Principal;
import com.lela.commons.service.UserService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.ingest.json.JsonTemplateEngine;
import org.codehaus.jackson.map.ObjectMapper;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by IntelliJ IDEA.
 * User: Chris Tallent
 * Date: 12/1/11
 * Time: 10:49 AM
 */
public class EtlExecutionContext extends AbstractCountingExecutionContext<EtlJobDetail> implements ExecutionContext {

    private static final Logger log = LoggerFactory.getLogger(EtlExecutionContext.class);

    private final JdbcTemplate jdbcTemplate;
    private final UserService userService;
    private final String ingestUser;
    private final String ingestPassword;

    private final List<String> preExecutionQueries;

    private ExecutorService executor;
    
    private ConvertAndSaveUtil ingestUtil;

    public EtlExecutionContext(EtlJobDetail ingestJob,
                               IngestJobService ingestJobService,
                               DataSource etlDataSource,
                               ObjectMapper ingestObjectMapper,
                               ConversionService conversionService,
                               JsonTemplateEngine jsonTemplateEngine,
                               UserService userService,
                               String ingestUser,
                               String ingestPassword,
                               List<String> preExecutionQueries) {

        super(ingestJob, ingestJobService);

        jdbcTemplate = new JdbcTemplate(etlDataSource);

        this.userService = userService;
        this.ingestUser = ingestUser;
        this.ingestPassword = ingestPassword;

        this.preExecutionQueries = preExecutionQueries;

        // Create the thread executor service
        executor = Executors.newFixedThreadPool(getJobDetail().getThreadPool());

        ingestUtil = new ConvertAndSaveUtil(this,
                ingestObjectMapper,
                conversionService,
                jsonTemplateEngine,
                jdbcTemplate,
                getJobDetail().getIngestService(),
                getJobDetail().getIngestMethod(),
                getJobDetail().getJsonTemplate(),
                getJobDetail().getJsonType(),
                getJobDetail().getSimpleType(),
                getJobDetail().getEtlUpdateQuery(),
                getJobDetail().getIngestLogProperty());
    }

    @Override
    protected void preIngest() {
        // Auth the ingest user
        authenticate();
    }

    @Override
    protected void postIngest() {
        log.info("Unsecure the channel");
        SpringSecurityHelper.unsecureChannel();
    }

    protected void executeInternal(JobExecutionContext quartzContext) {

        try {
            validate();

            // Run any pre-execution queries defined for all jobs
            runGlobalPreExecutionQueries();

            // Run any pre-execution queries defined for this particular job
            runJobPreExecutionQueries();

            // Query the ETL for items to process
            log.info(message("Starting main query"));
            List rows = executeQuery();
            log.info(message("Queried row count: " + rows.size()));
            setTotalCount(rows.size());

            // Get the authorized principal
            Principal securityPrincipal = SpringSecurityHelper.getSecurityContextPrincipal();

            // Process the rows and ingest batches
            // If the Ingest method is not a List method, batch sized forced to 1
            startProcessingTimer();
            int batchSize = ingestUtil.isListMethod() ? getJobDetail().getBatchSize() : 1;
            int count = 0;
            int submitCount = 0;
            List<Object> objects = new ArrayList<Object>(batchSize);
            ExecutorCompletionService<List<Object>> completionService = new ExecutorCompletionService<List<Object>>(executor);
            for (Object row : rows) {

                objects.add(row);

                // If the batch size has been hit, ingest them now
                if (++count >= batchSize) {
                    if (batchSize > 1) {
                        log.debug(message("Create Execution for batch of size: " + batchSize));
                    }

                    completionService.submit(new Execution(objects, securityPrincipal));
                    submitCount++;

                    objects = new ArrayList<Object>(batchSize);
                    count = 0;
                }
            }

            // Process any remaining objects
            if (!objects.isEmpty()) {
                log.debug(message("Create Execution for remaining items"));
                completionService.submit(new Execution(objects, securityPrincipal));
                submitCount++;
            }

            // Wait for all executions
            log.info("Wait for all executions");
            for (int i=0; i<submitCount; i++) {
                try {
                    completionService.take();
                } catch (InterruptedException e) {
                    log.error(message("Caught InterruptedException while waiting for ETL execution", e));
                }
            }

            // Run any post-execution queries defined for this particular job
            runJobPostExecutionQueries();

            log.info(message("Finished job: " + getJobDetail().getName()));
        } catch (RuntimeException e) {
            setFatalResult();
            log.error(message("Error occurred during job", e));
            throw e;
        } catch (Exception e) {
            setFatalResult();
            log.error(message("Error occurred during job", e));
            throw new LelaException("Error occurred during job", e);
        } finally {
            executor.shutdownNow();
        }
    }

    private void runJobPreExecutionQueries() {
        if (getJobDetail().getPreExecutionQueries() != null && !getJobDetail().getPreExecutionQueries().isEmpty()) {
            for (String query : getJobDetail().getPreExecutionQueries()) {
                log.info(message("Run job-level pre-execution query: " + query));
                jdbcTemplate.execute(query);
            }
        }
    }

    private void runJobPostExecutionQueries() {
        if (getJobDetail().getPostExecutionQueries() != null && !getJobDetail().getPostExecutionQueries().isEmpty()) {
            for (String query : getJobDetail().getPostExecutionQueries()) {
                log.info(message("Run job-level post-execution query: " + query));
                jdbcTemplate.execute(query);
            }
        }
    }

    private void runGlobalPreExecutionQueries() {
        if (preExecutionQueries != null && !preExecutionQueries.isEmpty()) {
            for (String query : preExecutionQueries) {
                log.info(message("Run global pre-execution query: " + query));
                jdbcTemplate.execute(query);
            }
        }
    }

    private List executeQuery() {
        if (ingestUtil.isExecuteJsonTemplate()) {
            // We want a Map returned for template processing
            return jdbcTemplate.queryForList(getJobDetail().getSqlQuery());
        } else {
            // Let the JdbcTemplate convert the single column to our defined type
            return jdbcTemplate.queryForList(getJobDetail().getSqlQuery(), ingestUtil.getQueryType());
        }
    }

    private void authenticate() {
        // Authenticate with Spring Security
        Principal u      = null;
        User user   = userService.findUserByEmail(ingestUser);

        if (user != null) {
            u = new Principal(user);
        }

        // Check user
        if (u != null) {
            if (userService.isValid(u.getPassword(), ingestPassword)) {
                log.info("Secure the channel");
                SpringSecurityHelper.secureChannel(u);
            } else {
                // No user for email
                log.error("Password incorrect for ingest user: " + ingestUser);
                throw new IllegalArgumentException("Password incorrect for ingest user: " + ingestUser);
            }
        } else {
            // No user for email
            log.error("No ingest user found for: " + ingestUser);
            throw new IllegalArgumentException("No ingest user found for: " + ingestUser);
        }
    }

    private void validate() {
        if (!StringUtils.hasText(getJobDetail().getName())) {
            log.error("No ingest job name defined");
            throw new IllegalArgumentException("No ingest job name defined");
        }

        if (!StringUtils.hasText(getJobDetail().getSqlQuery())) {
            log.error(message("Must define SQL query"));
            throw new IllegalArgumentException(message("Must define SQL query"));
        }

        if (!StringUtils.hasText(getJobDetail().getIngestMethod())) {
            log.error(message("Must define Ingest Method"));
            throw new IllegalArgumentException(message("Must define Ingest Method"));
        }

        if ((getJobDetail().getJsonType() == null && getJobDetail().getSimpleType() == null) ||
            (getJobDetail().getJsonType() != null && getJobDetail().getSimpleType() != null)) {
            log.error(message("Must define Ingest JSON Type OR Simple Type"));
            throw new IllegalArgumentException(message("Must define Ingest JSON Type OR Simple Type"));
        }

        if (getJobDetail().getJsonTemplate() != null && getJobDetail().getJsonType() == null) {
            log.error(message("If Json Template is defined, must also define Ingest JSON Type"));
            throw new IllegalArgumentException(message("If Json Template is defined, must also define Ingest JSON Type"));
        }

        if (getJobDetail().getBatchSize() == null ||
                getJobDetail().getBatchSize() < 1 ||
                getJobDetail().getBatchSize() > 10000) {

            log.error(message("Batch size must be between 1 and 10,000"));
            throw new IllegalArgumentException(message("Batch size must be between 1 and 10,000"));
        }

    }

    private class Execution implements Callable<List<Object>> {

        private List<Object> rows;
        private Principal securityPrincipal;

        public Execution(List<Object> rows, Principal securityPrincipal) {
            this.rows = rows;
            this.securityPrincipal = securityPrincipal;
        }

        @Override
        public List<Object> call() throws Exception {
            List<Object> objects = new ArrayList<Object>(rows.size());

            try
            {
                SpringSecurityHelper.secureChannel(securityPrincipal);

                for (Object row : rows) {
                    Object object = null;

                    try {
                        object = ingestUtil.convert(row);
                    } catch (IOException e) {
                        log.error(message("Could not convert row: " + row), e);
                        addErrorCount(1);
                        setErrorResult();
                        continue;
                    }

                    if (object != null) {
                        objects.add(object);
                    } else {
                        addSkippedCount(1);
                    }
                }

                // If the batch size has been hit, ingest them now
                try {
                    log.debug(message("Ingest batch of size: " + objects.size()));
                    addProcessed(ingestUtil.ingest(objects));
                    addProcessedCount(objects.size());
                } catch (Exception e) {
                    log.error(message("Could not ingest objects", e));
                    addErrorCount(objects.size());
                    setErrorResult();
                }
            } catch (Exception e) {
                setErrorResult();
                log.error(message("Unexpected Error occurred during job", e));
            } finally {
                SpringSecurityHelper.unsecureChannel();
            }

            return objects;
        }
    }
}
