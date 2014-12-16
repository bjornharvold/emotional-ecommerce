/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.repository.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.lela.commons.repository.NavigationBarRepositoryCustom;
import com.lela.domain.document.NavigationBar;

/**
 * Custom user repository
 */
public class NavigationBarRepositoryImpl implements NavigationBarRepositoryCustom {
    private final static Logger log = LoggerFactory.getLogger(NavigationBarRepositoryImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void setDefaultFlagOnCollection(Locale locale, Boolean isDefault) {
        Query query = query(where("dflt").is(true).and("lcl").is(locale));
        Update update = update("dflt", false);

        mongoTemplate.updateMulti(query, update, NavigationBar.class);
    }
    
    @Override
    public List<NavigationBar> findNavigationBars(List<String> fields) {
        Query query = query(where("id").exists(true));
        
        if (fields != null && !fields.isEmpty()) {
            for (String field : fields) {
                query.fields().include(field);
            }
        }
        
        return mongoTemplate.find(query, NavigationBar.class);
    }

    @Override
    public boolean departmentExists(String departmentUrlName) {
        long result = 0;
        Query query = query(where("grps.rlnm").is(departmentUrlName));
        result = mongoTemplate.count(query, NavigationBar.class);

        return result > 0;
    }
}
