/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.event;

import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.enums.RegistrationType;

/**
 * User: Chris Tallent
 * Date: 10/18/12
 * Time: 9:00 PM
 */
public class RegistrationEvent extends AbstractUserEvent {
    private final RegistrationType registrationType;
    private final UserSupplement us;
    private String rawPassword;

    public RegistrationEvent(User user, UserSupplement us, RegistrationType registrationType) {
        super(user);
        this.us = us;
        this.registrationType = registrationType;
    }

    public RegistrationType getRegistrationType() {
        return this.registrationType;
    }

    public UserSupplement getUserSupplement()
    {
        return this.us;
    }

    public String getRawPassword() {
        return rawPassword;
    }

    public void setRawPassword(String rawPassword) {
        this.rawPassword = rawPassword;
    }
}
