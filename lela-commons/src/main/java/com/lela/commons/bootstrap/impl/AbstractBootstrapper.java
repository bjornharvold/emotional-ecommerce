/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.bootstrap.impl;

import com.lela.commons.bootstrap.Bootstrapper;
import com.lela.util.utilities.jackson.CustomObjectMapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * User: bjorn
 * Date: Aug 21, 2008
 * Time: 3:05:40 PM
 */
public abstract class AbstractBootstrapper implements Bootstrapper {
    protected CustomObjectMapper mapper = new CustomObjectMapper();
    protected final DateFormat df = new SimpleDateFormat("yyyyMMdd");

    public abstract Boolean getEnabled();
}
