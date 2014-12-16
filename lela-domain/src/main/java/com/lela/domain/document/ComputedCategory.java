/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 10/7/11
 * Time: 10:02 PM
 * Responsibility:
 */
@Document
public class ComputedCategory extends AbstractDocument implements Serializable {
    private static final long serialVersionUID = -6610314021655119437L;

    // Item max score for that category
    private Integer mxscr;

    /** Max score item url name */
    private String mxscrtmrlnm;

    /** Category url name */
    private String rlnm;

    /** Dirty flag which means this data needs to be updated */
    private Boolean drty;

    /** Date this category was computed */
    private Date dt;

    /** User id */
    private String cd;

    public ComputedCategory() {
    }

    public ComputedCategory(String userCode, String categoryUrlName) {
        this.rlnm = categoryUrlName;

        this.cd = userCode;

        // when we instantiate a new instance we set the dirty flag to false
        this.drty = false;

        // also set the time this category was created
        this.dt = new Date();
    }

    private List<ComputedItem> tms = new ArrayList<ComputedItem>();

    public Integer getMxscr() {
        return mxscr;
    }

    public void setMxscr(Integer mxscr) {
        this.mxscr = mxscr;
    }

    public List<ComputedItem> getTms() {
        return tms;
    }

    public void setTms(List<ComputedItem> tms) {
        this.tms = tms;
    }

    public String getMxscrtmrlnm() {
        return mxscrtmrlnm;
    }

    public void setMxscrtmrlnm(String mxscrtmrlnm) {
        this.mxscrtmrlnm = mxscrtmrlnm;
    }

    public String getRlnm() {
        return rlnm;
    }

    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    public ComputedItem getComputedItem(String itemUrlName) {

        if (tms != null && !tms.isEmpty()) {
            for (ComputedItem ci : tms) {
                if (StringUtils.equals(ci.getRlnm(), itemUrlName)) {
                    return ci;
                }
            }
        }

        return null;
    }

    public Boolean getDrty() {
        return drty;
    }

    public void setDrty(Boolean drty) {
        this.drty = drty;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public String getCd() {
        return cd;
    }

    public void setCd(String cd) {
        this.cd = cd;
    }
}
