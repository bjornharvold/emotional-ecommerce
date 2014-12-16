package com.lela.commons.jobs;

import com.lela.commons.service.IngestJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: Chris Tallent
 * Date: 8/21/12
 * Time: 8:43 AM
 */
public class JobLogCleanup implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(JobLogCleanup.class);

    private final IngestJobService ingestJobService;

    @Autowired
    public JobLogCleanup(IngestJobService ingestJobService) {
        this.ingestJobService = ingestJobService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Marking all prior running jobs as ended in database");
        ingestJobService.markAllJobExecutionsAsEnded();

        log.info("Removing old job log entries");
        ingestJobService.removeOldJobLogs();
    }
}
