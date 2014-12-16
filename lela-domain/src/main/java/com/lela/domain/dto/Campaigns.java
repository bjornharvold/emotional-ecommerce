/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import com.lela.domain.document.Campaign;

import java.util.List;

public class Campaigns extends AbstractJSONPayload {
    private List<Campaign> list;

    public Campaigns() {
    }

    public Campaigns(List<Campaign> list) {
        this.list = list;
    }

    public List<Campaign> getList() {
        return list;
    }

    public void setList(List<Campaign> list) {
        this.list = list;
    }
}
