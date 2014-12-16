/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.coupon;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;

/**
 * Created by Bjorn Harvold
 * Date: 3/15/12
 * Time: 1:07 AM
 * Responsibility:
 */
public class CouponRedemptionRequest {
    @NotNull
    @NotEmpty
    private String localCode;

    @NotNull
    @NotEmpty
    private String couponCode;

    @NotNull
    @NotEmpty
    private String branchUrlName;

    public CouponRedemptionRequest() {}

    public CouponRedemptionRequest(String branchUrlName, String couponCode, String localCode) {
        this.localCode = localCode;
        this.couponCode = couponCode;
        this.branchUrlName = branchUrlName;
    }

    public String getLocalCode() {
        return localCode;
    }

    public void setLocalCode(String localCode) {
        this.localCode = localCode;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getBranchUrlName() {
        return branchUrlName;
    }

    public void setBranchUrlName(String branchUrlName) {
        this.branchUrlName = branchUrlName;
    }
}
