/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.repository.impl;

import com.lela.commons.repository.PressRepositoryCustom;
import com.lela.domain.document.Press;
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
public class PressRepositoryImpl implements PressRepositoryCustom {

    private static final Logger log = LoggerFactory.getLogger(PressRepositoryImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Press> findAllPressUrlNames() {
        Query query = query(where("rlnm").exists(true));
        query.fields().include("rlnm");

        return mongoTemplate.find(query, Press.class);
    }

    @Override
    public Press findByUrlName(String urlName, List<String> fields) {
        Query query = query(where("rlnm").is(urlName));

        if (fields != null && !fields.isEmpty()) {
            for (String field : fields) {
                query.fields().include(field);
            }
        }

        return mongoTemplate.findOne(query, Press.class);
    }

    @Override
    public Press findByUrlName(String urlName) {
        return findByUrlName(urlName, null);
    }
}
