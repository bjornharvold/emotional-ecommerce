/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.repository.impl;

import com.lela.commons.repository.ComputedCategoryRepositoryCustom;
import com.lela.domain.document.ComputedCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

public class ComputedCategoryRepositoryImpl implements ComputedCategoryRepositoryCustom {

    private static final Logger log = LoggerFactory.getLogger(ComputedCategoryRepositoryImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public void setAllComputedCategoriesToDirty() {
        Query query = query(where("id").exists(true));
        Update update = update("drty", true);
        mongoTemplate.updateMulti(query, update, ComputedCategory.class);
    }

    @Override
    public void setAllComputedCategoryToDirty(String categoryUrlName) {
        Query query = query(where("rlnm").is(categoryUrlName));
        Update update = update("drty", true);
        mongoTemplate.updateMulti(query, update, ComputedCategory.class);
    }
}
