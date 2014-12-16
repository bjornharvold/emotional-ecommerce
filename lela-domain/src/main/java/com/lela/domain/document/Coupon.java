package com.lela.domain.document;

import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Martin Gamboa
 * Date: 12/2/11
 * Time: 7:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class Coupon implements Serializable {

    private static final long serialVersionUID = -7628829647050745885L;

    /** Coupon Id */
    private ObjectId cpnd;

    /* Offer Url Name */
    private String ffrrlnm;

    /** Item Url Name */
    private String tmrlnm;

    /** Coupon Code */
    private String cpncd;

    /** First Name */
    private String fnm;

    /** Last Name */
    private String lnm;

    /** Redemption Date */
    private Date rdmptndt;

    /** Created Date */
    private Date crtddt;

    /** Transaction Amount */
    private Double trnsctnmnt;

    /** Customer Discount Amount */
    private Double cstmrdscntmnt;

    /** Lela Commission Amount */
    private Double cmmssnmnt;

    /** Lela Commission Payment Status */
    private Double cmmssnpymntstts;

    public ObjectId getCpnd() {
        return cpnd;
    }

    public void setCpnd(ObjectId cpnd) {
        this.cpnd = cpnd;
    }

    public String getTmrlnm() {
        return tmrlnm;
    }

    public void setTmrlnm(String tmd) {
        this.tmrlnm = tmd;
    }

    public String getCpncd() {
        return cpncd;
    }

    public void setCpncd(String cpncd) {
        this.cpncd = cpncd;
    }

    public String getFnm() {
        return fnm;
    }

    public void setFnm(String fnm) {
        this.fnm = fnm;
    }

    public String getLnm() {
        return lnm;
    }

    public void setLnm(String lnm) {
        this.lnm = lnm;
    }

    public Date getRdmptndt() {
        return rdmptndt;
    }

    public void setRdmptndt(Date rdmptndt) {
        this.rdmptndt = rdmptndt;
    }

    public Date getCrtddt() {
        return crtddt;
    }

    public void setCrtddt(Date crtddt) {
        this.crtddt = crtddt;
    }

    public String getFfrrlnm() {
        return ffrrlnm;
    }

    public void setFfrrlnm(String ffrrlnm) {
        this.ffrrlnm = ffrrlnm;
    }

    public Double getTrnsctnmnt() {
        return trnsctnmnt;
    }

    public void setTrnsctnmnt(Double trnsctnmnt) {
        this.trnsctnmnt = trnsctnmnt;
    }

    public Double getCstmrdscntmnt() {
        return cstmrdscntmnt;
    }

    public void setCstmrdscntmnt(Double cstmrdscntmnt) {
        this.cstmrdscntmnt = cstmrdscntmnt;
    }

    public Double getCmmssnmnt() {
        return cmmssnmnt;
    }

    public void setCmmssnmnt(Double cmmssnmnt) {
        this.cmmssnmnt = cmmssnmnt;
    }

    public Double getCmmssnpymntstts() {
        return cmmssnpymntstts;
    }

    public void setCmmssnpymntstts(Double cmmssnpymntstts) {
        this.cmmssnpymntstts = cmmssnpymntstts;
    }
}
