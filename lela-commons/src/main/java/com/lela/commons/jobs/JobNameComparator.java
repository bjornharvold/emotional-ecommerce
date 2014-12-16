/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.jobs;

import com.lela.domain.document.JobKey;

import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: Chris Tallent
 * Date: 12/1/11
 * Time: 10:49 AM
 */
public class JobNameComparator implements Comparator<JobKey> {

    @Override
    public int compare(JobKey lhs, JobKey rhs) {
        String lhsName = lhs != null ? lhs.getJobName() : "";
        String rhsName = rhs != null ? rhs.getJobName() : "";
        
        return lhsName.compareTo(rhsName);
    }
}
