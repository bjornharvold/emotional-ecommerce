/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.department;

import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 11/1/12
 * Time: 7:47 PM
 * Responsibility:
 */
public class DepartmentLandingPageData {
    private final DepartmentQuery query;
    private Map<String, Long> categoryCount;
    private Long count;

    public DepartmentLandingPageData(DepartmentQuery query) {
        this.query = query;
    }

    public DepartmentQuery getQuery() {
        return query;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Map<String, Long> getCategoryCount() {
        return categoryCount;
    }

    public void setCategoryCount(Map<String, Long> categoryCount) {
        this.categoryCount = categoryCount;
    }
}
