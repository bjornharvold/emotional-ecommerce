/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Chris Tallent
 * Date: 11/10/11
 * Time: 2:49 PM
 */
@Document
public class PostalCode extends AbstractDocument implements Serializable {
    /**
     * Postal code
     */
    @Indexed(unique = true)
    private String cd;

    /**
     * Population
     */
    private Integer ppltn;

    /**
     * Location array of (Longitude, Latitude)
     */
    private Float[] lc;

    /**
     * State
     */
    private String st;

    /**
     * State Name
     */
    private String stnm;

    /**
     * City
     */
    private String ct;

    /* -- METHODS ---------------------------------------------------------------- */

    public Float getLongitude() {
        if (lc != null && lc.length > 0) {
            return lc[0];
        }

        return null;
    }

    public Float getLatitude() {
        if (lc != null && lc.length > 1) {
            return lc[1];
        }

        return null;
    }

    /* -- GET METHODS ---------------------------------------------------------------- */

    public String getCd() {
        return cd;
    }

    public Integer getPpltn() {
        return ppltn;
    }

    public Float[] getLc() {
        return lc;
    }

    public String getSt() {
        return st;
    }

    public String getStnm() {
        return stnm;
    }

    public String getCt() {
        return ct;
    }

    /* -- SET METHODS ---------------------------------------------------------------- */

    public void setCd(String cd) {
        this.cd = cd;
    }

    public void setPpltn(Integer ppltn) {
        this.ppltn = ppltn;
    }

    public void setLc(Float[] lc) {
        this.lc = lc;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public void setStnm(String stnm) {
        this.stnm = stnm;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }
}
