/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.remote;

/**
 * User: Bjorn Harvold
 * Date: May 5, 2006
 * Time: 11:13:52 AM
 */
public class MerchantException extends Exception {

    private static final long serialVersionUID = -7157684567162634556L;

    String[] params;

    public MerchantException() {
        super();
    }

    public MerchantException(String msg) {
        super(msg);
    }

    public MerchantException(String msg, Throwable t) {
        super(msg, t);
    }

    public MerchantException(String msg, Throwable t, String... params) {
        super(msg, t);
        this.params = params;

    }

    public MerchantException(String key, String... params) {
        super(key);
        this.params = params;
    }

    public String[] getParams() {
        return params;
    }
}
