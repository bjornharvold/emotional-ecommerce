/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

/**
 * Created by Bjorn Harvold
 * Date: 8/30/11
 * Time: 2:51 PM
 * Responsibility:
 */
public class ForgotPassword extends AbstractJSONPayload {
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
