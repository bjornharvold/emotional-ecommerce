/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service;

/**
 * Created by Bjorn Harvold
 * Date: 2/18/12
 * Time: 1:24 AM
 * Responsibility:
 */
public class SearchException extends Exception {
    private String[] params;

    public SearchException() {
        super();
    }

    public SearchException(String msg) {
        super(msg);
    }

    public SearchException(String msg, Throwable t) {
        super(msg, t);
    }

    public SearchException(String msg, Throwable t, String... params) {
        super(msg, t);
        this.params = params;
    }

    public SearchException(String key, String... params) {
        super(key);
        this.params = params;
    }
}
