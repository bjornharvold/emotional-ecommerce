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
 * Date: 12/12/12
 * Time: 9:16 AM
 */
public class AffiliateAccountStore implements Serializable {
    private static final long serialVersionUID = 7533130482482704480L;

    /** Store name */
    private String nm;

    /** Store Url Name */
    @NotNull
    @NotEmpty
    private String rlnm;

    /** Create date */
    private Date dt;

    /** Owning affiliate account */
    @Transient
    private String affiliateAccountUrlName;

    public AffiliateAccountStore() {
    }

    public AffiliateAccountStore(String affiliateAccountUrlName) {
        this.affiliateAccountUrlName = affiliateAccountUrlName;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
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

    public String getAffiliateAccountUrlName() {
        return affiliateAccountUrlName;
    }

    public void setAffiliateAccountUrlName(String affiliateAccountUrlName) {
        this.affiliateAccountUrlName = affiliateAccountUrlName;
    }
}
