/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.commons.jobs.AvailableJobs;
import com.lela.commons.jobs.IngestJobDetail;
import com.lela.commons.jobs.JobListing;
import com.lela.commons.jobs.JobSummary;
import com.lela.domain.document.JobKey;
import com.lela.domain.document.JobExecution;
import com.lela.domain.document.JobMessage;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Chris Tallent
 * Date: 12/2/11
 * Time: 8:45 AM
 */
public interface IngestJobService {
    AvailableJobs getAvailableJobs();
    JobListing getJobListing(JobKey jobKey);
    boolean executeJob(JobKey jobKey, Map<String, String> parameters);
    IngestJobDetail getJob(JobKey jobKey);
    JobSummary getSummary(JobKey jobKey);
    JobExecution saveJobExecution(JobExecution jobExecution);
    JobExecution findJobExecution(ObjectId jobExecutionId);
    JobMessage saveJobMessage(ObjectId jobExecutionId, String message);
    JobMessage saveJobMessage(ObjectId jobExecutionId, String message, Exception e);
    List<JobMessage> getMessages(ObjectId jobExecutionId);
    void markAllJobExecutionsAsEnded();
    void removeOldJobLogs();
    void pauseJob(JobKey jobKey)  throws Exception;
    void resumeJob(JobKey jobKey) throws Exception ;
    void assignTriggerToJob(String jobName, String triggerUrl) throws Exception ;
}
