/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.repository;

import com.lela.domain.enums.TaskType;

import java.util.Date;

/**
 * User: Chris Tallent
 * Date: 8/30/12
 * Time: 10:47 AM
 */
public interface TaskRepositoryCustom {
    void removeTasksOlderThan(TaskType taskType, Date date);
}
