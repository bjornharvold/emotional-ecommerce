/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.ingest.test.jobs;

import com.lela.ingest.test.AbstractFunctionalTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.listeners.TriggerListenerSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Bjorn Harvold
 * Date: 11/23/11
 * Time: 10:40 PM
 * Responsibility:
 */
public class TalendJobFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(TalendJobFunctionalTest.class);
    private static final int WAIT_TIMEOUT = 10000;

    private static final String TARGET_METHOD               = "runJobInTOS";
    private static final String CONTEXT_TOKEN               = "--context=DEFAULT";
    private static final String CONTEXT_PARAM_TOKEN         = "--context_param";

    private static final String LelaETL_Server = "LelaETL_Server";
    private static final String LelaETL_Port = "LelaETL_Port";
    private static final String LelaETL_Database = "LelaETL_Database";
    private static final String LelaETL_Login = "LelaETL_Login";
    private static final String LelaETL_Password = "LelaETL_Password";
    private static final String LelaETL_AdditionalParams = "LelaETL_AdditionalParams";
    private static final String LelaETL_LogPath = "LelaETL_LogPath";
    private static final String LelaETL_LogFile = "LelaETL_LogFile";

    @Autowired
    @Qualifier("ingestJobScheduler")
    private Scheduler scheduler;

    private static SuccessTalendJob successJob;
    private static FailTalendJob failJob;

    @Before
    @After
    public void initStaticVariables() {
        successJob = null;
        failJob = null;
    }

    @Test
    public void testTalendJob() {

//        // Create the trigger listener that will wait for the chain job to complete and wake up the test
//        TestTriggerListener listener = new TestTriggerListener();
//        try {
//            TalendJobDetail detail = new TalendJobDetail();
//            detail.setName("testSuccessTalendJobDetail");
//            detail.setTalendClass(SuccessTalendJob.class.getName());
//
//            // Create the Chain job executor and pass in the chain job
//            // Note this also auto-registers the chain job with the scheduler
//            List<TalendJobDetail> jobs = new ArrayList<TalendJobDetail>();
//            jobs.add(detail);
//
//            // Map of context arguments for talend job
//            Map<String, String> args = new HashMap<String, String>();
//            args.put(LelaETL_Server, "testServer");
//            args.put(LelaETL_Port, "testPort");
//            args.put(LelaETL_Database, "testDatabase");
//            args.put(LelaETL_Login, "testLogin");
//            args.put(LelaETL_Password, "testPassword");
//            args.put(LelaETL_AdditionalParams, "testAdditionalParams");
//
//            TalendJobExecutorImpl executor = new TalendJobExecutorImpl(scheduler, args, jobs);
//            executor.afterPropertiesSet();
//
//            // Create a Quartz trigger to run the chain job
//            Date date = new Date();
//            Trigger trigger = new org.quartz.SimpleTrigger(
//                    "testSuccessTalendJobTrigger",
//                    Scheduler.DEFAULT_MANUAL_TRIGGERS,
//                    date);
//            trigger.setJobName(detail.getName());
//            trigger.setJobGroup(detail.getGroup());
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
//                log.info("Main Talend Success test waking: " + new Date());
//            }
//
//            // Check the listener results
//            assertFalse(listener.errorMessage, listener.error);
//            assertTrue("Talend job did not complete", listener.complete);
//
//            assertNotNull("No talend job was instantiated", successJob);
//            assertTrue("Talend job runJobInTOS method was not executed", successJob.executed);
//            assertTrue("Talend job was not passed the context argument", successJob.setContext);
//
//            assertEquals("Talend job invalid argument for LelaETL_Server", args.get(LelaETL_Server), successJob.actualArgs.get(LelaETL_Server));
//            assertEquals("Talend job invalid argument for LelaETL_Port", args.get(LelaETL_Port), successJob.actualArgs.get(LelaETL_Port));
//            assertEquals("Talend job invalid argument for LelaETL_Database", args.get(LelaETL_Database), successJob.actualArgs.get(LelaETL_Database));
//            assertEquals("Talend job invalid argument for LelaETL_Login", args.get(LelaETL_Login), successJob.actualArgs.get(LelaETL_Login));
//            assertEquals("Talend job invalid argument for LelaETL_Password", args.get(LelaETL_Password), successJob.actualArgs.get(LelaETL_Password));
//            assertEquals("Talend job invalid argument for LelaETL_AdditionalParams", args.get(LelaETL_AdditionalParams), successJob.actualArgs.get(LelaETL_AdditionalParams));
//
//            // Get the execution context for the talend job and verify the success result
//            TalendExecutionContext context = executor.getExecutionContext(detail);
//            assertNotNull("No execution context saved for Talend job execution", context);
//            assertEquals("Talend job is not marked as completed", JobStatus.DONE, context.getStatus());
//            assertEquals("Talend job is not marked as success", JobResult.SUCCESS, context.getResult());
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

    @Test
    public void testFailTalendJob() {

        // Create the trigger listener that will wait for the chain job to complete and wake up the test
//        TestTriggerListener listener = new TestTriggerListener();
//        try {
//            TalendJobDetail detail = new TalendJobDetail();
//            detail.setName("testFailTalendJobDetail");
//            detail.setTalendClass(FailTalendJob.class.getName());
//
//            // Create the Chain job executor and pass in the chain job
//            // Note this also auto-registers the chain job with the scheduler
//            List<TalendJobDetail> jobs = new ArrayList<TalendJobDetail>();
//            jobs.add(detail);
//
//            TalendJobExecutorImpl executor = new TalendJobExecutorImpl(scheduler, new HashMap<String, String>(), jobs);
//            executor.afterPropertiesSet();
//
//            // Create a Quartz trigger to run the chain job
//            Date date = new Date();
//            Trigger trigger = new org.quartz.SimpleTrigger(
//                    "testFailTalendJobTrigger",
//                    Scheduler.DEFAULT_MANUAL_TRIGGERS,
//                    date);
//            trigger.setJobName(detail.getName());
//            trigger.setJobGroup(detail.getGroup());
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
//                log.info("Main Talend Fail test waking: " + new Date());
//            }
//
//            // Check the listener results
//            assertFalse(listener.errorMessage, listener.error);
//            assertTrue("Talend job did not complete", listener.complete);
//
//            assertNotNull("No talend job was instantiated", failJob);
//            assertTrue("Talend job runJobInTOS method was not executed", failJob.executed);
//
//            // Get the execution context for the talend job and verify the FAIL result
//            TalendExecutionContext context = executor.getExecutionContext(detail);
//            assertNotNull("No execution context saved for Talend job execution", context);
//            assertEquals("Talend job is not marked as completed", JobStatus.DONE, context.getStatus());
//            assertEquals("Talend job is not marked as Error", JobResult.ERROR, context.getResult());
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
     * Test Talend Job that will accept the arguments passed in and create a set of testable properties
     */
    public static class SuccessTalendJob {

        public boolean executed = false;
        public boolean setContext = false;
        public Map<String, String> actualArgs = new HashMap<String, String>();

        public SuccessTalendJob() {
            TalendJobFunctionalTest.successJob = this;
        }

        public int runJobInTOS(String[] args) {
            executed = true;

            if (args != null) {
                String lastArg = null;
                for (String arg : args) {
                    if (CONTEXT_TOKEN.equals(arg)) {
                        setContext = true;
                    } else if (CONTEXT_PARAM_TOKEN.equals(lastArg)) {
                        // Try to split the argument on "="
                        String[] tokens = arg.split("=");
                        if (tokens != null && tokens.length > 1) {
                            actualArgs.put(tokens[0], tokens[1]);
                        }
                    }
                    
                    lastArg = arg;
                }
            }

            return 0;
        }
    }

    /**
     * Test Talend Job that will "Fail"
     */
    public static class FailTalendJob {

        public boolean executed = false;

        public FailTalendJob() {
            TalendJobFunctionalTest.failJob = this;
        }

        public int runJobInTOS(String[] args) {
            executed = true;

            // Return failure
            return 1;
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
                log.info("Notify Talend main test: " + new Date());
                this.notifyAll();
            }
        }
    }
}
