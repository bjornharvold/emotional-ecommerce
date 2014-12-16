/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 4/26/12
 * Time: 8:53 PM
 * Responsibility:
 */
public class FacetSearchQuery {
    private List<String> facetFields;
    private List<String> facetQueries;
    private Map<String, String> queryMap;

    public void setFacetFields(List<String> facetFields) {
        this.facetFields = facetFields;
    }

    public void setQueryMap(Map<String, String> queryMap) {
        this.queryMap = queryMap;
    }

    public List<String> getFacetFields() {
        return facetFields;
    }

    public Map<String, String> getQueryMap() {
        return queryMap;
    }

    public List<String> getFacetQueries() {
        return facetQueries;
    }

    public void setFacetQueries(List<String> facetQueries) {
        this.facetQueries = facetQueries;
    }

    public String getQueryString() {
        KeywordTerms terms = null;

        if (queryMap != null && !queryMap.isEmpty()) {
            terms = new KeywordTerms();

            for (Map.Entry<String, String> entry : queryMap.entrySet()) {
                boolean wildcard = false;
                List<String> values = new ArrayList<String>(1);

                String value = entry.getValue();

                // if the value has a wildcard on it, we want to recognize this
                if (value.endsWith("*")) {
                    wildcard = true;
                    value = value.substring(0, value.length()-1);
                }

                values.add(value);
                terms.addSearchCondition(entry.getKey(), values, SearchConditionType.AND, wildcard);
            }
        }

        return terms != null ? terms.getQuery() : "*:*";
    }
}
