/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.coupon;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 3/7/12
 * Time: 11:54 PM
 * Responsibility:
 */
public class CouponGenerationRequest {
    @NotNull
    @NotEmpty
    private String offerUrlName;
    private String itemUrlName;

    public CouponGenerationRequest() {
    }

    public CouponGenerationRequest(String offerUrlName, String itemUrlName) {
        this.offerUrlName = offerUrlName;
        this.itemUrlName = itemUrlName;
    }

    public String getOfferUrlName() {
        return offerUrlName;
    }

    public void setOfferUrlName(String offerUrlName) {
        this.offerUrlName = offerUrlName;
    }

    public String getItemUrlName() {
        return itemUrlName;
    }

    public void setItemUrlName(String itemUrlName) {
        this.itemUrlName = itemUrlName;
    }
}
