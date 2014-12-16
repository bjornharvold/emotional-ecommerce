/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 9/2/11
 * Time: 2:35 PM
 * Responsibility:
 */
public class MetricValue implements Serializable {

    private static final long serialVersionUID = 61923179260695697L;

    private String ky;
    private Integer rdr;

    public MetricValue() {
    }

    public MetricValue(String ky, Integer rdr) {
        this.ky = ky;
        this.rdr = rdr;
    }

    public String getKy() {
        return ky;
    }

    public void setKy(String ky) {
        this.ky = ky;
    }

    public Integer getRdr() {
        return rdr;
    }

    public void setRdr(Integer rdr) {
        this.rdr = rdr;
    }
}
