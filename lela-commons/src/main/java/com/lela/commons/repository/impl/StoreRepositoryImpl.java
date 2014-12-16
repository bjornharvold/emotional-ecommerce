/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.repository.impl;

import com.lela.commons.repository.BranchRepositoryCustom;
import com.lela.commons.repository.StoreRepositoryCustom;
import com.lela.domain.document.Branch;
import com.lela.domain.document.Store;
import com.lela.domain.dto.BranchSearchResult;
import com.lela.domain.dto.store.BranchDistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoResult;
import org.springframework.data.mongodb.core.geo.GeoResults;
import org.springframework.data.mongodb.core.geo.Metrics;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by IntelliJ IDEA.
 * User: Chris Tallent
 * Date: 11/10/11
 * Time: 9:01 AM
 */
public class StoreRepositoryImpl implements StoreRepositoryCustom {

    private static final Logger log = LoggerFactory.getLogger(StoreRepositoryImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Store> findStoreUrlNamesByMerchantIds(List<String> merchantIds) {
        Query query = query(where("mrchntd").in(merchantIds));

        return mongoTemplate.find(query, Store.class);
    }

    @Override
    public List<Store> findStores(List<String> fields) {
        Query query = query(where("id").exists(true));
        query.sort().on("nm", Order.ASCENDING);

        if (fields != null && !fields.isEmpty()) {
            for (String field : fields) {
                query.fields().include(field);
            }
        }

        return mongoTemplate.find(query, Store.class);
    }
}
