/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons;

/**
 * Created by IntelliJ IDEA.
 * User: Martin Gamboa
 * Date: 10/6/11
 * Time: 2:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class LelaException extends RuntimeException {

    String[] params;

    public LelaException() {
        super();
    }

    public LelaException(String msg) {
        super(msg);
    }

    public LelaException(String msg, Throwable t) {
        super(msg, t);
    }

    public LelaException(String msg, Throwable t, String... params) {
        super(msg, t);
        this.params = params;

    }

    public LelaException(String key, String... params) {
        super(key);
        this.params = params;
    }

    public String[] getParams() {
        return params;
    }
}
