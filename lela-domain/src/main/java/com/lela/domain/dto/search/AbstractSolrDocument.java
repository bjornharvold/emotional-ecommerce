/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.search;

import org.apache.solr.client.solrj.beans.Field;

import java.util.Date;

/**
 * Created by Bjorn Harvold
 * Date: 2/15/12
 * Time: 11:24 PM
 * Responsibility:
 */
public abstract class AbstractSolrDocument {

    private String id;
    private Date ldt;
    private Float score;

    public String getId() {
        return id;
    }

    @Field
    public void setId(String id) {
        this.id = id;
    }

    public Date getLdt() {
        return ldt;
    }

    @Field
    public void setLdt(Date ldt) {
        this.ldt = ldt;
    }

    public Float getScore() {
        return score;
    }

    @Field
    public void setScore(Float score) {
        this.score = score;
    }
}
