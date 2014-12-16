package com.lela.commons.jobs;

import com.lela.domain.document.JobKey;
import org.quartz.JobExecutionContext;

import java.util.List;

/**
 * User: Chris Tallent
 * Date: 5/23/12
 * Time: 1:39 PM
 */
public interface JobExecutor<T extends IngestJobDetail, C extends ExecutionContext> {
    public String getDisplayName();
    public void execute(T detail, JobExecutionContext context);
    public List<T> getJobs();
    public T getJob(JobKey jobKey);
    public C getExecutionContext(T detail);
}
