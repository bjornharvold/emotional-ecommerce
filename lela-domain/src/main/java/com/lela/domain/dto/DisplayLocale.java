/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

/**
 * Created by Bjorn Harvold
 * Date: 9/2/11
 * Time: 1:05 PM
 * Responsibility:
 */
public class DisplayLocale implements Comparable<DisplayLocale> {
    private final String key;
    private final String value;

    public DisplayLocale(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int compareTo(DisplayLocale dl) {
        return this.key.compareTo(dl.getKey());
    }
}
