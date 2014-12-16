/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.compute.dto.compute;

/**
 * Created by Bjorn Harvold
 * Date: 2/4/12
 * Time: 1:02 AM
 * Responsibility:
 */
public class MotivatorMatchResult {
    private String identifier;
    private MotivatorMatch relevancy;
    private Integer totalRelevancy;

    public MotivatorMatchResult() {
    }

    public MotivatorMatchResult(String identifier, MotivatorMatch relevancy) {
        this.identifier = identifier;
        this.relevancy = relevancy;

        if (relevancy != null && relevancy.getMotivators() != null && !relevancy.getMotivators().isEmpty()) {
            this.totalRelevancy = 0;
            for (Integer value : relevancy.getMotivators().values()) {
                this.totalRelevancy += value;
            }
        }
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public MotivatorMatch getRelevancy() {
        return relevancy;
    }

    public void setRelevancy(MotivatorMatch relevancy) {
        this.relevancy = relevancy;
    }

    public Integer getTotalRelevancy() {
        return totalRelevancy;
    }

    public void setTotalRelevancy(Integer totalRelevancy) {
        this.totalRelevancy = totalRelevancy;
    }
}
