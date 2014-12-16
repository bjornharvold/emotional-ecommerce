/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import com.lela.domain.document.Branch;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 7/28/11
 * Time: 12:37 PM
 * Responsibility:
 */
public class Branches extends AbstractJSONPayload {
    private List<Branch> list;

    public Branches() {
    }

    public Branches(List<Branch> list) {
        this.list = list;
    }

    public List<Branch> getList() {
        return list;
    }

    public void setList(List<Branch> list) {
        this.list = list;
    }
}
