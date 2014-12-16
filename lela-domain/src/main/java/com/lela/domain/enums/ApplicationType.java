/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 7/3/12
 * Time: 3:06 PM
 * Responsibility:
 */
public enum ApplicationType {
    QUIZ("quiz"),
    PRODUCT_GRID("grid"),
    CURATED_PRODUCT_GRID("curated");

    private static final Map<String, ApplicationType> byRlnm = new HashMap<String, ApplicationType>();
    private String rlnm;
    private ApplicationType(String rlnm)
    {
        this.rlnm = rlnm;
    }

    public String getRlnm()
    {
        return this.rlnm;
    }

    public static ApplicationType getByRlnm(String rlnm) {
        return byRlnm.get(rlnm);
    }

    static {
        for (ApplicationType type : ApplicationType.values()) {
            byRlnm.put(type.getRlnm(), type);
        }
    }
}
