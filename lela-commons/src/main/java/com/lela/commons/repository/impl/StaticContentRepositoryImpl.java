/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.repository.impl;

import com.lela.commons.repository.StaticContentRepositoryCustom;
import com.lela.domain.document.StaticContent;
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
public class StaticContentRepositoryImpl implements StaticContentRepositoryCustom {

    private static final Logger log = LoggerFactory.getLogger(StaticContentRepositoryImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<StaticContent> findAll(Integer page, Integer size, List<String> fields) {
        Query query = query(where("id").exists(true));

        query.skip(page * size);
        query.limit(size);

        if (fields != null && !fields.isEmpty()) {
            for (String field : fields) {
                query.fields().include(field);
            }
        }

        return mongoTemplate.find(query, StaticContent.class);
    }

    @Override
    public List<StaticContent> findAll(List<String> fields) {
        Query query = query(where("id").exists(true));

        if (fields != null && !fields.isEmpty()) {
            for (String field : fields) {
                query.fields().include(field);
            }
        }

        return mongoTemplate.find(query, StaticContent.class);
    }
}
