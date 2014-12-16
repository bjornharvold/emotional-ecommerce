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
public class ViewedDepartmentEvent extends AbstractUserEvent {
    private final String departmentUrlName;

    public ViewedDepartmentEvent(User user, String departmentUrlName) {
        super(user);
        this.departmentUrlName = departmentUrlName;
    }

    public String getDepartmentUrlName() {
        return departmentUrlName;
    }
}