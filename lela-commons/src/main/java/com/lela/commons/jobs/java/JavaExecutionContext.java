/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.commons.jobs.java;

import com.lela.commons.jobs.AbstractCountingExecutionContext;
import com.lela.commons.jobs.ExecutionContext;
import com.lela.commons.jobs.AbstractExecutionContext;
import com.lela.commons.service.IngestJobService;
import org.quartz.JobExecutionContext;

/**
 * User: Chris Tallent
 * Date: 5/11/12
 * Time: 3:46 PM
 */
public class JavaExecutionContext extends AbstractCountingExecutionContext<JavaJobDetail> implements ExecutionContext {
    public JavaExecutionContext(JavaJobDetail ingestJob, IngestJobService ingestJobService) {
        super(ingestJob, ingestJobService);
    }

    @Override
    protected void executeInternal(JobExecutionContext quartzContext) {
        JavaJob job = getJobDetail().getJob();
        job.execute(this);
    }
}
