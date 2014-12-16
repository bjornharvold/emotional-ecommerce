/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.domain.document.Branch;
import com.lela.domain.dto.BranchSearchResult;
import com.lela.domain.dto.LocationQuery;
import com.lela.domain.dto.store.BranchDistance;
import com.lela.domain.dto.store.LocalStoresSearchResult;

import java.util.List;
import java.util.Set;

/**
 * Created by Bjorn Harvold
 * Date: 5/9/12
 * Time: 6:48 PM
 * Responsibility:
 */
public interface BranchService {
    List<Branch> saveBranches(List<Branch> list);
    Branch saveBranch(Branch branch);
    Branch findBranchByUrlName(String urlName);
    Branch findBranchByLocalCode(String localCode);
    List<Branch> findBranchesByAffiliateUrlName(String affiliateUrlName);
    List<Branch> findBranchesByMerchantId(String merchantId);
    Branch removeBranch(String rlnm);
    List<BranchSearchResult> findNearest(Float longitude, Float latitude, Float radiusInMiles, Set<String> merchantIds);

    List<BranchDistance> findNearest(Float longitude, Float latitude, Float radiusInMiles);

    LocalStoresSearchResult findLocalStoreAggregateDetails(LocationQuery query);
}
