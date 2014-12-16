/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.coupon;

import com.lela.domain.document.Branch;
import com.lela.domain.document.Coupon;
import com.lela.domain.document.Offer;

/**
 * Created by Bjorn Harvold
 * Date: 3/8/12
 * Time: 12:47 AM
 * Responsibility:
 */
public class CouponGenerationResult {
    private Coupon coupon;
    private Boolean existing;
    private Offer offer;
    private Branch branch;

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public Boolean getExisting() {
        return existing;
    }

    public void setExisting(Boolean existing) {
        this.existing = existing;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }
}
