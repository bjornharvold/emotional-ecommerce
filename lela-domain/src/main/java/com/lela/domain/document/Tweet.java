/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Bjorn Harvold
 * Date: 6/8/12
 * Time: 5:02 PM
 * Responsibility:
 */
public class Tweet implements Serializable {
    private static final long serialVersionUID = -1943242188627916577L;

    /** Publish date */
    @NotNull
    private Date pblshdt;

    /** Tweet date */
    @NotNull
    private Date dt;

    /** Creation date */
    private Date cdt;

    /** Image url */
    @NotNull
    @NotEmpty
    private String mgrl;

    /** Twitter user name */
    @NotNull
    @NotEmpty
    private String nm;

    /** Twitter */
    @NotNull
    @NotEmpty
    private String hndl;

    /** Tweet url */
    @NotNull
    @NotEmpty
    private String rl;

    /** Tweet id */
    private String tid;

    /** Tweet */
    @NotNull
    @NotEmpty
    private String txt;

    /** Order */
    @Min(1)
    @NotNull
    private Integer rdr;

    @Transient
    @NotNull
    @NotEmpty
    private String rlnm;

    public Tweet() {
    }

    public Tweet(String rlnm) {
        this.rlnm = rlnm;
    }

    public Tweet(Tweet tweet) {
        this.nm = tweet.getNm();
        this.rlnm = tweet.getRlnm();
        this.rdr = tweet.getRdr();
        this.txt = tweet.getTxt();
        this.rl = tweet.getRl();
        this.tid = tweet.getTid();
        this.hndl = tweet.getHndl();
        this.mgrl = tweet.getMgrl();
        this.cdt = tweet.getCdt();
        this.dt = tweet.getDt();
        this.pblshdt = tweet.getPblshdt();
    }

    public Date getPblshdt() {
        return pblshdt;
    }

    public void setPblshdt(Date pblshdt) {
        this.pblshdt = pblshdt;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public String getMgrl() {
        return mgrl;
    }

    public void setMgrl(String mgrl) {
        this.mgrl = mgrl;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getHndl() {
        return hndl;
    }

    public void setHndl(String hndl) {
        this.hndl = hndl;
    }

    public String getRl() {
        return rl;
    }

    public void setRl(String rl) {
        this.rl = rl;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getRlnm() {
        return rlnm;
    }

    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    public Date getCdt() {
        return cdt;
    }

    public void setCdt(Date cdt) {
        this.cdt = cdt;
    }

    public Integer getRdr() {
        return rdr;
    }

    public void setRdr(Integer rdr) {
        this.rdr = rdr;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }
}
