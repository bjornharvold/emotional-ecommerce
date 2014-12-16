/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.jobs.talend;

import com.lela.domain.ApplicationConstants;
import com.lela.commons.jobs.JobExecutor;
import com.lela.commons.service.IngestJobService;
import com.lela.domain.document.JobKey;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.springframework.beans.factory.InitializingBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Chris Tallent
 */
public class TalendJobExecutor implements JobExecutor<TalendJobDetail, TalendExecutionContext>, InitializingBean {

    private final Scheduler scheduler;
    private final IngestJobService ingestJobService;
    private Map<String, String> defaultContext;
    private final List<TalendJobDetail> jobs;

    private Map<TalendJobDetail, TalendExecutionContext> contexts = new HashMap<TalendJobDetail, TalendExecutionContext>();

    public TalendJobExecutor(Scheduler scheduler,
                             IngestJobService ingestJobService,
                             Map<String, String> defaultContext,
                             List<TalendJobDetail> jobs) {
        this.scheduler = scheduler;
        this.ingestJobService = ingestJobService;
        this.defaultContext = defaultContext;
        this.jobs = jobs;
    }

    @Override
    public String getDisplayName() {
        return "Talend Jobs";
    }

    @Override
    public List<TalendJobDetail> getJobs() {
        return jobs;
    }

    @Override
    public TalendJobDetail getJob(JobKey jobKey) {
        for (TalendJobDetail job : jobs) {
            if (job.getName().equals(jobKey.getJobName()) && job.getGroup().equals(jobKey.getGroupName())) {
                return job;
            }
        }

        return null;
    }

    @Override
    public void execute(TalendJobDetail job, JobExecutionContext quartzContext) {
        JobDataMap jobDataMap = quartzContext.getMergedJobDataMap();
        Map<String, String> parameters = (Map<String, String>)jobDataMap.get(ApplicationConstants.JOB_PARAMETERS);

        TalendExecutionContext context = new TalendExecutionContext(job, defaultContext, ingestJobService, parameters);

        contexts.put(job, context);
        context.execute(quartzContext);
    }

    @Override
    public TalendExecutionContext getExecutionContext(TalendJobDetail job) {
        return contexts.get(job);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (TalendJobDetail job : jobs) {
            job.setJobExecutor(this);
            scheduler.addJob(job, true);
        }
    }
}
