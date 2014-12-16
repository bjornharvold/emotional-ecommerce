/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.event;

import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.document.UserVisit;
import com.lela.domain.enums.RegistrationType;

/**
 * User: Chris Tallent
 * Date: 10/18/12
 * Time: 9:00 PM
 */
public class UserVisitEvent extends AbstractUserEvent {

    private final UserVisit visit;

    public UserVisitEvent(User user, UserVisit visit) {
        super(user);
        this.visit = visit;
    }

    public UserVisit getVisit() {
        return visit;
    }
}
