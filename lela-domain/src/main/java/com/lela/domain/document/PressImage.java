/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Bjorn Harvold
 * Date: 6/8/12
 * Time: 4:56 PM
 * Responsibility:
 */
public class PressImage implements Serializable {
    private static final long serialVersionUID = -2636982159260705612L;

    /** Date this press article was written */
    private Date dt;

    /** Publish date */
    @NotNull
    private Date pblshdt;

    /** Name of the publisher */
    @NotNull
    @NotEmpty
    private String pblshr;

    /** Article text */
    @NotNull
    @NotEmpty
    private String txt;

    /** Header title */
    @NotNull
    @NotEmpty
    private String hdr;

    /** URL to external article */
    @NotNull
    @NotEmpty
    private String rl;

    /** Image url */
    private String mgrl;

    private String mgid;

    /** Order by this field */
    @NotNull
    @Min(1)
    private Integer rdr;

    public PressImage() {}

    public PressImage(PressImage pressImage) {
        this.mgid = pressImage.getMgid();
        this.dt = pressImage.getDt();
        this.hdr = pressImage.getHdr();
        this.mgrl = pressImage.getMgrl();
        this.pblshdt = pressImage.getPblshdt();
        this.pblshr = pressImage.getPblshr();
        this.rdr = pressImage.getRdr();
        this.rl = pressImage.getRl();
        this.txt = pressImage.getTxt();
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public Date getPblshdt() {
        return pblshdt;
    }

    public void setPblshdt(Date pblshdt) {
        this.pblshdt = pblshdt;
    }

    public String getPblshr() {
        return pblshr;
    }

    public void setPblshr(String pblshr) {
        this.pblshr = pblshr;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getHdr() {
        return hdr;
    }

    public void setHdr(String hdr) {
        this.hdr = hdr;
    }

    public String getRl() {
        return rl;
    }

    public void setRl(String rl) {
        this.rl = rl;
    }

    public String getMgrl() {
        return mgrl;
    }

    public void setMgrl(String mgrl) {
        this.mgrl = mgrl;
    }

    public Integer getRdr() {
        return rdr;
    }

    public void setRdr(Integer rdr) {
        this.rdr = rdr;
    }

    public String getMgid() {
        return mgid;
    }

    public void setMgid(String mgid) {
        this.mgid = mgid;
    }
}
