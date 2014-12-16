/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.domain.dto.admin;

/**
 * User: Chris Tallent
 * Date: 8/13/12
 * Time: 10:01 AM
 */
public class AffiliateUserTrackingResults {
    private String id;
    private AffiliateUserTracking value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AffiliateUserTracking getValue() {
        return value;
    }

    public void setValue(AffiliateUserTracking value) {
        this.value = value;
    }
}
