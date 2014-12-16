/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.jobs.java;


import com.lela.commons.jobs.AbstractJobDetail;

/**
 * Created by IntelliJ IDEA.
 * User: Chris Tallent
 * Date: 12/1/11
 * Time: 10:49 AM
 */
public class JavaJobDetail extends AbstractJobDetail {

    private JavaJob job;

    public JavaJob getJob() {
        return job;
    }

    public void setJob(JavaJob job) {
        this.job = job;
    }
}
