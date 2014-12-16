/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.compute.dto.compute;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 2/4/12
 * Time: 12:09 AM
 * Responsibility:
 */
public class MotivatorMatchQuery {
    private List<MotivatorMatch> subjects;
    private List<MotivatorMatch> products;

    public List<MotivatorMatch> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<MotivatorMatch> subjects) {
        this.subjects = subjects;
    }

    public List<MotivatorMatch> getProducts() {
        return products;
    }

    public void setProducts(List<MotivatorMatch> products) {
        this.products = products;
    }
}
