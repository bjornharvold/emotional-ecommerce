/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 7/18/11
 * Time: 11:58 AM
 * Responsibility:
 */
@Document
public class Foo extends AbstractDocument {
    private ObjectId one;

    private String two;

    private List<FunctionalFilter> tnrs;
    private List<Bar> bars = new ArrayList<Bar>();

    public ObjectId getOne() {
        return one;
    }

    public void setOne(ObjectId one) {
        this.one = one;
    }

    public String getTwo() {
        return two;
    }

    public void setTwo(String two) {
        this.two = two;
    }

    public List<Bar> getBars() {
        return bars;
    }

    public void setBars(List<Bar> bars) {
        this.bars = bars;
    }

    public List<FunctionalFilter> getTnrs() {
        return tnrs;
    }

    public void setTnrs(List<FunctionalFilter> tnrs) {
        this.tnrs = tnrs;
    }
}
