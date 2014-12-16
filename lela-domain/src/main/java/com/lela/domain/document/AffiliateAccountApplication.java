/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import com.lela.domain.enums.ApplicationType;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Bjorn Harvold
 * Date: 7/10/12
 * Time: 3:14 PM
 * Responsibility:
 */
public class AffiliateAccountApplication implements Serializable {
    private static final long serialVersionUID = -7562662008097232479L;

    /** Application url name */
    @NotNull
    @NotEmpty
    private String rlnm;

    /** Creation date */
    private Date dt;

    /** Application type. This is just to more easily gauge
        what type of app this is without loading up the application */
    private ApplicationType tp;

    @Transient
    private String affiliateAccountUrlName;

    public AffiliateAccountApplication() {
    }

    public AffiliateAccountApplication(String affiliateAccountUrlName) {
        this.affiliateAccountUrlName = affiliateAccountUrlName;
    }

    public String getRlnm() {
        return rlnm;
    }

    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    public String getAffiliateAccountUrlName() {
        return affiliateAccountUrlName;
    }

    public void setAffiliateAccountUrlName(String affiliateAccountUrlName) {
        this.affiliateAccountUrlName = affiliateAccountUrlName;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public ApplicationType getTp() {
        return tp;
    }

    public void setTp(ApplicationType tp) {
        this.tp = tp;
    }
}
