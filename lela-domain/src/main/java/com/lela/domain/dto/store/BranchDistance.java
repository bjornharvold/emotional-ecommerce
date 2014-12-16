/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.store;

import com.lela.domain.document.Branch;

import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 5/10/12
 * Time: 5:28 PM
 * Responsibility:
 */
public class BranchDistance implements Serializable {
    private static final long serialVersionUID = 5371350162517978204L;
    private final Branch branch;
    private final Double distance;

    public BranchDistance(Branch branch, Double distance) {
        this.branch = branch;
        this.distance = distance;
    }

    public Branch getBranch() {
        return branch;
    }

    public Double getDistance() {
        return distance;
    }
}
