package com.lela.domain.document;

import com.lela.domain.enums.JobResult;
import com.lela.domain.enums.JobStatus;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Chris Tallent
 * Date: 8/20/12
 * Time: 1:53 PM
 */
@Document
public class JobExecution extends AbstractDocument {
    private JobKey jobKey;
    protected JobStatus status = JobStatus.NOT_STARTED;
    protected JobResult result = null;
    protected Date startDate;
    protected Date endDate;
    private Map<String, Object> attribs = new HashMap<String, Object>();

    public JobExecution(JobKey jobKey) {
        this.jobKey = jobKey;
    }

    public JobKey getJobKey() {
        return jobKey;
    }

    public void setJobKey(JobKey jobKey) {
        this.jobKey = jobKey;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public JobResult getResult() {
        return result;
    }

    public void setResult(JobResult result) {
        this.result = result;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Map<String, Object> getAttribs() {
        return attribs;
    }

    public void setAttribs(Map<String, Object> attribs) {
        this.attribs = attribs;
    }
}
