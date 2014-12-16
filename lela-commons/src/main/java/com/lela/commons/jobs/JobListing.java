/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.jobs;

import com.lela.domain.document.CronTrigger;
import com.lela.domain.document.JobKey;
import com.lela.domain.enums.JobResult;
import com.lela.domain.enums.JobStatus;
import org.quartz.Trigger;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Chris Tallent
 * Date: 12/1/11
 * Time: 10:49 AM
 */
public class JobListing implements Comparable<JobListing> {
    
    private JobKey jobKey;
    private String jobType;
    private List<Trigger> triggers;
    private List<TriggerDetail> triggerDetailList;
    /**
     * We will support only one trigger per job for now
     */
    private CronTrigger currentTrigger;
    
    private JobStatus status = JobStatus.NOT_STARTED;
    private JobResult result;
    private JobSummary lastSummary;
    private List<JobParameter> parameters;
    public JobListing(){}
    public JobListing(JobKey jobKey, String jobType) {
        this.jobKey = jobKey;
        this.jobType = jobType;
    }

    public JobListing(JobKey jobKey, String jobType, List<JobParameter> parameters) {
        this.jobKey = jobKey;
        this.jobType = jobType;
        this.parameters = parameters;
    }

    public JobListing(JobKey jobKey, String jobType, List<JobParameter> parameters, List<Trigger> triggers, JobSummary lastSummary) {
        this.jobKey = jobKey;
        this.jobType = jobType;
        this.parameters = parameters;
        this.triggers = triggers;
        this.lastSummary = lastSummary;

        if (lastSummary != null) {
            status = lastSummary.getExecution().getStatus();
            result = lastSummary.getExecution().getResult();
        }
    }

    public JobKey getJobKey() {
        return jobKey;
    }

    public void setJobKey(JobKey jobKey) {
        this.jobKey = jobKey;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public List<Trigger> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<Trigger> triggers) {
        this.triggers = triggers;
    }

    public JobSummary getLastSummary() {
        return lastSummary;
    }

    public void setLastSummary(JobSummary lastSummary) {
        this.lastSummary = lastSummary;
    }

    public JobStatus getStatus() {
        return status;
    }

    public JobResult getResult() {
        return result;
    }

    public List<JobParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<JobParameter> parameters) {
        this.parameters = parameters;
    }

    @Override
    public int compareTo(JobListing jl) {
        return jobKey.getJobName().compareTo(jl.getJobKey().getJobName());
    }

	public List<TriggerDetail> getTriggerDetailList() {
		return triggerDetailList;
	}

	public void setTriggerDetailList(List<TriggerDetail> triggerDetailList) {
		this.triggerDetailList = triggerDetailList;
	}

	public CronTrigger getCurrentTrigger() {
		return currentTrigger;
	}

	public void setCurrentTrigger(CronTrigger currentTrigger) {
		this.currentTrigger = currentTrigger;
	}

}
