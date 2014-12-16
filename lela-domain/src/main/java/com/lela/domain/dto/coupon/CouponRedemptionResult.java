/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.coupon;

import com.lela.domain.document.Branch;
import com.lela.domain.document.Coupon;
import com.lela.domain.document.Offer;

/**
 * Created by Bjorn Harvold
 * Date: 3/15/12
 * Time: 1:12 AM
 * Responsibility:
 */
public class CouponRedemptionResult {
    private Coupon coupon;
    private Branch branch;
    private Offer offer;

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }
}
