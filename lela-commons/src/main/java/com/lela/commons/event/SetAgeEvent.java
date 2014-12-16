/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.event;

import com.lela.domain.document.User;
import com.lela.domain.enums.Gender;

/**
 * User: Chris Tallent
 * Date: 11/5/12
 * Time: 4:31 PM
 */
public class SetAgeEvent extends AbstractUserEvent {
    private final int age;

    public SetAgeEvent(User user, int age) {
        super(user);
        this.age = age;
    }

    public int getAge() {
        return age;
    }
}
