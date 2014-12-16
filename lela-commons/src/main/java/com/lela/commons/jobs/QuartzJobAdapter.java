package com.lela.commons.jobs;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * User: Chris Tallent
 * Date: 3/1/12
 * Time: 4:09 PM
 */
public class QuartzJobAdapter implements Job, StatefulJob {

    private static final Logger log = LoggerFactory.getLogger(QuartzJobAdapter.class);
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        AbstractJobDetail detail = (AbstractJobDetail) context.getJobDetail();
        if(!detail.isAllowConcurrentRuns())
        {

            List<JobExecutionContext> jobs = null;
            try {
                jobs = context.getScheduler().getCurrentlyExecutingJobs();
                for (JobExecutionContext job : jobs) {
                if (job.getTrigger().equals(context.getTrigger()) && !job.getJobInstance().equals(this)) {
                        log.info("There's another instance running, so leaving " + job.getJobDetail().getName());
                        return;
                    }
                }
            } catch (SchedulerException e) {
                log.error("Could not determine running tasks", e);
            }

        }
        log.info("Running the job" + detail.getName());
        detail.getJobExecutor().execute(detail, context);
    }
}
