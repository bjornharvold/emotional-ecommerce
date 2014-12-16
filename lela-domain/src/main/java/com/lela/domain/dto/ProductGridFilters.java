/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 8/23/12
 * Time: 5:55 PM
 * Responsibility:
 */
public class ProductGridFilters {
    private HashMap<String, List<String>> filters;

    public HashMap<String, List<String>> getFilters() {
        return filters;
    }

    public void setFilters(HashMap<String, List<String>> filters) {
        this.filters = filters;
    }
}
