/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.util;

/**
 * Created by Bjorn Harvold
 * Date: 9/15/11
 * Time: 4:18 PM
 * Responsibility:
 */
public class UtilityException extends Exception {
    public UtilityException() {
        super();
    }

    public UtilityException(String msg) {
        super(msg);
    }

    public UtilityException(String msg, Throwable t) {
        super(msg, t);
    }
}
