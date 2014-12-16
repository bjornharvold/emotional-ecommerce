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
 * Time: 2:48 PM
 */
public class QuizFinishEvent extends AbstractApiEvent {
    public QuizFinishEvent(User user, AffiliateAccount affiliate, Application application) {
        super(user, affiliate, application);
    }
}
