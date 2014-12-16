/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.department;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 11/1/12
 * Time: 7:13 PM
 * Responsibility: This object is used from the department landing page to get a filter selection
 */
public class DepartmentQuery {
    private static final String CATEGORY = "category";
    private String rlnm;
    private List<String> availableCategoryUrlNames;
    private List<String> categoryUrlNames;
    private Map<String, Map<String, String>> filters;

    public List<String> getAvailableCategoryUrlNames() {
        return availableCategoryUrlNames;
    }

    public void setAvailableCategoryUrlNames(List<String> availableCategoryUrlNames) {
        this.availableCategoryUrlNames = availableCategoryUrlNames;
    }

    public String getRlnm() {
        return rlnm;
    }

    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    public List<String> getCategoryUrlNames() {
        return categoryUrlNames;
    }

    public void setCategoryUrlNames(List<String> categoryUrlNames) {
        this.categoryUrlNames = categoryUrlNames;
    }

    public Map<String, Map<String, String>> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Map<String, String>> filters) {
        this.filters = filters;
    }

    public List<String> findCategoriesFromFilter() {
        List<String> result = null;

        if (filters != null && !filters.isEmpty()) {
            for (String filterKey : filters.keySet()) {
                if (StringUtils.equals(filterKey, CATEGORY)) {
                    Map<String, String> categories = filters.get(filterKey);

                    if (categories != null && !categories.isEmpty()) {
                        result = new ArrayList<String>(categories.size());
                        for (String categoryUrlName : categories.keySet()) {
                            result.add(categoryUrlName);
                        }
                    }
                }
            }
        }

        return result;
    }
}
