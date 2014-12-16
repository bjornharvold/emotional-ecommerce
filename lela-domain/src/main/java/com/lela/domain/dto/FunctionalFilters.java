/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import com.lela.domain.document.FunctionalFilter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 10/14/11
 * Time: 3:17 PM
 * Responsibility:
 */
public class FunctionalFilters extends AbstractJSONPayload implements Serializable {
    private static final long serialVersionUID = 5693763273958199632L;

    private List<FunctionalFilter> list;

    public FunctionalFilters() {
    }

    public FunctionalFilters(List<FunctionalFilter> list) {
        this.list = list;
    }

    public List<FunctionalFilter> getList() {
        return list;
    }

    public void setList(List<FunctionalFilter> list) {
        this.list = list;
    }
}
