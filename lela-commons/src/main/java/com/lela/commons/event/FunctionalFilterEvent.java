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
public class FunctionalFilterEvent extends AbstractUserEvent {
    private final String categoryUrlName;
    private final String filter;
    private final String option;
    private final String value;

    public FunctionalFilterEvent(User user, String categoryUrlName, String filter, String option, String value) {
        super(user);
        this.categoryUrlName = categoryUrlName;
        this.filter = filter;
        this.option = option;
        this.value = value;
    }

    public String getCategoryUrlName() {
        return categoryUrlName;
    }

    public String getFilter() {
        return filter;
    }

    public String getOption() {
        return option;
    }

    public String getValue() {
        return value;
    }
}
