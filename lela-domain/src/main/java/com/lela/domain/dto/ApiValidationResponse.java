/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.Application;

/**
 * Created by Bjorn Harvold
 * Date: 8/24/12
 * Time: 10:39 PM
 * Responsibility:
 */
public class ApiValidationResponse {
    private AffiliateAccount affiliateAccount;
    private Application application;
    private Boolean valid = false;

    public AffiliateAccount getAffiliateAccount() {
        return affiliateAccount;
    }

    public Application getApplication() {
        return application;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setAffiliateAccount(AffiliateAccount affiliateAccount) {
        this.affiliateAccount = affiliateAccount;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }
}
