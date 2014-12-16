/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.commons.jobs;

import com.lela.domain.enums.JobResult;
import com.lela.domain.enums.JobStatus;

import java.util.Date;
import java.util.List;

public interface ExecutionContext {
    List<Object> getProcessed();
    JobStatus getStatus();
    JobResult getResult();
    String message(Object message);
    String message(Object message, Exception e);
    void setErrorResult();
    void setFatalResult();
}
