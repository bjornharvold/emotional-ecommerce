package com.lela.commons.event.ingest;

import com.lela.commons.event.AbstractEvent;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 11/16/12
 * Time: 1:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class JobErrorEvent extends AbstractEvent {

    String jobName;
    Date date;

    public JobErrorEvent(String jobName, Date date)
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
