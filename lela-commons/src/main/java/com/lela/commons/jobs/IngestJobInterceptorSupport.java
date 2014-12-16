/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.commons.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Chris Tallent
 * Date: 3/20/12
 * Time: 8:58 AM
 */
public class IngestJobInterceptorSupport implements IngestJobInterceptor {

    private final static Logger log = LoggerFactory.getLogger(IngestJobInterceptorSupport.class);

    /**
     * Execute an action before the ingest job starts
     * @param context Execution context of the current job run
     * @return True if the ingest should continue, false if it should terminate.  False will cause JobResult to be Fatal
     */
    @Override
    public boolean ingestStarting(ExecutionContext context) {
        log.debug("ingestStarting not implemented");
        return true;
    }

    /**
     * Execute an action after the ingest job completes with a Success or Error status.  This will NOT be
     * called if the job terminated with a Fatal JobResult or Exception.
     *
     * Job Result can be checked by calling context.getJobResult()
     *
     * @param context Execution context of the current job run
     * @return True if the ingest should continue, false if it should terminate.  False will cause JobResult to be Fatal
     */
    @Override
    public boolean ingestCompleted(ExecutionContext context) {
        log.debug("ingestStarting not implemented");
        return true;
    }
}
