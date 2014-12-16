/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.domain.dto.job;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Chris Tallent
 * Date: 8/25/12
 * Time: 2:47 PM
 */
public class JobRunParameters {
    private Map<String, String> params = new HashMap<String, String>();

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
