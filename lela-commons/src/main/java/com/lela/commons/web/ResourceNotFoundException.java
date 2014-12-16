/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by IntelliJ IDEA.
 * User: Chris Tallent
 * Date: 1/10/12
 * Time: 2:26 PM
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    String[] params;

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String msg) {
        super(msg);
    }

    public ResourceNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public ResourceNotFoundException(String msg, Throwable t, String... params) {
        super(msg, t);
        this.params = params;

    }

    public ResourceNotFoundException(String key, String... params) {
        super(key);
        this.params = params;
    }

    public String[] getParams() {
        return params;
    }
}
