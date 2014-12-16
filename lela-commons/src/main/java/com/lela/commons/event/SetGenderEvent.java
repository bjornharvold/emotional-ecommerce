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
public class SetGenderEvent extends AbstractUserEvent {
    private final Gender gender;

    public SetGenderEvent(User user, Gender gender) {
        super(user);
        this.gender = gender;
    }

    public Gender getGender() {
        return gender;
    }
}
