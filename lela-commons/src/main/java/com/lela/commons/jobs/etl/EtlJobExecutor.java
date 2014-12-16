/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.jobs.etl;

import com.lela.commons.jobs.JobExecutor;
import com.lela.commons.service.IngestJobService;
import com.lela.domain.document.JobKey;
import com.lela.commons.service.UserService;
import com.lela.ingest.json.JsonTemplateEngine;
import org.codehaus.jackson.map.ObjectMapper;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Chris Tallent
 * Date: 12/1/11
 * Time: 10:49 AM
 */
public class EtlJobExecutor implements JobExecutor<EtlJobDetail, EtlExecutionContext>, InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(EtlJobExecutor.class);

    private final Scheduler scheduler;
    private final DataSource etlDataSource;
    private final UserService userService;
    private final IngestJobService ingestJobService;
    private final String ingestUser;
    private final String ingestPassword;

    private final ObjectMapper ingestObjectMapper;
    private final ConversionService conversionService;
    private final JsonTemplateEngine jsonTemplateEngine;
    
    private final List<String> preExecutionQueries;

    private Map<EtlJobDetail, EtlExecutionContext> contexts = new HashMap<EtlJobDetail, EtlExecutionContext>();
    private List<EtlJobDetail> jobs;

    public EtlJobExecutor(@Qualifier("etlDataSource") DataSource etlDataSource,
                          ObjectMapper ingestObjectMapper,
                          ConversionService conversionService,
                          JsonTemplateEngine jsonTemplateEngine,
                          IngestJobService ingestJobService,
                          UserService userService,
                          String ingestUser,
                          String ingestPassword,
                          List<String> preExecutionQueries,
                          Scheduler scheduler,
                          List<EtlJobDetail> jobs) {
        this.etlDataSource = etlDataSource;
        this.ingestObjectMapper = ingestObjectMapper;
        this.conversionService = conversionService;
        this.jsonTemplateEngine = jsonTemplateEngine;
        this.ingestJobService = ingestJobService;
        this.userService = userService;
        this.ingestUser = ingestUser;
        this.ingestPassword = ingestPassword;
        this.preExecutionQueries = preExecutionQueries;
        this.scheduler = scheduler;
        this.jobs = jobs;
    }

    @Override
    public String getDisplayName() {
        return "ETL Jobs";
    }

    @Override
    public List<EtlJobDetail> getJobs() {
        return jobs;
    }

    @Override
    public EtlJobDetail getJob(JobKey jobKey) {
        for (EtlJobDetail job : jobs) {
            if (job.getName().equals(jobKey.getJobName()) && job.getGroup().equals(jobKey.getGroupName())) {
                return job;
            }
        }

        return null;
    }

    @Override
    public void execute(EtlJobDetail ingestJob, JobExecutionContext quartzContext) {
        EtlExecutionContext context = new EtlExecutionContext(
                ingestJob,
                ingestJobService,
                etlDataSource,
                ingestObjectMapper,
                conversionService,
                jsonTemplateEngine,
                userService,
                ingestUser,
                ingestPassword,
                preExecutionQueries);

        contexts.put(ingestJob, context);
        context.execute(quartzContext);
    }

    @Override
    public EtlExecutionContext getExecutionContext(EtlJobDetail ingestJob) {
        return contexts.get(ingestJob);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (EtlJobDetail job : jobs) {
            job.setJobExecutor(this);
            scheduler.addJob(job, false);
        }
    }
}
