/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.commons.spring.data;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * User: Chris Tallent
 * Date: 6/15/12
 * Time: 9:24 AM
 */
public class FieldBasicQuery extends Query {
    private final DBObject queryObject;
    private DBObject sortObject;

    public FieldBasicQuery(String query) {
        this.queryObject = (DBObject) JSON.parse(query);
    }

    @Override
    public Query addCriteria(Criteria criteria) {
        this.queryObject.putAll(criteria.getCriteriaObject());
        return this;
    }

    @Override
    public DBObject getQueryObject() {
        return this.queryObject;
    }

    @Override
    public DBObject getSortObject() {

        BasicDBObject result = new BasicDBObject();
        if (sortObject != null) {
            result.putAll(sortObject);
        }

        DBObject overrides = super.getSortObject();
        if (overrides != null) {
            result.putAll(overrides);
        }

        return result;
    }

    public void setSortObject(DBObject sortObject) {
        this.sortObject = sortObject;
    }
}
