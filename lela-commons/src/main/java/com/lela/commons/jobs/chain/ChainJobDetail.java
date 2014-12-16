/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.commons.jobs.chain;

import com.lela.commons.jobs.AbstractJobDetail;
import org.quartz.JobDetail;

import java.util.List;

/**
 * User: Chris Tallent
 * Date: 2/28/12
 * Time: 2:59 PM
 */
public class ChainJobDetail extends AbstractJobDetail {

    private List<JobDetail> jobs;

    public List<JobDetail> getJobs() {
        return jobs;
    }

    public void setJobs(List<JobDetail> jobs) {
        this.jobs = jobs;

        // A chain must have child jobs
        if (jobs == null || jobs.isEmpty()) {
            throw new IllegalArgumentException("A job chain must define child jobs");
        }
        
        // A chain cannot run chains
        for (JobDetail job : jobs) {
            if (job instanceof ChainJobDetail) {
                throw new IllegalArgumentException("A job chain cannot be nested in another job chain");
            }
        }
    }
}
