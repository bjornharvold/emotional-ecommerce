/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.repository.impl;

import com.lela.commons.repository.ApplicationRepositoryCustom;
import com.lela.commons.repository.DealRepositoryCustom;
import com.lela.domain.document.Application;
import com.lela.domain.document.Deal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class DealRepositoryImpl implements DealRepositoryCustom {

    private static final Logger log = LoggerFactory.getLogger(DealRepositoryImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public List<Deal> findDealsForStore(String storeUrlName) {
        Query query = query(where("rlnm").is(storeUrlName));

        return mongoTemplate.find(query, Deal.class);
    }
}
