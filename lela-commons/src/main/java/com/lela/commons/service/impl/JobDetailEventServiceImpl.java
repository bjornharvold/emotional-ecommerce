package com.lela.commons.service.impl;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.lela.commons.event.ingest.JobErrorEvent;
import com.lela.commons.event.ingest.JobFailedEvent;
import com.lela.commons.service.EmailService;
import com.lela.commons.service.JobDetailEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 11/16/12
 * Time: 1:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class JobDetailEventServiceImpl implements JobDetailEventService {
    private static final Logger log = LoggerFactory.getLogger(JobDetailEventServiceImpl.class);
    private final EmailService emailService;
    private final String alertEmailAddress;
    private final String environment;

    public JobDetailEventServiceImpl(EmailService emailService, String environment, String alertEmailAddress) {
        this.emailService = emailService;
        this.environment = environment;
        this.alertEmailAddress = alertEmailAddress;
    }

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void jobFailed(JobFailedEvent jobFailedEvent) {
        try
        {
           emailService.sendEmail(alertEmailAddress, String.format("[%s] [DATA] [%s] FATAL", this.environment, jobFailedEvent.getJobName()), String.format("%s failed on '%s' at %s.", jobFailedEvent.getJobName(), this.environment, jobFailedEvent.getDate()));
        } catch (Exception e)
        {
           log.error("Couldn't send job failed email.", e);
        }
    }

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void jobError(JobErrorEvent jobErrorEvent) {
        try
        {
            emailService.sendEmail(alertEmailAddress, String.format("[%s] [DATA] [%s] ERROR", this.environment, jobErrorEvent.getJobName()), String.format("%s had an error on '%s' at %s.", jobErrorEvent.getJobName(), this.environment, jobErrorEvent.getDate()));
        } catch (Exception e)
        {
            log.error("Couldn't send job had error email.", e);
        }
    }
}
