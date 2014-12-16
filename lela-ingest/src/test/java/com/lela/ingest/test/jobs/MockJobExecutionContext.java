/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.ingest.test.jobs;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.spi.TriggerFiredBundle;

import java.util.Date;

/**
 * User: Chris Tallent
 * Date: 3/20/12
 * Time: 1:06 PM
 */
public class MockJobExecutionContext extends JobExecutionContext {

    public static MockJobExecutionContext getMock(JobDetail detail) {
        Scheduler scheduler = null;

        Date date = new Date();
        Trigger trigger = new org.quartz.SimpleTrigger(
                "mockTrigger" + date,
                Scheduler.DEFAULT_MANUAL_TRIGGERS,
                date);
        trigger.setJobName(detail.getName());
        trigger.setJobGroup(detail.getGroup());

        TriggerFiredBundle firedBundle = new TriggerFiredBundle(detail, trigger, null, false, new Date(), new Date(), null, null);
        Job job = null;

        return new MockJobExecutionContext(scheduler, firedBundle, job);
    }

    public MockJobExecutionContext(Scheduler scheduler, TriggerFiredBundle firedBundle, Job job) {
        super(scheduler, firedBundle, job);
    }


}
