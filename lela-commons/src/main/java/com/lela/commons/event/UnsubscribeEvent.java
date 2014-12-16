/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.event;

import com.lela.domain.document.User;

/**
 * User: Chris Tallent
 * Date: 11/1/12
 * Time: 1:45 PM
 */
public class UnsubscribeEvent extends AbstractUserEvent {
    private final String listId;

    public UnsubscribeEvent(User user, String listId) {
        super(user);
        this.listId = listId;
    }

    public String getListId() {
        return listId;
    }
}