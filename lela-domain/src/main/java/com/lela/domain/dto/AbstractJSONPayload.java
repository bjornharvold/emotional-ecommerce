/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

/**
 * Created by Bjorn Harvold
 * Date: 8/5/11
 * Time: 10:02 AM
 * Responsibility:
 */
public abstract class AbstractJSONPayload {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
