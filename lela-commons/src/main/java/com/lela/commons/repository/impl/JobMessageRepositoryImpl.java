/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.commons.repository.impl;

import com.lela.commons.repository.JobMessageRepositoryCustom;
import com.lela.domain.document.JobMessage;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * User: Chris Tallent
 * Date: 6/6/12
 * Time: 10:00 AM
 */
public class JobMessageRepositoryImpl implements JobMessageRepositoryCustom {

    private static final Logger log = LoggerFactory.getLogger(UserTrackerRepositoryImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<JobMessage> findAllJobMessages(ObjectId jobExecutionId) {
        Query query = query(where("jobExecutionId").is(jobExecutionId));
        query.sort().on("cdt", Order.ASCENDING);

        return mongoTemplate.find(query, JobMessage.class);
    }

    @Override
    public void removeOlderThan(Date date) {
        Query query = query(where("cdt").lt(date));
        mongoTemplate.remove(query, JobMessage.class);
    }
}