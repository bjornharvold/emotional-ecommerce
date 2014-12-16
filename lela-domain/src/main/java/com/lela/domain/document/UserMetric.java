/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import com.lela.domain.enums.MetricType;

import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 9/2/11
 * Time: 2:44 PM
 * Responsibility:
 */
public class UserMetric implements Serializable {
    private static final long serialVersionUID = -6706004603658992330L;
    private String vl;
    private MetricType tp;

    public UserMetric() {
    }

    public UserMetric(MetricType tp, String vl) {
        this.tp = tp;
        this.vl = vl;
    }

    public String getVl() {
        return vl;
    }

    public void setVl(String vl) {
        this.vl = vl;
    }

    public MetricType getTp() {
        return tp;
    }

    public void setTp(MetricType tp) {
        this.tp = tp;
    }
}
