package com.lela.commons.repository;

import com.lela.domain.document.JobExecution;
import com.lela.domain.document.JobKey;

import java.util.Date;

/**
 * User: Chris Tallent
 * Date: 8/20/12
 * Time: 5:20 PM
 */
public interface JobExecutionRepositoryCustom {
    JobExecution findLatestForJobKey(JobKey jobKey);
    void markAllAsEnded();

    void removeOlderThan(Date date);
}
