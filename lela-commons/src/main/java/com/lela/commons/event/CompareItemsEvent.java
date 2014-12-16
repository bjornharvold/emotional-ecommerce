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
public class CompareItemsEvent extends AbstractUserEvent {
    private final String[] itemUrlNames;

    public CompareItemsEvent(User user, String[] itemUrlNames) {
        super(user);
        this.itemUrlNames = itemUrlNames;
    }

    public String[] getItemUrlNames() {
        return itemUrlNames;
    }
}