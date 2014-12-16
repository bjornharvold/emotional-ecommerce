/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.event;

import com.lela.domain.document.User;

/**
 * User: Chris Tallent
 * Date: 10/31/12
 * Time: 3:22 PM
 */
public class ViewedCategoryEvent extends AbstractUserEvent {
    private final String categoryUrlName;

    public ViewedCategoryEvent(User user, String categoryUrlName) {
        super(user);
        this.categoryUrlName = categoryUrlName;
    }

    public String getCategoryUrlName() {
        return categoryUrlName;
    }
}