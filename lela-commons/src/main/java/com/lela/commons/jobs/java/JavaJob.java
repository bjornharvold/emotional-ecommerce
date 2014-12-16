/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.commons.jobs.java;

import com.lela.commons.jobs.ExecutionContext;

/**
 * User: Chris Tallent
 */
public interface JavaJob {
    void execute(JavaExecutionContext context);
}
