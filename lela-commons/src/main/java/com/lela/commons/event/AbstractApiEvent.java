/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.event;

import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.Application;
import com.lela.domain.document.User;

/**
 * User: Chris Tallent
 * Date: 10/29/12
 * Time: 2:46 PM
 */
public class AbstractApiEvent extends AbstractUserEvent {
    private final AffiliateAccount affiliate;
    private final Application application;

    public AbstractApiEvent(User user, AffiliateAccount affiliate, Application application) {
        super(user);
        this.affiliate = affiliate;
        this.application = application;
    }

    public AffiliateAccount getAffiliate() {
        return affiliate;
    }

    public Application getApplication() {
        return application;
    }
}
