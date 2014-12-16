/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import com.lela.domain.enums.CacheType;

/**
 * Created by Bjorn Harvold
 * Date: 11/24/11
 * Time: 1:08 AM
 * Responsibility:
 */
public class CacheEviction {
    private CacheType type;
    private String key;

    public CacheEviction() {

    }

    public CacheEviction(CacheType type, String key) {
        this.type = type;
        this.key = key;
    }

    public CacheEviction(CacheType type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public CacheType getType() {
        return type;
    }

    public void setType(CacheType type) {
        this.type = type;
    }
}
