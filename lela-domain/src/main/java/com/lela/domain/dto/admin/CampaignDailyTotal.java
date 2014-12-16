/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto.admin;

/**
 * User: Chris Tallent
 * Date: 6/12/12
 * Time: 10:03 PM
 */
public class CampaignDailyTotal {
    private String id;
    private CampaignDailyTotalValue value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CampaignDailyTotalValue getValue() {
        return value;
    }

    public void setValue(CampaignDailyTotalValue value) {
        this.value = value;
    }
}