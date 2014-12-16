/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.event;

import com.lela.domain.document.Motivator;
import com.lela.domain.document.User;

/**
 * User: Chris Tallent
 * Date: 10/18/12
 * Time: 9:00 PM
 */
public class PageViewEvent extends AbstractUserEvent {
    public PageViewEvent(User user) {
        super(user);
    }
}
