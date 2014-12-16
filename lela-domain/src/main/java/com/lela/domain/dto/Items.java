/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import com.lela.domain.document.Item;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 7/28/11
 * Time: 12:37 PM
 * Responsibility:
 */
public class Items extends AbstractJSONPayload {
    private List<Item> list;

    public Items() {
    }

    public Items(List<Item> list) {
        this.list = list;
    }

    public List<Item> getList() {
        return list;
    }

    public void setList(List<Item> list) {
        this.list = list;
    }
}
