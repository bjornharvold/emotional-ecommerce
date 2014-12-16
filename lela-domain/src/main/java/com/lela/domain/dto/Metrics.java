/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import com.lela.domain.document.Metric;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 7/28/11
 * Time: 12:37 PM
 * Responsibility:
 */
public class Metrics extends AbstractJSONPayload {
    private List<Metric> list;

    public Metrics() {
    }

    public Metrics(List<Metric> list) {
        this.list = list;
    }

    public List<Metric> getList() {
        return list;
    }

    public void setList(List<Metric> list) {
        this.list = list;
    }
}
