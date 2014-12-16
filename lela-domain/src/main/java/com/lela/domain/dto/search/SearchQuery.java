/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.search;

/**
 * Created by Bjorn Harvold
 * Date: 2/21/12
 * Time: 8:27 PM
 * Responsibility:
 */
public class SearchQuery {
    private String keywords;

    public SearchQuery(String keywords) {
        this.keywords = keywords;
    }

    public SearchQuery() {
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
