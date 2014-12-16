/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.bootstrap;

/**
 * User: bjorn
 * Date: Nov 4, 2007
 * Time: 11:18:03 AM
 */
public class BootstrapperException extends Exception {
	private static final long serialVersionUID = 792559203647529430L;
	String[] params;

    public BootstrapperException() {
        super();
    }

    public BootstrapperException(String msg) {
        super(msg);
    }

    public BootstrapperException(String msg, Throwable t) {
        super(msg, t);
    }

    public BootstrapperException(String msg, Throwable t, String... params) {
        super(msg, t);
        this.params = params;
    }

    public BootstrapperException(String key, String... params) {
        super(key);
        this.params = params;
    }

    public String[] getParams() {
        return params;
    }
}
