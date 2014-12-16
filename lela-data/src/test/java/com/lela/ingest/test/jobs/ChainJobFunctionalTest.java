/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.ingest.test.jobs;

import com.lela.ingest.test.AbstractFunctionalTest;
import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdScheduler;
import org.quartz.listeners.TriggerListenerSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Bjorn Harvold
 * Date: 11/23/11
 * Time: 10:40 PM
 * Responsibility:
 */
public class ChainJobFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(ChainJobFunctionalTest.class);
    private static final int WAIT_TIMEOUT = 10000;

    @Autowired
    @Qualifier("ingestJobScheduler")
    private Scheduler scheduler;

    @Test
    public void testJobChain() {

//        // Create the trigger listener that will wait for the chain job to complete and wake up the test
//        TestTriggerListener listener = new TestTriggerListener();
//        try {
//
//            // Used to track that the sub-jobs were actually executed
//            Set<JobDetail> completed = new HashSet<JobDetail>();
//
//            // Create the test job details
//            TestJobDetail detail1 = new TestJobDetail("testJobChainDetail1", completed);
//            scheduler.addJob(detail1, true);
//
//            TestJobDetail detail2 = new TestJobDetail("testJobChainDetail2", completed);
//            scheduler.addJob(detail2, true);
//
//            // Create the Chain to be tested
//            List<JobDetail> jobs = new ArrayList<JobDetail>();
//            jobs.add(detail1);
//            jobs.add(detail2);
//
//            ChainJobDetail chain = new ChainJobDetail();
//            chain.setName("testStoreJobChain");
//            chain.setJobs(jobs);
//            chain.afterPropertiesSet();
//
//            // Create the Chain job executor and pass in the chain job
//            // Note this also auto-registers the chain job with the scheduler
//            List<ChainJobDetail> chainJobs = new ArrayList<ChainJobDetail>();
//            chainJobs.add(chain);
//
//            ChainExecutorImpl chainExecutor = new ChainExecutorImpl(scheduler, chainJobs);
//            chainExecutor.afterPropertiesSet();
//
//            // Create a Quartz trigger to run the chain job
//            Date date = new Date();
//            Trigger trigger = new org.quartz.SimpleTrigger(
//                    "testJobChainTrigger",
//                    Scheduler.DEFAULT_MANUAL_TRIGGERS,
//                    date);
//            trigger.setJobName(chain.getName());
//            trigger.setJobGroup(chain.getGroup());
//
//            // Add the trigger listener to wake the main thread
//            trigger.addTriggerListener(listener.getName());
//            scheduler.addTriggerListener(listener);
//
//            // Schedule the chain job
//            scheduler.scheduleJob(trigger);
//
//            synchronized (listener) {
//                listener.wait(WAIT_TIMEOUT);
//                log.info("Main chain test waking: " + new Date());
//            }
//
//            // Check the listener results
//            assertFalse(listener.errorMessage, listener.error);
//            assertTrue("Chain job did not complete", listener.complete);
//            assertTrue("Chain detail 1 was not executed", completed.contains(detail1));
//            assertTrue("Chain detail 2 was not executed", completed.contains(detail2));
//        } catch (Exception e) {
//            e.printStackTrace();
//            fail(e.getMessage());
//        } finally {
////            try {
////                scheduler.removeTriggerListener(listener.getName());
////            } catch (SchedulerException e) {
////                e.printStackTrace();
////                fail(e.getMessage());
////            }
//        }
    }

    /**
     * Test Job Detail that will take a specific job name and a Set that will track
     * the various job details that are completed for later assertion in the main test thread
     */
    public class TestJobDetail extends JobDetail {

        public final Set<JobDetail> completed;

        public TestJobDetail(String jobName, Set<JobDetail> completed) {
            setName(jobName);
            this.completed = completed;
        }
        
        @Override
        public String getGroup() {
            return StdScheduler.DEFAULT_GROUP;
        }

        @Override
        public Class getJobClass() {
            return TestJob.class;
        }

        @Override
        public boolean isDurable() {
            return false;
        }
    }

    /**
     * Required by Quartz to actually execute the instance.  Created "new" for each invocation.
     * This will track that the job was executed.
     */
    public static class TestJob implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            log.info("EXECUTING TEST JOB");
            ((TestJobDetail)context.getJobDetail()).completed.add(context.getJobDetail());
        }
    }

    /**
     * Quartz trigger listener owned by the main test thread that will be used to wake up / notify the
     * main thread when the full job chain has been completed by Quartz
     */
    private class TestTriggerListener extends TriggerListenerSupport {

        public boolean complete = false;
        public boolean error = false;
        public String errorMessage;
        
        public HashSet<JobDetail> completedJobs = new HashSet<JobDetail>();
        
        @Override
        public String getName() {
            return "testJobChainTriggerListener";
        }

        @Override
        public void triggerMisfired(Trigger trigger) {
            log.info("Trigger misfired");
            error = true;
            errorMessage = "Trigger Misfired";
        }

        @Override
        public void triggerComplete(Trigger trigger, JobExecutionContext context, int triggerInstructionCode) {
            log.info("Trigger Listener Complete");
            synchronized (this) {
                complete = true;
                log.info("Notify chain main test: " + new Date());
                this.notifyAll();
            }
        }
    }
}
