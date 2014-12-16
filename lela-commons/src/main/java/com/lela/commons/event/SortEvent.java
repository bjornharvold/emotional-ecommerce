/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.event;

import com.lela.domain.document.User;

/**
 * User: Chris Tallent
 * Date: 11/5/12
 * Time: 11:00 AM
 */
public class SortEvent extends AbstractUserEvent {
    private final String categoryUrlName;
    private final String sort;

    public SortEvent(User user, String categoryUrlName, String sort) {
        super(user);
        this.categoryUrlName = categoryUrlName;
        this.sort = sort;
    }

    public String getCategoryUrlName() {
        return categoryUrlName;
    }

    public String getSort() {
        return sort;
    }
}
