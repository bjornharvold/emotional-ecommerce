/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.user;

import com.lela.domain.dto.AbstractApiDto;
import com.lela.domain.dto.AbstractJSONPayload;

import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 7/4/12
 * Time: 3:45 PM
 * Responsibility:
 */
public class RegisterUserResponse extends AbstractApiDto implements Serializable {
    private static final long serialVersionUID = 88364931203084622L;

    private final String email;
    private boolean userAlreadyExists = false;

    public RegisterUserResponse(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public boolean isUserAlreadyExists() {
        return userAlreadyExists;
    }

    public void setUserAlreadyExists(boolean userAlreadyExists) {
        this.userAlreadyExists = userAlreadyExists;
    }
}
