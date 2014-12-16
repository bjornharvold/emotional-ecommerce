/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.domain.dto;

/**
 * User: Chris Tallent
 * Date: 11/5/12
 * Time: 2:00 PM
 */
public class FilterEventTrack {
    private String filter;
    private String option;
    private String value;

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
