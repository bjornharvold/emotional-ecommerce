/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.commons.jobs;

import com.lela.domain.document.JobExecution;
import com.lela.domain.document.JobMessage;

import java.util.List;

/**
 * User: Chris Tallent
 * Date: 8/20/12
 * Time: 5:08 PM
 */
public class JobSummary {
    private JobExecution execution;
    private List<JobMessage> messages;

    public JobExecution getExecution() {
        return execution;
    }

    public void setExecution(JobExecution execution) {
        this.execution = execution;
    }

    public List<JobMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<JobMessage> messages) {
        this.messages = messages;
    }
}
