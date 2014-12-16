/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.department;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 11/4/12
 * Time: 6:38 PM
 * Responsibility:
 */
public class DepartmentSearchResults {
    private final DepartmentQuery query;
    private Long count;
    private List<DepartmentSearchResult> categories;

    public DepartmentSearchResults(DepartmentQuery query) {
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

    public List<DepartmentSearchResult> getCategories() {
        return categories;
    }

    public void setCategories(List<DepartmentSearchResult> categories) {
        this.categories = categories;
    }
}
