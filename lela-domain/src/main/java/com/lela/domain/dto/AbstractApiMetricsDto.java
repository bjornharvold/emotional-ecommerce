/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 8/25/12
 * Time: 1:18 PM
 * Responsibility:
 */
public class AbstractApiMetricsDto extends AbstractApiDto implements Serializable {
    private static final long serialVersionUID = 7673878327268920049L;

    /** Is google analytics enabled for this environment */
    private boolean analyticsEnabled = false;

    /** Kissmetrics enabled */
    private boolean kmEnabled = false;

    /** Kissmetrics Key */
    private String kmKey;

    public boolean isAnalyticsEnabled() {
        return analyticsEnabled;
    }

    public void setAnalyticsEnabled(boolean analyticsEnabled) {
        this.analyticsEnabled = analyticsEnabled;
    }

    public boolean isKmEnabled() {
        return kmEnabled;
    }

    public void setKmEnabled(boolean kmEnabled) {
        this.kmEnabled = kmEnabled;
    }

    public String getKmKey() {
        return kmKey;
    }

    public void setKmKey(String kmKey) {
        this.kmKey = kmKey;
    }
}