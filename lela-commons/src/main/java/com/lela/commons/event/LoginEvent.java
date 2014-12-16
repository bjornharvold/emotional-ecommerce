/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.event;

import com.lela.domain.document.User;
import com.lela.domain.enums.AuthenticationType;

/**
 * User: Chris Tallent
 * Date: 10/26/12
 * Time: 1:41 PM
 */
public class LoginEvent  extends AbstractUserEvent {
    private final AuthenticationType authenticationType;

    public LoginEvent(User user, AuthenticationType authenticationType) {
        super(user);
        this.authenticationType = authenticationType;
    }

    public AuthenticationType getAuthenticationType() {
        return authenticationType;
    }
}