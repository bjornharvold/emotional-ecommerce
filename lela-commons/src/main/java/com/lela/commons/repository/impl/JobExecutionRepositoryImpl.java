/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.commons.repository.impl;

import com.lela.commons.jobs.JobSummary;
import com.lela.commons.repository.JobExecutionRepositoryCustom;
import com.lela.domain.document.JobExecution;
import com.lela.domain.document.JobKey;
import com.lela.domain.document.JobMessage;
import com.lela.domain.enums.JobStatus;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * User: Chris Tallent
 * Date: 6/6/12
 * Time: 10:00 AM
 */
public class JobExecutionRepositoryImpl implements JobExecutionRepositoryCustom {

    private static final Logger log = LoggerFactory.getLogger(UserTrackerRepositoryImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public JobExecution findLatestForJobKey(JobKey jobKey) {
        Query query = query(where("jobKey.jobName").is(jobKey.getJobName()).and("jobKey.groupName").is(jobKey.getGroupName()));
        query.sort().on("cdt", Order.DESCENDING);
        query.limit(1);

        return mongoTemplate.findOne(query, JobExecution.class);
    }

    @Override
    public void markAllAsEnded() {
        Query query = Query.query(where("status").is(JobStatus.RUNNING));
        Update update = Update.update("status", JobStatus.DONE).set("endDate", new Date());
        mongoTemplate.updateMulti(query, update, JobExecution.class);
    }

    @Override
    public void removeOlderThan(Date date) {
        Query query = query(where("cdt").lt(date));
        mongoTemplate.remove(query, JobExecution.class);
    }
}