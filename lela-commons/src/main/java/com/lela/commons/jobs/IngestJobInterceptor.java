/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.commons.jobs;

/**
 * User: Chris Tallent
 * Date: 3/20/12
 * Time: 8:58 AM
 */
public interface IngestJobInterceptor {
    /**
     * Execute an action before the ingest job starts
     * @param context Execution context of the current job run
     * @return True if the ingest should continue, false if it should terminate.  False will cause JobResult to be Fatal
     */
    boolean ingestStarting(ExecutionContext context);

    /**
     * Execute an action after the ingest job completes with a Success or Error status.  This will NOT be
     * called if the job terminated with a Fatal JobResult or Exception.
     *
     * Job Result can be checked by calling context.getJobResult()
     *
     * @param context Execution context of the current job run
     * @return True if the ingest should continue, false if it should terminate.  False will cause JobResult to be Fatal
     */
    boolean ingestCompleted(ExecutionContext context);
}
