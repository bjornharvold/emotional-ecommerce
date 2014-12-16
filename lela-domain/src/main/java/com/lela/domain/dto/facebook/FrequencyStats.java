/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.domain.dto.facebook;

import java.util.Date;

/**
 * User: Chris Tallent
 * Date: 3/23/12
 * Time: 11:58 AM
 */
public class FrequencyStats {
    private Date startDate = null;
    private Date endDate = null;
    private int total = 0;

    private float perDay = 0;
    private float perWeek = 0;
    private float perMonth = 0;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public float getPerDay() {
        return perDay;
    }

    public void setPerDay(float perDay) {
        this.perDay = perDay;
    }

    public float getPerWeek() {
        return perWeek;
    }

    public void setPerWeek(float perWeek) {
        this.perWeek = perWeek;
    }

    public float getPerMonth() {
        return perMonth;
    }

    public void setPerMonth(float perMonth) {
        this.perMonth = perMonth;
    }
}
