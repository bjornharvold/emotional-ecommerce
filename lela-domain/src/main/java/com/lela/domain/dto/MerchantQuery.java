/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import com.lela.domain.enums.MerchantType;

import java.util.HashMap;

/**
 * Created by Bjorn Harvold
 * Date: 9/13/11
 * Time: 8:38 PM
 * Responsibility:
 */
public class MerchantQuery {
    HashMap<String, Object> map = new HashMap<String, Object>();
    private MerchantType type;

    public MerchantQuery(MerchantType type) {
        this.type = type;
    }

    public MerchantType getType() {
        return type;
    }

    public HashMap<String, Object> getMap() {
        return map;
    }

    public void addEntry(String key, Object value) {
        map.put(key, value);
    }

    public Object getValue(String key) {
        return map.get(key);
    }
}
