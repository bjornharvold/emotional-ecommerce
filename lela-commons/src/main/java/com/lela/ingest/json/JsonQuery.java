/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.ingest.json;

import org.springframework.beans.factory.BeanNameAware;

/**
 * User: Chris Tallent
 * Date: 4/21/12
 * Time: 7:09 PM
 */
public class JsonQuery implements BeanNameAware {
    private String name;
    private boolean cache = false;
    private String sql;
    private String[] groupBy;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String[] getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String[] groupBy) {
        this.groupBy = groupBy;
    }

    @Override
    public void setBeanName(String name) {
        if (name == null) {
            this.name = name;
        }
    }
}
