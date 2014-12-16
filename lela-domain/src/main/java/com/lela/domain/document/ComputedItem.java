/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 10/7/11
 * Time: 10:07 PM
 * Responsibility:
 */
public class ComputedItem implements Serializable {
    private static final long serialVersionUID = 8794491738462139210L;

    /** Motivator Relevancy */
    private Map<String, Integer> mtvtrrlvncy;

    /** Total Relevancy */
    private Integer ttlrlvncy;

    /** Total Relevancy number */
    private Integer ttlrlvncynmbr;

    /** Item url name */
    private String rlnm;

    public ComputedItem() {
    }

    public ComputedItem(String rlnm, Map<String, Integer> mtvtrrlvncy, Integer ttlrlvncy, Integer ttlrlvncynmbr) {
        this.rlnm = rlnm;
        this.mtvtrrlvncy = mtvtrrlvncy;
        this.ttlrlvncy = ttlrlvncy;
        this.ttlrlvncynmbr = ttlrlvncynmbr;
    }

    public Map<String, Integer> getMtvtrrlvncy() {
        return mtvtrrlvncy;
    }

    public void setMtvtrrlvncy(Map<String, Integer> mtvtrrlvncy) {
        this.mtvtrrlvncy = mtvtrrlvncy;
    }

    public Integer getTtlrlvncy() {
        return ttlrlvncy;
    }

    public void setTtlrlvncy(Integer ttlrlvncy) {
        this.ttlrlvncy = ttlrlvncy;
    }

    public Integer getTtlrlvncynmbr() {
        return ttlrlvncynmbr;
    }

    public void setTtlrlvncynmbr(Integer ttlrlvncynmbr) {
        this.ttlrlvncynmbr = ttlrlvncynmbr;
    }

    public String getRlnm() {
        return rlnm;
    }

    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }
}
