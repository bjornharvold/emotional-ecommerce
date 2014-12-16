/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.commons.jobs.chain;

import com.lela.commons.jobs.ExecutionContext;
import com.lela.commons.service.IngestJobService;
import com.lela.domain.enums.JobResult;
import com.lela.commons.jobs.AbstractExecutionContext;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.LinkedList;

/**
 * User: Chris Tallent
 * Date: 2/28/12
 * Time: 2:59 PM
 */
public class ChainExecutionContext extends AbstractExecutionContext<ChainJobDetail> implements ExecutionContext, TriggerListener {

    private static final Logger log = LoggerFactory.getLogger(ChainExecutionContext.class);

    private final Scheduler scheduler;
    private final ChainJobDetail detail;
    private JobExecutionContext quartzContext;

    private LinkedList<JobDetail> currentQueue;
    
    private final Object lock = new Object();

    public ChainExecutionContext(Scheduler scheduler, ChainJobDetail detail, IngestJobService ingestJobService) {
        super(detail, ingestJobService);
        this.scheduler = scheduler;
        this.detail = detail;
    }

    protected void executeInternal(JobExecutionContext quartzContext) {
        this.quartzContext = quartzContext;
        if (detail.getJobs() != null && !detail.getJobs().isEmpty()) {
            try {
                scheduler.addTriggerListener(this);

                currentQueue = new LinkedList<JobDetail>(detail.getJobs());
                scheduleNext();

                synchronized (lock) {
                    // Have the primary thread wait for all chained jobs
                    // Don't set the wait() if the queue has been emptied
                    // because of a scheduler failure in the scheduleNext() method
                    if (!currentQueue.isEmpty()) {
                        log.warn("Main run() thread waiting");
                        lock.wait();
                    }
                }
                log.warn("Job Chain complete");
            } catch (Exception e) {
                setFatalResult();
                log.error("Job chain wait interrupted", e);
                throw new RuntimeException("Job Chain wait interrupted", e);
            } finally {
                try {
                    // Try to remove the scheduler listener
                    scheduler.removeTriggerListener(getName());
                }
                catch (SchedulerException e) {
                    log.error("Could not remove trigger listener for job chain: " + getName(), e);
                }
            }
        }
        else {
            message("No child jobs defined");
        }
    }

    @Override
    public String getName() {
        return "chain-" + detail.getName();
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context, int triggerInstructionCode) {
        log.warn("Trigger complete");
        message("Completed child job: " + context.getJobDetail().getName());
        
        // Check the result of the job
        if (JobResult.ERROR.equals(context.getResult())) {
            setErrorResult();
        } else if (JobResult.FATAL.equals(context.getResult())) {
            setFatalResult();
        }

        scheduleNext();
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        log.error("Trigger misfired for trigger: " + trigger.getName());

        // Check the result of the job
        setErrorResult();

        scheduleNext();
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        // Not implemented for this class
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        // Not implemented for this class
        return false;
    }

    private void scheduleNext() {
        if (!currentQueue.isEmpty()) {
            JobDetail job = currentQueue.poll();
            message("Starting child job: " + job.getName());

            Date date = new Date();
            Trigger trigger = new org.quartz.SimpleTrigger(
                    getName() + "-" + job.getName() + "-" + date,
                    Scheduler.DEFAULT_MANUAL_TRIGGERS,
                    date);
            trigger.setJobName(job.getName());
            trigger.setJobGroup(job.getGroup());
            trigger.addTriggerListener(getName());

            try {
                scheduler.scheduleJob(trigger);
            } catch (SchedulerException e) {
                setErrorResult();
                message("Error with triggered chain job", e);
                log.error("Error with triggered chain job", e);
                scheduleNext();
            }
        } else {
            // Job chain is complete
            synchronized (lock) {
                log.warn("Notify waiting run() thread");
                lock.notifyAll();
            }
        }
    }
}
