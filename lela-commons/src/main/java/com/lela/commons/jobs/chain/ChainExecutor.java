/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.jobs.chain;

import com.lela.commons.jobs.JobExecutor;
import com.lela.commons.service.IngestJobService;
import com.lela.domain.document.JobKey;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.springframework.beans.factory.InitializingBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Chris Tallent
 */
public class ChainExecutor implements JobExecutor<ChainJobDetail, ChainExecutionContext>, InitializingBean {

    private final Scheduler scheduler;
    private final IngestJobService ingestJobService;
    private final List<ChainJobDetail> jobs;

    private Map<ChainJobDetail, ChainExecutionContext> contexts = new HashMap<ChainJobDetail, ChainExecutionContext>();

    public ChainExecutor(Scheduler scheduler,
                         IngestJobService ingestJobService, List<ChainJobDetail> jobs) {
        this.scheduler = scheduler;
        this.ingestJobService = ingestJobService;
        this.jobs = jobs;
    }

    @Override
    public String getDisplayName() {
        return "Chain Jobs";
    }

    @Override
    public List<ChainJobDetail> getJobs() {
        return jobs;
    }

    @Override
    public ChainJobDetail getJob(JobKey jobKey) {
        for (ChainJobDetail job : jobs) {
            if (job.getName().equals(jobKey.getJobName()) && job.getGroup().equals(jobKey.getGroupName())) {
                return job;
            }
        }

        return null;
    }

    @Override
    public void execute(ChainJobDetail job, JobExecutionContext quartzContext) {
        ChainExecutionContext context = new ChainExecutionContext(scheduler, job, ingestJobService);

        contexts.put(job, context);
        context.execute(quartzContext);
    }

    @Override
    public ChainExecutionContext getExecutionContext(ChainJobDetail job) {
        return contexts.get(job);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (ChainJobDetail job : jobs) {
            job.setJobExecutor(this);
            scheduler.addJob(job, true);
        }
    }
}
