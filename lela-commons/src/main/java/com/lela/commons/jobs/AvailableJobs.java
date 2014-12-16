/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.commons.jobs;

import java.util.List;
import java.util.Map;

/**
 * User: Chris Tallent
 * Date: 3/2/12
 * Time: 9:51 AM
 */
public class AvailableJobs {
    private List<String> groups;
    private Map<String, List<JobListing>> jobs;

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public Map<String, List<JobListing>> getJobs() {
        return jobs;
    }

    public void setJobs(Map<String, List<JobListing>> jobs) {
        this.jobs = jobs;
    }
}
