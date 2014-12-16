/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.jobs.social;

import com.lela.commons.jobs.ExecutionContext;
import com.lela.commons.jobs.java.JavaExecutionContext;
import com.lela.commons.jobs.java.JavaJob;
import com.lela.commons.jobs.java.JavaJobDetail;
import com.lela.commons.service.TaskService;
import com.lela.domain.enums.TaskType;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * User: Chris Tallent
 * Date: 8/30/12
 * Time: 10:37 AM
 */
public class FacebookTaskCleanupJob extends JavaJobDetail implements JavaJob {
    private static final int EXPIRATION_MINUTES = 3 * 60;  // 3 hours

    private final TaskService taskService;

    public FacebookTaskCleanupJob(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void execute(JavaExecutionContext context) {
        DateTime removeOlderThan = new DateTime();
        removeOlderThan = removeOlderThan.minusMinutes(EXPIRATION_MINUTES);

        taskService.removeTasksOlderThan(TaskType.FACEBOOK_DATA_AGGREGATION, new Date(removeOlderThan.getMillis()));
    }

    @Override
    public JavaJob getJob() {
        return this;
    }
}
