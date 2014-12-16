package com.lela.commons.jobs;

import java.util.List;

/**
 * User: Chris Tallent
 * Date: 5/24/12
 * Time: 1:10 AM
 */
public interface IngestJobDetail {
    String getName();
    String getGroupName();
    List<IngestJobInterceptor> getInterceptors();
    JobExecutor getJobExecutor();
    Class getJobClass();
    List<JobParameter> getParameters();
}
