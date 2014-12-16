/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.event;

import com.lela.domain.document.User;
import com.lela.domain.dto.user.Address;

/**
 * User: Chris Tallent
 * Date: 11/6/12
 * Time: 8:08 AM
 */
public class SetAddressEvent extends AbstractUserEvent {
    private final Address address;

    public SetAddressEvent(User user, Address address) {
        super(user);
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }
}
