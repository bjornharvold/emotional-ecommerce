/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.store;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 5/10/12
 * Time: 10:41 PM
 * Responsibility:
 */
public class StoreAggregateQuery {
    private String filterOnLetter;
    private List<String> facetQueries;

    public String getFilterOnLetter() {
        return filterOnLetter;
    }

    public void setFilterOnLetter(String filterOnLetter) {
        this.filterOnLetter = filterOnLetter;
    }

    public List<String> getFacetQueries() {
        return facetQueries;
    }

    public void setFacetQueries(List<String> facetQueries) {
        this.facetQueries = facetQueries;
    }
}
