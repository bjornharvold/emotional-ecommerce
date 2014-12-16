/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.compute.concurrent.motivator.untyped;

import com.lela.compute.dto.compute.MotivatorMatch;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 2/7/12
 * Time: 3:51 AM
 * Responsibility:
 */
public class MotivatorPayload {
    private final MotivatorMatch subject;
    private final List<MotivatorMatch> products;

    public MotivatorPayload(MotivatorMatch subject, List<MotivatorMatch> products) {
        this.subject = subject;
        this.products = products;
    }

    public MotivatorMatch getSubject() {
        return subject;
    }

    public List<MotivatorMatch> getProducts() {
        return products;
    }
}
