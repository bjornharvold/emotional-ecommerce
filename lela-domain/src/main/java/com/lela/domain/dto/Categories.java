/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import com.lela.domain.document.Category;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 7/28/11
 * Time: 12:37 PM
 * Responsibility:
 */
public class Categories extends AbstractJSONPayload {
    private List<Category> list;

    public Categories() {
    }

    public Categories(List<Category> list) {
        this.list = list;
    }

    public List<Category> getList() {
        return list;
    }

    public void setList(List<Category> list) {
        this.list = list;
    }
}
