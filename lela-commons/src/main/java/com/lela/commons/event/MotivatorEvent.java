/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.event;

import com.lela.domain.document.Motivator;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;

/**
 * User: Chris Tallent
 * Date: 10/18/12
 * Time: 9:00 PM
 */
public class MotivatorEvent extends AbstractUserEvent {
    private final Motivator motivator;

    public MotivatorEvent(User user, Motivator motivator) {
        super(user);
        this.motivator = motivator;
    }

    public Motivator getMotivator() {
        return motivator;
    }
}
