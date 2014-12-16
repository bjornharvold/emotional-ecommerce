/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import com.lela.domain.document.Owner;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 7/28/11
 * Time: 12:37 PM
 * Responsibility:
 */
public class Owners extends AbstractJSONPayload {
    private List<Owner> list;

    public Owners() {
    }

    public Owners(List<Owner> list) {
        this.list = list;
    }

    public List<Owner> getList() {
        return list;
    }

    public void setList(List<Owner> list) {
        this.list = list;
    }
}
