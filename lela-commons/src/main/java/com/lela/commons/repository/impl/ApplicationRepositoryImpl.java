/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.repository.impl;

import com.lela.commons.repository.ApplicationRepositoryCustom;
import com.lela.domain.document.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * User: Chris Tallent
 * Date: 6/6/12
 * Time: 10:00 AM
 */
public class ApplicationRepositoryImpl implements ApplicationRepositoryCustom {

    private static final Logger log = LoggerFactory.getLogger(ApplicationRepositoryImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Application> findAll(Integer page, Integer maxResults, List<String> fields) {
        Query query = query(where("id").exists(true));

        if (page != null && maxResults != null) {
            query.skip(page*maxResults);
            query.limit(maxResults);
        }

        if (fields != null && !fields.isEmpty()) {
            for (String field : fields) {
                query.fields().include(field);
            }
        }

        return mongoTemplate.find(query, Application.class);
    }

    @Override
    public List<Application> findAll(List<String> fields) {
        return findAll(null, null, fields);
    }
}
