/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import com.lela.domain.enums.Comparison;
import com.lela.domain.enums.RecommendToFriend;

import javax.validation.constraints.NotNull;

/**
 * Created by Bjorn Harvold
 * Date: 10/27/11
 * Time: 12:55 PM
 * Responsibility:
 */
public class Feedback {
    @NotNull
    private Comparison comparison;

    @NotNull
    private String like;

    @NotNull
    private String change;

    @NotNull
    private RecommendToFriend recommendToFriend;

    @NotNull
    private String paypalEmail;

    @NotNull
    private Boolean agree;

    public Comparison getComparison() {
        return comparison;
    }

    public void setComparison(Comparison comparison) {
        this.comparison = comparison;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public RecommendToFriend getRecommendToFriend() {
        return recommendToFriend;
    }

    public void setRecommendToFriend(RecommendToFriend recommendToFriend) {
        this.recommendToFriend = recommendToFriend;
    }

    public String getPaypalEmail() {
        return paypalEmail;
    }

    public void setPaypalEmail(String paypalEmail) {
        this.paypalEmail = paypalEmail;
    }

    public Boolean getAgree() {
        return agree;
    }

    public void setAgree(Boolean agree) {
        this.agree = agree;
    }
}
