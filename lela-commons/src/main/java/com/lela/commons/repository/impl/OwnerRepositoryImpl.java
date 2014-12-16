/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.repository.impl;

import com.lela.commons.repository.OwnerRepositoryCustom;
import com.lela.commons.repository.StoreRepositoryCustom;
import com.lela.domain.document.Owner;
import com.lela.domain.document.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by IntelliJ IDEA.
 * User: Chris Tallent
 * Date: 11/10/11
 * Time: 9:01 AM
 */
public class OwnerRepositoryImpl implements OwnerRepositoryCustom {

    private static final Logger log = LoggerFactory.getLogger(OwnerRepositoryImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Owner> findOwners(List<String> fields) {
        Query query = query(where("id").exists(true));
        
        if (fields != null && !fields.isEmpty()) {
            for (String field : fields) {
                query.fields().include(field);
            }
        }
        
        return mongoTemplate.find(query, Owner.class);
    }
}
