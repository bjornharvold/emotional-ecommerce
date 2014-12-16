/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 11/24/11
 * Time: 1:08 AM
 * Responsibility:
 */
public class CacheEvictions {
    private List<CacheEviction> list;

    public List<CacheEviction> getList() {
        return list;
    }

    public void setList(List<CacheEviction> list) {
        this.list = list;
    }
}
