/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import com.lela.domain.enums.ClaimStatus;

import java.io.Serializable;
import java.util.Date;

public class Reward implements Serializable {

    private static final long serialVersionUID = -7534015313841767328L;

    /** Product Name */
    private String prdctnm;

    /** Purchase Date */
    private Date prchsdt;

    /** Sale Price */
    private Double slprc;

    /** Reward Amount */
    private Double rwrdamnt;

    /** Claim Date */
    private Date clmdt;

    /** Claim Status */
    private ClaimStatus clmstts;

    public Reward(String prdctnm, Date prchsdt, Double slprc, Double rwrdamnt, Date clmdt, ClaimStatus clmstts) {
        this.prdctnm = prdctnm;
        this.prchsdt = prchsdt;
        this.slprc = slprc;
        this.rwrdamnt = rwrdamnt;
        this.clmdt = clmdt;
        this.clmstts = clmstts;
    }

    public Reward() {
    }

    public String getPrdctnm() {
        return prdctnm;
    }

    public void setPrdctnm(String prdctnm) {
        this.prdctnm = prdctnm;
    }

    public Date getPrchsdt() {
        return prchsdt;
    }

    public void setPrchsdt(Date prchsdt) {
        this.prchsdt = prchsdt;
    }

    public Double getSlprc() {
        return slprc;
    }

    public void setSlprc(Double slprc) {
        this.slprc = slprc;
    }

    public Double getRwrdamnt() {
        return rwrdamnt;
    }

    public void setRwrdamnt(Double rwrdamnt) {
        this.rwrdamnt = rwrdamnt;
    }

    public Date getClmdt() {
        return clmdt;
    }

    public void setClmdt(Date clmdt) {
        this.clmdt = clmdt;
    }

    public ClaimStatus getClmstts() {
        return clmstts;
    }

    public void setClmstts(ClaimStatus clmstts) {
        this.clmstts = clmstts;
    }
}
