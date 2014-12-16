/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.event;

import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;

/**
 * User: Chris Tallent
 * Date: 10/23/12
 * Time: 1:13 PM
 */
public class AbstractUserEvent extends AbstractEvent {
    protected final User user;

    public AbstractUserEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
