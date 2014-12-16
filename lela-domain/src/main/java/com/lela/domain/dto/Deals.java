/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import com.lela.domain.document.Deal;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 7/28/11
 * Time: 12:37 PM
 * Responsibility:
 */
public class Deals extends AbstractJSONPayload {
    private List<Deal> list;

    public Deals() {
    }

    public Deals(List<Deal> list) {
        this.list = list;
    }

    public List<Deal> getList() {
        return list;
    }

    public void setList(List<Deal> list) {
        this.list = list;
    }
}
