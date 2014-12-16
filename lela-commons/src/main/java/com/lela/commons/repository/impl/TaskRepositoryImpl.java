/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.repository.impl;

import com.lela.commons.repository.TaskRepositoryCustom;
import com.lela.domain.document.Task;
import com.lela.domain.enums.TaskType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Date;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * User: Chris Tallent
 * Date: 8/30/12
 * Time: 10:49 AM
 */
public class TaskRepositoryImpl implements TaskRepositoryCustom{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void removeTasksOlderThan(TaskType taskType, Date date) {
        Query query = query(where("tp").is(taskType).and("cdt").lt(date));
        mongoTemplate.remove(query, Task.class);
    }
}
