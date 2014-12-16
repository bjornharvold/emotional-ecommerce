/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import com.lela.domain.document.User;
import com.lela.domain.enums.FunctionalSortType;

import java.util.List;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 4/7/12
 * Time: 12:52 AM
 * Responsibility:
 */
public class SimpleCategoryItemsQuery {
    /** Update flag to save user preset */
    private Boolean update = true;

    /** Whether to sort or not */
    private Boolean sort = true;

    /** Field description */
    private String rlnm;

    private Map<String, List<String>> filters;

    /** Sort items by */
    private FunctionalSortType sortBy;

    /** User */
    private String userCode;

    public Map<String, List<String>> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, List<String>> filters) {
        this.filters = filters;
    }

    public Boolean getUpdate() {
        return update;
    }

    public void setUpdate(Boolean update) {
        this.update = update;
    }

    public Boolean getSort() {
        return sort;
    }

    public void setSort(Boolean sort) {
        this.sort = sort;
    }

    public String getRlnm() {
        return rlnm;
    }

    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    public FunctionalSortType getSortBy() {
        return sortBy;
    }

    public void setSortBy(FunctionalSortType sortBy) {
        this.sortBy = sortBy;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
}
