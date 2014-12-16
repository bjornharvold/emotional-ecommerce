/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.repository.impl;

import com.lela.commons.repository.QuestionRepositoryCustom;
import com.lela.domain.document.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Custom user repository
 */
public class QuestionRepositoryImpl implements QuestionRepositoryCustom {
    private final static Logger log = LoggerFactory.getLogger(QuestionRepositoryImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Question> findAll(List<String> fields) {
        Query query = query(where("rlnm").exists(true));

        query.sort().on("rlnm", Order.ASCENDING);

        if (fields != null && !fields.isEmpty()) {
            for (String field : fields) {
                query.fields().include(field);
            }
        }

        return mongoTemplate.find(query, Question.class);
    }
}
