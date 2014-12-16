/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.compute.dto.compute;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 2/4/12
 * Time: 1:02 AM
 * Responsibility:
 */
public class MotivatorMatchResults {
    private List<MotivatorMatchResult> perProductRelevancy;
    private List<MotivatorMatchResult> totalProductRelevancy;

    public List<MotivatorMatchResult> getPerProductRelevancy() {
        return perProductRelevancy;
    }

    public void setPerProductRelevancy(List<MotivatorMatchResult> perProductRelevancy) {
        this.perProductRelevancy = perProductRelevancy;
    }

    public List<MotivatorMatchResult> getTotalProductRelevancy() {
        return totalProductRelevancy;
    }

    public void setTotalProductRelevancy(List<MotivatorMatchResult> totalProductRelevancy) {
        this.totalProductRelevancy = totalProductRelevancy;
    }

    public void addMotivatorMatchResult(MotivatorMatchResult mmr) {
        if (this.perProductRelevancy == null) {
            this.perProductRelevancy = new ArrayList<MotivatorMatchResult>();
        }
        
        this.perProductRelevancy.add(mmr);
    }
}
