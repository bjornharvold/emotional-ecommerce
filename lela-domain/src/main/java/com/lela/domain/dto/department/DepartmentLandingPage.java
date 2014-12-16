/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.department;

import com.lela.domain.document.Category;
import com.lela.domain.document.FunctionalFilter;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 11/1/12
 * Time: 4:59 PM
 * Responsibility:
 */
public class DepartmentLandingPage {
    private List<FunctionalFilter> filters;

    public List<FunctionalFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<FunctionalFilter> filters) {
        this.filters = filters;
    }

}
