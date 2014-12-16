/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.domain.dto.admin;

import java.util.TreeMap;
import java.util.Map;

/**
 * User: Chris Tallent
 * Date: 8/13/12
 * Time: 9:52 AM
 */
public class AffiliateUserTracking extends AffiliateUserTrackingPerPeriod {
    private AffiliateUserTrackingPerPeriod yesterday;
    private TreeMap<String, AffiliateUserTrackingPerPeriod> byMonth;
    private TreeMap<String, AffiliateUserTrackingPerYear> byYear;

    public AffiliateUserTrackingPerPeriod getYesterday() {
        return yesterday;
    }

    public void setYesterday(AffiliateUserTrackingPerPeriod yesterday) {
        this.yesterday = yesterday;
    }

    public TreeMap<String, AffiliateUserTrackingPerPeriod> getByMonth() {
        return byMonth;
    }

    public void setByMonth(TreeMap<String, AffiliateUserTrackingPerPeriod> byMonth) {
        this.byMonth = byMonth;
    }

    public TreeMap<String, AffiliateUserTrackingPerYear> getByYear() {
        return byYear;
    }

    public void setByYear(TreeMap<String, AffiliateUserTrackingPerYear> byYear) {
        this.byYear = byYear;
    }
}
