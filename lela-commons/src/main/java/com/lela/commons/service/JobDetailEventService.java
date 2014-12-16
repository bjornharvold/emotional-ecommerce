package com.lela.commons.service;

import com.lela.commons.event.ingest.JobErrorEvent;
import com.lela.commons.event.ingest.JobFailedEvent;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 11/16/12
 * Time: 1:28 PM
 * To change this template use File | Settings | File Templates.
 */
public interface JobDetailEventService {

    public void jobFailed(JobFailedEvent jobFailedEvent);
    public void jobError(JobErrorEvent jobErrorEvent);
}
