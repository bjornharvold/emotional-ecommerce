/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.compute.dto.compute;

import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 2/4/12
 * Time: 12:14 AM
 * Responsibility:
 */
public class MotivatorMatch {
    private String identifier;
    private Map<String, Integer> motivators;

    public MotivatorMatch() {
    }

    public MotivatorMatch(String identifier, Map<String, Integer> relevancy) {
        this.identifier = identifier;
        this.motivators = relevancy;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Map<String, Integer> getMotivators() {
        return motivators;
    }

    public void setMotivators(Map<String, Integer> motivators) {
        this.motivators = motivators;
    }
}
