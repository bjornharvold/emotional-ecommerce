/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import org.springframework.data.annotation.Transient;

import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 9/13/12
 * Time: 12:28 PM
 * Responsibility:
 */
public class ListCardProfile implements Serializable {
    private static final long serialVersionUID = 50098738327924294L;

    private String cd;

    @Transient
    private String fnm;

    @Transient
    private String lnm;

    @Transient
    private String mg;

    @Transient
    private String fbd;

    public ListCardProfile() {
    }

    public ListCardProfile(String fnm, String lnm, String mg, String cd, String fbd) {
        this.fnm = fnm;
        this.lnm = lnm;
        this.mg = mg;
        this.cd = cd;
        this.fbd = fbd;
    }

    public String getFnm() {
        return fnm;
    }

    public String getLnm() {
        return lnm;
    }

    public String getMg() {
        return mg;
    }

    public String getCd() {
        return cd;
    }

    public String getFbd() {
        return fbd;
    }

    public void setFbd(String fbd) {
        this.fbd = fbd;
    }

    public void setFnm(String fnm) {
        this.fnm = fnm;
    }

    public void setLnm(String lnm) {
        this.lnm = lnm;
    }

    public void setMg(String mg) {
        this.mg = mg;
    }

    public void setCd(String cd) {
        this.cd = cd;
    }
}
