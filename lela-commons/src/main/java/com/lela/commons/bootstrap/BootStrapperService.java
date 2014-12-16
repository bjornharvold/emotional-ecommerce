/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.bootstrap;

/**
 * Created by Bjorn Harvold
 * Date: 4/2/11
 * Time: 10:49 PM
 * Responsibility:
 */
public interface BootStrapperService {
    void init();

    Boolean isComplete();
}
