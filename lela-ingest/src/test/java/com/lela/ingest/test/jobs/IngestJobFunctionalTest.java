/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.ingest.test.jobs;

import com.lela.commons.jobs.AbstractJobDetail;
import com.lela.commons.jobs.IngestJobInterceptor;
import com.lela.commons.jobs.ExecutionContext;
import com.lela.commons.service.IngestJobService;
import com.lela.domain.enums.JobResult;
import com.lela.domain.enums.JobStatus;
import com.lela.commons.jobs.AbstractExecutionContext;
import com.lela.ingest.test.AbstractFunctionalTest;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by Bjorn Harvold
 * Date: 11/23/11
 * Time: 10:40 PM
 * Responsibility:
 */
public class IngestJobFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(IngestJobFunctionalTest.class);
    private static final int WAIT_TIMEOUT = 10000;

    @Autowired
    @Qualifier("ingestJobScheduler")
    private Scheduler scheduler;

    @Autowired
    private IngestJobService ingestJobService;

    @Test
    public void testListeners() {

        log.info("Starting Ingest Listener test");
        TestIngestListener listener = new TestIngestListener();
        TestJobDetail jobDetail = new TestJobDetail();
        jobDetail.setInterceptors(Arrays.<IngestJobInterceptor>asList(listener));

        TestExecutionContext context = new TestExecutionContext(jobDetail);
        context.execute(MockJobExecutionContext.getMock(jobDetail));

        // Ensure that ingest listener was called
        assertEquals("ingestStarting should have been called once", 1, listener.ingestStartingCount);
        assertEquals("ingestCompleted should have been called once", 1, listener.ingestCompletedCount);
        assertEquals("ingest job not marked as done", JobStatus.DONE, context.getStatus());
        assertEquals("ingest job not marked as success", JobResult.SUCCESS, context.getResult());
    }

    @Test
    public void testMultipleListeners() {

        log.info("Starting Ingest Multiple Listener test");
        TestIngestListener listener = new TestIngestListener();
        TestJobDetail jobDetail = new TestJobDetail();
        jobDetail.setInterceptors(Arrays.<IngestJobInterceptor>asList(listener, listener, listener));

        TestExecutionContext context = new TestExecutionContext(jobDetail);
        context.execute(MockJobExecutionContext.getMock(jobDetail));

        // Ensure that ingest listener was called
        assertEquals("ingestStarting should have been called once", 3, listener.ingestStartingCount);
        assertEquals("ingestCompleted should have been called once", 3, listener.ingestCompletedCount);
        assertEquals("ingest job not marked as done", JobStatus.DONE, context.getStatus());
        assertEquals("ingest job not marked as success", JobResult.SUCCESS, context.getResult());
    }

    @Test
    public void testListenerStartFail() {

        log.info("Starting Ingest Listener Failure at start test");
        TestIngestListener listener = new TestIngestListener();
        listener.ingestStartingResult = false;

        TestJobDetail jobDetail = new TestJobDetail();
        jobDetail.setInterceptors(Arrays.<IngestJobInterceptor>asList(listener, listener));

        TestExecutionContext context = new TestExecutionContext(jobDetail);
        
        try {
            context.execute(MockJobExecutionContext.getMock(jobDetail));
        } catch (Exception e) {
            // This should have thrown an exception

            // Ensure that ingest listener was called
            assertEquals("ingestStarting should have been called once", 1, listener.ingestStartingCount);
            assertEquals("ingestCompleted should not have been called", 0, listener.ingestCompletedCount);
            assertEquals("ingest job not marked as done", JobStatus.DONE, context.getStatus());
            assertEquals("ingest job not marked as fatal", JobResult.FATAL, context.getResult());
            
            return;
        }
        
        // An exception should have been thrown
        fail("No exception was thrown by ingest as expected");
    }

    @Test
    public void testListenerCompletedFail() {

        log.info("Starting Ingest Listener Failure at complete test");
        TestIngestListener listener = new TestIngestListener();
        listener.ingestCompletedResult = false;

        TestJobDetail jobDetail = new TestJobDetail();
        jobDetail.setInterceptors(Arrays.<IngestJobInterceptor>asList(listener, listener));

        TestExecutionContext context = new TestExecutionContext(jobDetail);

        try {
            context.execute(MockJobExecutionContext.getMock(jobDetail));
        } catch (Exception e) {
            // This should have thrown an exception

            // Ensure that ingest listener was called
            assertEquals("ingestStarting should have been called once", 2, listener.ingestStartingCount);
            assertEquals("ingestCompleted should not have been called", 1, listener.ingestCompletedCount);
            assertEquals("ingest job not marked as done", JobStatus.DONE, context.getStatus());
            assertEquals("ingest job not marked as fatal", JobResult.FATAL, context.getResult());

            return;
        }

        // An exception should have been thrown
        fail("No exception was thrown by ingest as expected");
    }

    private class TestExecutionContext extends AbstractExecutionContext<TestJobDetail> {

        public TestExecutionContext(TestJobDetail jobDetail) {
            super(jobDetail, ingestJobService);
        }

        @Override
        protected void executeInternal(JobExecutionContext quartzContext) {
            // Do nothing
        }
    }

    private class TestJobDetail extends AbstractJobDetail {
        public TestJobDetail(String jobName) {
            setName(jobName);
            try {
                afterPropertiesSet();
            } catch (Exception e) {
                log.error("Problem calling afterPropertiesSet()", e);
            }
        }
        
        public TestJobDetail() {
            this("testJobDetail" + new Date());
        }
    }

    private class TestIngestListener implements IngestJobInterceptor {

        public int ingestStartingCount = 0;
        public int ingestCompletedCount = 0;

        public boolean ingestStartingResult = true;
        public boolean ingestCompletedResult = true;
        
        @Override
        public boolean ingestStarting(ExecutionContext context) {
            ingestStartingCount++;
            return ingestStartingResult;
        }

        @Override
        public boolean ingestCompleted(ExecutionContext context) {
            ingestCompletedCount++;
            return ingestCompletedResult;
        }
    }
}
