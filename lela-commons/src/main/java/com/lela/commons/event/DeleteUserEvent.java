/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.event;

import com.lela.domain.document.User;

/**
 * User: Chris Tallent
 * Date: 11/1/12
 * Time: 2:52 PM
 */
public class DeleteUserEvent extends AbstractUserEvent {
    public DeleteUserEvent(User user) {
        super(user);
    }
}
