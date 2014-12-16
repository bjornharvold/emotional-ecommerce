/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.commons.repository.impl;

import com.lela.commons.repository.FacebookSnapshotRepositoryCustom;
import com.lela.domain.document.FacebookSnapshot;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

/**
 * User: Chris Tallent
 * Date: 8/29/12
 * Time: 9:00 PM
 */
public class FacebookSnapshotRepositoryImpl implements FacebookSnapshotRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void flagAsMotivatorsDone(ObjectId id) {
        Query query = query(where("id").is(id));
        Update update = update("motivatorsDone", true);

        mongoTemplate.updateFirst(query, update, FacebookSnapshot.class);
    }

    @Override
    public void removeByLelaEmail(String lelaEmail) {
        Query query = query(where("lelaEmail").is(lelaEmail));
        mongoTemplate.remove(query, FacebookSnapshot.class);
    }
}
