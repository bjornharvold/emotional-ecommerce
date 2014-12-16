/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Bjorn Harvold
 * Date: 12/9/11
 * Time: 12:57 AM
 * Responsibility:
 */
public class UserEvent implements Serializable {
    private static final long serialVersionUID = 3414309890009514137L;
    
    /** Url name */
    private String rlnm;

    /** Sign up date */
    private Date dt;

    /**
     * (Optional) Affiliate account of campaign introducing the user
     */
    private String ffltccntrlnm;

    /**
     * (Optional) Affiliate account of campaign introducing the user
     */
    private String cmpgnrlnm;

    private Map<String, Object> attrs = new ConcurrentHashMap<String, Object>();

    public UserEvent() {
    }

    public UserEvent(String rlnm, Date dt) {
        this.rlnm = rlnm;
        this.dt = dt;
    }

    public UserEvent(String rlnm, Date dt, Map attrs)
    {
        this.rlnm = rlnm;
        this.dt = dt;
        this.attrs = attrs;
    }

    public String getRlnm() {
        return rlnm;
    }

    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public Map<String, Object> getAttrs()
    {
        return attrs;
    }

    public void setAttrs(Map<String, Object> attrs)
    {
        this.attrs = attrs;
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
}
