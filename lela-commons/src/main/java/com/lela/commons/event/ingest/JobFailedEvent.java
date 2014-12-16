package com.lela.commons.event.ingest;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 11/16/12
 * Time: 1:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class JobFailedEvent {

    String jobName;
    Date date;

    public JobFailedEvent(String jobName, Date date)
    {
        this.jobName = jobName;
        this.date = date;
    }

    public String getJobName() {
        return jobName;
    }

    public Date getDate() {
        return date;
    }
}
