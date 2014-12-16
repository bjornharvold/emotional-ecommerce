/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.store;

import com.lela.domain.document.Branch;

import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 5/10/12
 * Time: 5:07 PM
 * Responsibility:
 */
public class LocalStoreAggregate extends StoreAggregate implements Serializable {
    private static final long serialVersionUID = 8702684112762762528L;
    private BranchDistance branchDistance;

    public LocalStoreAggregate(StoreAggregate sa) {
        super(sa);
    }

    public BranchDistance getBranchDistance() {
        return branchDistance;
    }

    public void setBranchDistance(BranchDistance branchDistance) {
        this.branchDistance = branchDistance;
    }
}
