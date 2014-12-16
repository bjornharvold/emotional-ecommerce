/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

/**
 * Created by Bjorn Harvold
 * Date: 1/14/12
 * Time: 12:30 AM
 * Responsibility:
 */
public class RegistrationResult extends AbstractJSONPayload {
    public RegistrationResult(String message) {
        this.setMessage(message);
    }
}
