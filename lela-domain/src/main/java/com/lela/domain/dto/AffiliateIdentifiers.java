/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.domain.dto;

/**
 * User: Chris Tallent
 * Date: 9/20/12
 * Time: 1:33 PM
 */
public class AffiliateIdentifiers {
    /**
     * Domain name that the user registered with
     */
    private String dmn;

    /**
     * Domain affiliate that the user registered with, if any
     */
    private String dmnffltrlnm;

    /**
     * Affiliate account of campaign introducing the user
     */
    private String ffltccntrlnm;

    /**
     * Affiliate account of campaign introducing the user
     */
    private String cmpgnrlnm;

    /**
     * Affiliate account of referring affiliate introducing the user
     * which may be different from the account that owns the campaign
     */
    private String rfrrlnm;

    public String getDmn() {
        return dmn;
    }

    public void setDmn(String dmn) {
        this.dmn = dmn;
    }

    public String getDmnffltrlnm() {
        return dmnffltrlnm;
    }

    public void setDmnffltrlnm(String dmnffltrlnm) {
        this.dmnffltrlnm = dmnffltrlnm;
    }

    public String getFfltccntrlnm() {
        return ffltccntrlnm;
    }

    public void setFfltccntrlnm(String ffltccntrlnm) {
        this.ffltccntrlnm = ffltccntrlnm;
    }

    public String getCmpgnrlnm() {
        return cmpgnrlnm;
    }

    public void setCmpgnrlnm(String cmpgnrlnm) {
        this.cmpgnrlnm = cmpgnrlnm;
    }

    public String getRfrrlnm() {
        return rfrrlnm;
    }

    public void setRfrrlnm(String rfrrlnm) {
        this.rfrrlnm = rfrrlnm;
    }
}
