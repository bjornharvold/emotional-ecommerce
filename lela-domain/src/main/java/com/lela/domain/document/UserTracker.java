/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import com.lela.domain.enums.InteractionType;
import com.lela.domain.enums.RegistrationType;
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
@Document
public class UserTracker extends AbstractDocument implements Serializable {
    private static final long serialVersionUID = 2395659264492887845L;

    /**
     * User "cd" of the user INCLUDING transient users
     */
    @Indexed
    private String srcd;

    /**
     * Registered USER ID
     */
    @Indexed
    private ObjectId srid;

    /**
     * Date registration started
     */
    private Date rgstrtdt;

    /**
     * Date registration completed
     */
    private Date rgcmpltdt;

    /**
     * Initial Registration authentication type
     */
    private RegistrationType rgtp;

    /**
     * Date quiz viewed
     */
    private Date qzvwd;

    /**
     * Date quiz started/interacted
     */
    private Date qzstrtdt;

    /**
     * Date quiz completed
     */
    private Date qzcmpltdt;

    /**
     * Which quiz was taken
     */
    private String qzrlnm;

    /**
     * Which affiliate application was used, if any
     */
    private String qzpprlnm;

    /**
     * Which affiliate quiz was completed
     */
    private String qzffltrlnm;

    /**
     * Quiz interaction type
     */
    private InteractionType qztp;

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

    /**
     * Individual times the user has visited the site (user sessions)
     */
    private List<UserVisit> vsts = new ArrayList<UserVisit>();

    /**
     * Count of visits since Mongo DB operations suck at counting
     */
    private Integer vstcnt = 0;

    /**
     * Redirect links (e.g. - Purchase links) clicked by user
     */
    private List<Redirect> rdrcts = new ArrayList<Redirect>();

    /**
     * Count of redirects
     */
    private Integer rdrctcnt = 0;

    public void addVisit(UserVisit visit) {
        vsts.add(visit);
        vstcnt++;
    }

    public String getSrcd() {
        return srcd;
    }

    public void setSrcd(String srcd) {
        this.srcd = srcd;
    }

    public ObjectId getSrid() {
        return srid;
    }

    public void setSrid(ObjectId srid) {
        this.srid = srid;
    }

    public Date getRgstrtdt() {
        return rgstrtdt;
    }

    public void setRgstrtdt(Date rgstrtdt) {
        this.rgstrtdt = rgstrtdt;
    }

    public Date getRgcmpltdt() {
        return rgcmpltdt;
    }

    public void setRgcmpltdt(Date rgcmpltdt) {
        this.rgcmpltdt = rgcmpltdt;
    }

    public RegistrationType getRgtp() {
        return rgtp;
    }

    public void setRgtp(RegistrationType rgtp) {
        this.rgtp = rgtp;
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

    public List<UserVisit> getVsts() {
        return vsts;
    }

    public void setVsts(List<UserVisit> vsts) {
        this.vsts = vsts;
    }

    public Integer getVstcnt() {
        return vstcnt;
    }

    public void setVstcnt(Integer vstcnt) {
        this.vstcnt = vstcnt;
    }

    public Date getQzvwd() {
        return qzvwd;
    }

    public void setQzvwd(Date qzvwd) {
        this.qzvwd = qzvwd;
    }

    public Date getQzstrtdt() {
        return qzstrtdt;
    }

    public void setQzstrtdt(Date qzstrtdt) {
        this.qzstrtdt = qzstrtdt;
    }

    public Date getQzcmpltdt() {
        return qzcmpltdt;
    }

    public void setQzcmpltdt(Date qzcmpltdt) {
        this.qzcmpltdt = qzcmpltdt;
    }

    public String getQzrlnm() {
        return qzrlnm;
    }

    public void setQzrlnm(String qzrlnm) {
        this.qzrlnm = qzrlnm;
    }

    public String getQzpprlnm() {
        return qzpprlnm;
    }

    public void setQzpprlnm(String qzpprlnm) {
        this.qzpprlnm = qzpprlnm;
    }

    public InteractionType getQztp() {
        return qztp;
    }

    public void setQztp(InteractionType qztp) {
        this.qztp = qztp;
    }

    public String getQzffltrlnm() {
        return qzffltrlnm;
    }

    public void setQzffltrlnm(String qzffltrlnm) {
        this.qzffltrlnm = qzffltrlnm;
    }

    public List<Redirect> getRdrcts() {
        return rdrcts;
    }

    public void setRdrcts(List<Redirect> rdrcts) {
        this.rdrcts = rdrcts;
    }

    public Integer getRdrctcnt() {
        return rdrctcnt;
    }

    public void setRdrctcnt(Integer rdrctcnt) {
        this.rdrctcnt = rdrctcnt;
    }
}
