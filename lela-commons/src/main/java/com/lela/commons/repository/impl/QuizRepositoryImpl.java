/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.repository.impl;

import com.lela.commons.repository.QuestionRepositoryCustom;
import com.lela.commons.repository.QuizRepositoryCustom;
import com.lela.domain.document.Analytics;
import com.lela.domain.document.Question;
import com.lela.domain.document.Quiz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

/**
 * Custom user repository
 */
public class QuizRepositoryImpl implements QuizRepositoryCustom {
    private final static Logger log = LoggerFactory.getLogger(QuizRepositoryImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void setDefaultFlagOnCollection(String language, Boolean isDefault) {
        Query query = query(where("dflt").is(true).and("lng").is(language));
        Update update = update("dflt", false);

        mongoTemplate.updateMulti(query, update, Quiz.class);
    }

    @Override
    public List<Quiz> findAll(List<String> fields) {
        Query query = query(where("id").exists(true));

        if (fields != null && !fields.isEmpty()) {
            for (String field : fields) {
                query.fields().include(field);
            }
        }

        return mongoTemplate.find(query, Quiz.class);
    }
}
