/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.bootstrap;

/**
 * User: bjorn
 * Date: Nov 4, 2007
 * Time: 11:17:25 AM
 */
public interface Bootstrapper {
    void create() throws BootstrapperException;
    Boolean getEnabled();
}
