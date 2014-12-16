/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import com.lela.domain.enums.AnalyticsSubType;
import com.lela.domain.enums.AnalyticsType;
import com.lela.domain.enums.AuthenticationType;
import com.lela.domain.enums.DeviceType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Chris Tallent
 * Date: 07/30/2012
 */
public class UserVisit implements Serializable {
    private static final long serialVersionUID = 2395659264492887845L;

    private ObjectId id;

    /** If this visit was by a logged in user, what auth type */
    private AuthenticationType lgntp;

    /** Referring page **/
    private String rfr;

    /** User agent */
    private String srgnt;

    /** Device Type */
    private DeviceType dvctp;

    /** Device Model as specified by wurfl */
    private String wrfld;

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

    /** First click of visit */
    private Date cdt;

    /** Last click of visit */
    private Date ldt;

    /** Number of pages visited */
    private Integer clckcnt = 0;

    public UserVisit() {
        id = new ObjectId();
        cdt = new Date();
        ldt = new Date();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public AuthenticationType getLgntp() {
        return lgntp;
    }

    public void setLgntp(AuthenticationType lgntp) {
        this.lgntp = lgntp;
    }

    public String getRfr() {
        return rfr;
    }

    public void setRfr(String rfr) {
        this.rfr = rfr;
    }

    public String getSrgnt() {
        return srgnt;
    }

    public void setSrgnt(String srgnt) {
        this.srgnt = srgnt;
    }

    public DeviceType getDvctp() {
        return dvctp;
    }

    public void setDvctp(DeviceType dvctp) {
        this.dvctp = dvctp;
    }

    public String getWrfld() {
        return wrfld;
    }

    public void setWrfld(String wrfld) {
        this.wrfld = wrfld;
    }

    public Date getCdt() {
        return cdt;
    }

    public void setCdt(Date cdt) {
        this.cdt = cdt;
    }

    public Date getLdt() {
        return ldt;
    }

    public void setLdt(Date ldt) {
        this.ldt = ldt;
    }

    public Integer getClckcnt() {
        return clckcnt;
    }

    public void setClckcnt(Integer clckcnt) {
        this.clckcnt = clckcnt;
    }

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
