/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.commons.jobs.java;

import com.lela.commons.jobs.JobExecutor;
import com.lela.commons.service.IngestJobService;
import com.lela.domain.document.JobKey;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Chris Tallent
 * Date: 12/1/11
 * Time: 10:49 AM
 */
public class JavaJobExecutor implements JobExecutor<JavaJobDetail, JavaExecutionContext>, InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(JavaJobExecutor.class);

    private final Scheduler scheduler;
    private final IngestJobService ingestJobService;

    private Map<JavaJobDetail, JavaExecutionContext> contexts = new HashMap<JavaJobDetail, JavaExecutionContext>();
    private final List<JavaJobDetail> jobs;

    public JavaJobExecutor(Scheduler scheduler,
                           IngestJobService ingestJobService,
                           List<JavaJobDetail> jobs) {
        this.scheduler = scheduler;
        this.ingestJobService = ingestJobService;
        this.jobs = jobs;
    }

    @Override
    public String getDisplayName() {
        return "Java Jobs";
    }

    @Override
    public List<JavaJobDetail> getJobs() {
        return jobs;
    }

    @Override
    public JavaJobDetail getJob(JobKey jobKey) {
        for (JavaJobDetail job : jobs) {
            if (job.getName().equals(jobKey.getJobName()) && job.getGroup().equals(jobKey.getGroupName())) {
                return job;
            }
        }

        return null;
    }

    @Override
    public void execute(JavaJobDetail ingestJob, JobExecutionContext quartzContext) {
        JavaExecutionContext context = new JavaExecutionContext(ingestJob, ingestJobService);

        contexts.put(ingestJob, context);
        context.execute(quartzContext);
    }

    @Override
    public JavaExecutionContext getExecutionContext(JavaJobDetail ingestJob) {
        return contexts.get(ingestJob);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (JavaJobDetail job : jobs) {
            job.setJobExecutor(this);
            scheduler.addJob(job, true);
        }
    }
}
