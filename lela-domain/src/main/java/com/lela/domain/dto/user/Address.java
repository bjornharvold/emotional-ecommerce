/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.domain.dto.user;

import com.lela.domain.enums.AddressType;

import java.io.Serializable;

/**
 * User: Chris Tallent
 * Date: 9/27/12
 * Time: 3:54 PM
 */
public class Address implements Serializable {
    private static final long serialVersionUID = 4633724440840710651L;

    private AddressType tp = AddressType.HOME;
    private Boolean dflt = false;
    private String ln1;
    private String ln2;
    private String ct;
    private String st;
    private String zp;

    public Address() {

    }

    public Address(AddressType tp,
                   String ln1,
                   String ln2,
                   String ct,
                   String st,
                   String zp) {
        this.tp = tp;
        this.ln1 = ln1;
        this.ln2 = ln2;
        this.ct = ct;
        this.st = st;
        this.zp = zp;
    }

    public Address(AddressType tp,
                   String ct,
                   String st,
                   String zp) {
        this.tp = tp;
        this.ct = ct;
        this.st = st;
        this.zp = zp;
    }

    public AddressType getTp() {
        return tp;
    }

    public void setTp(AddressType tp) {
        this.tp = tp;
    }

    public Boolean getDflt() {
        return dflt;
    }

    public void setDflt(Boolean dflt) {
        this.dflt = dflt;
    }

    public String getLn1() {
        return ln1;
    }

    public void setLn1(String ln1) {
        this.ln1 = ln1;
    }

    public String getLn2() {
        return ln2;
    }

    public void setLn2(String ln2) {
        this.ln2 = ln2;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getZp() {
        return zp;
    }

    public void setZp(String zp) {
        this.zp = zp;
    }
}
