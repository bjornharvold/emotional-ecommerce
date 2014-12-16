/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.domain.dto.quiz;

import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.Application;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.AbstractJSONPayload;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * User: Chris Tallent
 * Date: 10/4/12
 * Time: 7:23 AM
 */
public class QuizStaticContentRequest extends AbstractJSONPayload implements Serializable {
    private static final long serialVersionUID = 5126078408496638501L;

    /**
     * Affiliate ID
     */
    @NotNull
    @NotEmpty
    private String affiliateId;

    /**
     * Application Id
     */
    @NotNull
    @NotEmpty
    private String applicationId;

    /**
     * Static content rlnm
     */
    @NotNull
    @NotEmpty
    private String rlnm;

    /**
     * Is this a return visit to a completed quiz
     */
    private boolean returned;

    /**
     * Populated by service
     */
    private AffiliateAccount affiliate;

    /**
     * Populated by service
     */
    private Application application;

    /**
     * Populated by service
     */
    private User user;

    /**
     * Populated by service
     */
    private UserSupplement us;

    public String getRlnm() {
        return rlnm;
    }

    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public String getAffiliateId() {
        return affiliateId;
    }

    public void setAffiliateId(String affiliateId) {
        this.affiliateId = affiliateId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public AffiliateAccount getAffiliate() {
        return affiliate;
    }

    public void setAffiliate(AffiliateAccount affiliate) {
        this.affiliate = affiliate;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserSupplement getUs() {
        return us;
    }

    public void setUs(UserSupplement us) {
        this.us = us;
    }
}
