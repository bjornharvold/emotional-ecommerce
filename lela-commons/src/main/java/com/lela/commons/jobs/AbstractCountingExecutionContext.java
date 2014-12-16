/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.jobs;

import com.lela.commons.jobs.etl.EtlJobDetail;
import com.lela.commons.service.IngestJobService;

import java.util.Date;

/**
 * User: Chris Tallent
 * Date: 10/18/12
 * Time: 11:22 AM
 */
public abstract class AbstractCountingExecutionContext<T extends AbstractJobDetail> extends AbstractExecutionContext<T> {
    private static final String ERROR_COUNT = "errorCount";
    private static final String SKIPPED_COUNT = "skippedCount";
    private static final String PROCESSED_COUNT = "processedCount";
    private static final String AVERAGE_TIME = "averageTime";
    private static final String TOTAL_COUNT = "totalCount";
    private static final String TRACKING_COUNT_FLAG = "trackingCount";

    private Date startProcessingDate;

    public AbstractCountingExecutionContext(T jobDetail, IngestJobService ingestJobService) {
        super(jobDetail, ingestJobService);

        putAttrib(TOTAL_COUNT, 0);
        putAttrib(PROCESSED_COUNT, 0);
        putAttrib(SKIPPED_COUNT, 0);
        putAttrib(ERROR_COUNT, 0);
        putAttrib(AVERAGE_TIME, 0.0);
        putAttrib(TRACKING_COUNT_FLAG, false);
    }

    public void startProcessingTimer() {
        startProcessingDate = new Date();
        putAttrib(TRACKING_COUNT_FLAG, true);
    }

    public synchronized void setTotalCount(int totalCount) {
        putAttrib(TOTAL_COUNT, totalCount);
    }

    public synchronized void addErrorCount(int errors) {
        addToAttrib(ERROR_COUNT, errors);
    }

    public synchronized void addSkippedCount(int skipped) {
        addToAttrib(SKIPPED_COUNT, skipped);
    }

    public synchronized void addProcessedCount(int processed) {
        if (startProcessingDate == null) {
            startProcessingTimer();
        }

        addToAttrib(PROCESSED_COUNT, processed);

        if (processed > 0 || (Integer)getAttrib(PROCESSED_COUNT) > 0) {
            putAttrib(AVERAGE_TIME, (new Date().getTime() - startProcessingDate.getTime()) / (Integer)getAttrib(PROCESSED_COUNT) / 1000.0);
        }
    }
}
