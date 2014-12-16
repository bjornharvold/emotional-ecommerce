/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.jobs.etl;

import com.google.common.collect.Lists;
import com.lela.commons.LelaException;
import com.lela.commons.jobs.AbstractCountingExecutionContext;
import com.lela.commons.jobs.ExecutionContext;
import com.lela.commons.jobs.AbstractExecutionContext;
import com.lela.commons.service.IngestJobService;
import com.lela.commons.service.UserService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.domain.document.User;
import com.lela.domain.dto.Principal;
import com.lela.ingest.json.JsonTemplateEngine;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.velocity.exception.MethodInvocationException;
import org.codehaus.jackson.map.ObjectMapper;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Chris Tallent
 * Date: 12/1/11
 * Time: 10:49 AM
 */
public class SynchronizationExecutionContext extends AbstractCountingExecutionContext<SynchronizationJobDetail> implements ExecutionContext {

    private static final Logger log = LoggerFactory.getLogger(SynchronizationExecutionContext.class);

    private static final String ISDIRTY = "isdirty";
    private static final String RLNM = "rlnm";

    private final IngestJobService ingestJobService;
    private final JdbcTemplate jdbcTemplate;
    private final MongoTemplate mongoTemplate;
    private final UserService userService;
    private final String ingestUser;
    private final String ingestPassword;

    private final List<String> preExecutionQueries;

    private ExecutorService executor;

    private ConvertAndSaveUtil ingestUtil;
    private ConvertAndSaveUtil deleteUtil;

    private String dirtyProperty;
    private String idProperty;

    public SynchronizationExecutionContext(SynchronizationJobDetail ingestJob,
                                           IngestJobService ingestJobService,
                                           DataSource etlDataSource,
                                           MongoTemplate mongoTemplate,
                                           ObjectMapper ingestObjectMapper,
                                           ConversionService conversionService,
                                           JsonTemplateEngine jsonTemplateEngine,
                                           UserService userService,
                                           String ingestUser,
                                           String ingestPassword,
                                           List<String> preExecutionQueries) {

        super(ingestJob, ingestJobService);

        jdbcTemplate = new JdbcTemplate(etlDataSource);

        this.ingestJobService = ingestJobService;
        this.mongoTemplate = mongoTemplate;
        this.userService = userService;
        this.ingestUser = ingestUser;
        this.ingestPassword = ingestPassword;

        this.preExecutionQueries = preExecutionQueries;

        dirtyProperty = StringUtils.hasText(getJobDetail().getDirtyProperty()) ? getJobDetail().getDirtyProperty() : ISDIRTY;
        idProperty = StringUtils.hasText(getJobDetail().getIdProperty()) ? getJobDetail().getIdProperty() : RLNM;

        ingestUtil = new ConvertAndSaveUtil(this,
                ingestObjectMapper,
                conversionService,
                jsonTemplateEngine,
                jdbcTemplate,
                getJobDetail().getUpsert());

        deleteUtil = new ConvertAndSaveUtil(this,
                ingestObjectMapper,
                conversionService,
                jsonTemplateEngine,
                jdbcTemplate,
                getJobDetail().getDelete());

        // Create the thread executor service
        executor = Executors.newFixedThreadPool(getJobDetail().getThreadPool());
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

            // Query the Mongo collection ... Make sure we only query
            // For categories that were included in the ETL items so that we
            // don't accidently delete items from categories not included
            BasicQuery query = null;
            if (StringUtils.hasText(getJobDetail().getMongoFields())) {
                query = new BasicQuery(getJobDetail().getMongoQuery(), getJobDetail().getMongoFields());
            } else {
                query = new BasicQuery(getJobDetail().getMongoQuery());
            }

            Class dtoClass = getJobDetail().getMongoDto() != null ? getJobDetail().getMongoDto() : getJobDetail().getUpsert().getJsonType();
            List items = mongoTemplate.find(query, dtoClass, mongoTemplate.getCollectionName(getJobDetail().getUpsert().getJsonType()));

            // Build a set of Mongo ids
            Set<Object> mongoIds = new HashSet<Object>();
            for (Object mongoData : items) {
                mongoIds.add(String.valueOf(getProperty(mongoData, idProperty)));
            }

            // Build a set of ETL ids
            // UPSERTS
            Set<Object> etlIds = new HashSet<Object>();
            List<Object> upserts = new ArrayList<Object>();
            for (Object row : rows) {
                Object id = getProperty(row, idProperty);
                etlIds.add(id);

                // If the id is in ETL but not in Mongo, update
                boolean dirty = Boolean.valueOf(String.valueOf(getProperty(row, dirtyProperty)));
                if (dirty || !mongoIds.contains(id)) {
                    upserts.add(row);
                }
            }

            message("Found " + upserts.size() + " Upserts");

            // DELETE: What ids are in the Mongo collection but NOT in the ETL query
            List<Object> deletes = new ArrayList<Object>();
            for (Object id : mongoIds) {
                if (!etlIds.contains(id)) {
                    deletes.add(id);
                }
            }

            message("Found " + deletes.size() + " Deletes");

            setTotalCount(upserts.size() + deletes.size());

            // Get the authorized principal
            Principal securityPrincipal = SpringSecurityHelper.getSecurityContextPrincipal();

            // Process the rows and ingest batches
            // If the Ingest method is not a List method, batch sized forced to 1
            startProcessingTimer();

            message("\n** INSERT AND UPDATE **\n");
            batchAndExecuteIngest(upserts, securityPrincipal);

            message("\n** DELETE **\n");
            batchAndExecuteDelete(deletes, securityPrincipal);

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

    private void batchAndExecuteIngest(List rows, Principal securityPrincipal) {
        int batchSize = ingestUtil.isListMethod() ? getJobDetail().getBatchSize() : 1;
        int count = 0;
        int submitCount = 0;

        ExecutorCompletionService<List<Object>> completionService = new ExecutorCompletionService<List<Object>>(executor);


        //Group like objects together so that they are saved together
        if (getJobDetail().getUpsert().getSorter()!=null)
        {
            Map<Object, Collection<Object>> batches = getJobDetail().getUpsert().getSorter().sort(rows);
            for(Object key:batches.keySet())
            {

                List objects = Lists.newArrayList(batches.get(key));
                message("Processing group: " + key.toString() + " with " + objects.size() + " objects ");
                //submitCount += processUpsertBatch(objects, securityPrincipal, batchSize, count, completionService);
                completionService.submit(new UpsertExecution(objects, securityPrincipal, getJobDetail().getUpsert().getValidator()));
                submitCount += 1;
            }
        }
        else
        {
            submitCount += processUpsertBatch(rows, securityPrincipal, batchSize, count, completionService, getJobDetail());
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
    }

    private int processUpsertBatch(List rows, Principal securityPrincipal, int batchSize, int count, ExecutorCompletionService<List<Object>> completionService, SynchronizationJobDetail jobDetail) {
        int submitCount = 0;
        List<Object> objects = new ArrayList<Object>(batchSize);
        for (Object row : rows) {

            objects.add(row);

            // If the batch size has been hit, ingest them now
            if (++count >= batchSize) {
                if (batchSize > 1) {
                    log.debug(message("Create Upsert execution for batch of size: " + batchSize));
                }

                completionService.submit(new UpsertExecution(objects, securityPrincipal, jobDetail.getUpsert().getValidator()));
                submitCount++;

                objects = new ArrayList<Object>(batchSize);
                count = 0;
            }
        }

        // Process any remaining objects
        if (!objects.isEmpty()) {
            log.debug(message("Create Upsert execution for remaining items"));
            completionService.submit(new UpsertExecution(objects, securityPrincipal, jobDetail.getUpsert().getValidator()));
            submitCount++;
        }
        return submitCount;
    }

    private void batchAndExecuteDelete(List rows, Principal securityPrincipal) {
        int batchSize = deleteUtil.isListMethod() ? getJobDetail().getBatchSize() : 1;
        int count = 0;
        int submitCount = 0;
        List<Object> objects = new ArrayList<Object>(batchSize);
        ExecutorCompletionService<List<Object>> completionService = new ExecutorCompletionService<List<Object>>(executor);
        for (Object row : rows) {

            objects.add(row);

            // If the batch size has been hit, ingest them now
            if (++count >= batchSize) {
                if (batchSize > 1) {
                    log.debug(message("Create Delete execution for batch of size: " + batchSize));
                }

                completionService.submit(new DeleteExecution(objects, securityPrincipal));
                submitCount++;

                objects = new ArrayList<Object>(batchSize);
                count = 0;
            }
        }

        // Process any remaining objects
        if (!objects.isEmpty()) {
            log.debug(message("Create Delete execution for remaining items"));
            completionService.submit(new DeleteExecution(objects, securityPrincipal));
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
    }

    private Object getProperty(Object o, String property)
    throws Exception {
        Object result = null;
//        if (o instanceof Map) {
//            result = String.valueOf(((Map) o).get(property));
//        } else {
//            result = BeanUtils.getProperty(o, property);
//        }

        result = PropertyUtils.getProperty(o, property);

        return result;
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

        if (!StringUtils.hasText(getJobDetail().getMongoQuery())) {
            log.error(message("Must define Mongo query"));
            throw new IllegalArgumentException(message("Must define Mongo query"));
        }

        if (getJobDetail().getUpsert() == null) {
            log.error(message("Must define Upsert Arguments"));
            throw new IllegalArgumentException(message("Must define Upsert Arguments"));
        }

        if (getJobDetail().getDelete() == null) {
            log.error(message("Must define Delete Arguments"));
            throw new IllegalArgumentException(message("Must define Delete Arguments"));
        }

        if (!StringUtils.hasText(getJobDetail().getUpsert().getIngestMethod())) {
            log.error(message("Must define Upsert Ingest Method"));
            throw new IllegalArgumentException(message("Must define Upsert Ingest Method"));
        }

        if (!StringUtils.hasText(getJobDetail().getDelete().getIngestMethod())) {
            log.error(message("Must define Delete Method"));
            throw new IllegalArgumentException(message("Must define Delete Method"));
        }

        if ((getJobDetail().getUpsert().getJsonType() == null && getJobDetail().getUpsert().getSimpleType() == null) ||
            (getJobDetail().getUpsert().getJsonType() != null && getJobDetail().getUpsert().getSimpleType() != null)) {
            log.error(message("Must define Ingest JSON Type OR Simple Type"));
            throw new IllegalArgumentException(message("Must define Ingest JSON Type OR Simple Type"));
        }

        if (getJobDetail().getUpsert().getJsonTemplate() != null && getJobDetail().getUpsert().getJsonType() == null) {
            log.error(message("If Json Template is defined, must also define Ingest JSON Type"));
            throw new IllegalArgumentException(message("If Json Template is defined, must also define Ingest JSON Type"));
        }

        if ((getJobDetail().getDelete().getJsonType() == null && getJobDetail().getDelete().getSimpleType() == null) ||
                (getJobDetail().getDelete().getJsonType() != null && getJobDetail().getDelete().getSimpleType() != null)) {
            log.error(message("Must define Delete JSON Type OR Simple Type"));
            throw new IllegalArgumentException(message("Must define Delete JSON Type OR Simple Type"));
        }

        if (getJobDetail().getDelete().getJsonTemplate() != null && getJobDetail().getDelete().getJsonType() == null) {
            log.error(message("If Delete Json Template is defined, must also define Delete JSON Type"));
            throw new IllegalArgumentException(message("If Delete Json Template is defined, must also define Delete JSON Type"));
        }

        if (getJobDetail().getBatchSize() == null ||
                getJobDetail().getBatchSize() < 1 ||
                getJobDetail().getBatchSize() > 10000) {

            log.error(message("Batch size must be between 1 and 10,000"));
            throw new IllegalArgumentException(message("Batch size must be between 1 and 10,000"));
        }

    }

    private class UpsertExecution implements Callable<List<Object>> {

        private List<Object> rows;
        private Principal securityPrincipal;
        private Validator validator;

        public UpsertExecution(List<Object> rows, Principal securityPrincipal, Validator validator) {
            this.rows = rows;
            this.securityPrincipal = securityPrincipal;
            this.validator = validator;
        }

        @Override
        public List<Object> call() throws Exception {
            List<Object> objects = new ArrayList<Object>(rows.size());
            SpringSecurityHelper.secureChannel(securityPrincipal);
            try
            {

                log.info(message("Converting "+ rows.size() + " rows..."));
                for (Object row : rows) {
                    Object object = null;

                    try {
                        object = ingestUtil.convert(row);
                        if(validator!=null)
                        {
                            Set<ConstraintViolation<Object>> violations = validator.validate(object);
                            if(violations.size()>0)
                            {
                                log.error(message("Object failed validation " + object.toString() + ", raw data: " + row));

                                for(ConstraintViolation<Object> violation:violations)
                                {
                                    log.error(message("property: " + violation.getPropertyPath().toString() + " is " + violation.getInvalidValue() + "; error is: " + violation.getMessage()));
                                }
                                object = null;
                            }

                        }
                    } catch (IOException e) {
                        log.error(message("Could not convert row: " + row, e));
                        addErrorCount(1);
                        setErrorResult();
                        continue;
                    } catch (MethodInvocationException e) {
                        log.error(message("Template Exception occurred during job for row: " + row));
                        log.error(message(e.getMessage(), e));
                        addErrorCount(1);
                        setErrorResult();
                        continue;
                    } catch (Exception e) {
                        log.error(message("Unknown exception occurred converting row" + row));
                        log.error(message(e.getMessage(), e));
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
                log.info(message("Completed converting rows..."));
                // If the batch size has been hit, ingest them now
                try {
                    log.debug(message("Ingest batch of size: " + objects.size()));
                    addProcessed(ingestUtil.ingest(objects));
                    log.debug(message("Ingested batch of size: " + objects.size()));
                    addProcessedCount(objects.size());
                } catch (Exception e) {
                    log.error(message("Could not ingest objects: " + objects.toString(), e));
                    addErrorCount(objects.size());
                    setErrorResult();
                }
            } catch (Exception e) {
                setErrorResult();
                log.error(message("Unexpected Error occurred during job", e));
            }finally {
                SpringSecurityHelper.unsecureChannel();
            }

            return objects;
        }
    }

    private class DeleteExecution implements Callable<List<Object>> {

        private List<Object> rows;
        private Principal securityPrincipal;

        public DeleteExecution(List<Object> rows, Principal securityPrincipal) {
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
                        object = deleteUtil.convert(row);
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
                    log.debug(message("Delete batch of size: " + objects.size()));
                    addProcessed(deleteUtil.ingest(objects));
                    addProcessedCount(objects.size());
                } catch (Exception e) {
                    log.error(message("Could not delete objects"), e);
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
