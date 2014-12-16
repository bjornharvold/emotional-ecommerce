/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import org.springframework.data.annotation.Transient;

/**
 * Created by IntelliJ IDEA.
 * User: Chris Tallent
 * Date: 12/2/11
 * Time: 3:29 PM
 */
public class JobKey {
    private String jobName;
    private String groupName = "DEFAULT";

    public JobKey(String jobName) {
        this.jobName = jobName;
    }

    public String getJobName() {
        return jobName;
    }

    public String getGroupName() {
        return groupName;
    }

    @Override
    public String toString() {
        return groupName + "." + jobName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JobKey jobKey = (JobKey) o;

        if (groupName != null ? !groupName.equals(jobKey.groupName) : jobKey.groupName != null) return false;
        if (jobName != null ? !jobName.equals(jobKey.jobName) : jobKey.jobName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = jobName != null ? jobName.hashCode() : 0;
        result = 31 * result + (groupName != null ? groupName.hashCode() : 0);
        return result;
    }
}
