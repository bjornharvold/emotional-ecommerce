/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.domain.document;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * User: Chris Tallent
 * Date: 12/10/12
 * Time: 3:05 PM
 */
public class AffiliateAccountCssStyle implements Serializable {
    private static final long serialVersionUID = 6432175866448657662L;

    /** Application url name */
    @NotNull
    @NotEmpty
    private String rlnm;

    /** Creation date */
    private Date dt;

    /** CSS Style value */
    @NotNull
    @NotEmpty
    private String vl;

    @Transient
    private String affiliateAccountUrlName;

    public AffiliateAccountCssStyle() {
    }

    public AffiliateAccountCssStyle(String affiliateAccountUrlName) {
        this.affiliateAccountUrlName = affiliateAccountUrlName;
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

    public String getVl() {
        return vl;
    }

    public void setVl(String vl) {
        this.vl = vl;
    }

    public String getAffiliateAccountUrlName() {
        return affiliateAccountUrlName;
    }

    public void setAffiliateAccountUrlName(String affiliateAccountUrlName) {
        this.affiliateAccountUrlName = affiliateAccountUrlName;
    }
}
