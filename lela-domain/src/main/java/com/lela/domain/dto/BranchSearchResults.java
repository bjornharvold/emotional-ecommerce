/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import java.util.List;

/**
 * Created by Chris Tallent
 * Date: 11/8/11
 * Time: 11:41 AM
 * Responsibility:
 */
public class BranchSearchResults {

    private List<BranchSearchResult> results;
    private Float longitude;
    private Float latitude;
    private String searchedForZipcode;

    public List<BranchSearchResult> getResults() {
        return results;
    }

    public void setResults(List<BranchSearchResult> results) {
        this.results = results;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public String getSearchedForZipcode() {
        return searchedForZipcode;
    }

    public void setSearchedForZipcode(String searchedForZipcode) {
        this.searchedForZipcode = searchedForZipcode;
    }
}
