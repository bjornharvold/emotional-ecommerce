/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.Branch;
import com.lela.domain.dto.BranchSearchResult;
import com.lela.domain.dto.store.BranchDistance;

import java.util.List;
import java.util.Set;

/**
 * User: Chris Tallent
 * Date: 11/9/11
 * Time: 4:42 PM
 */
public interface BranchRepositoryCustom {

    List<BranchSearchResult> findNearest(Float longitude, Float latitude, Float radiusInMiles, Set<String> merchantIds);

    List<BranchDistance> findNearest(Float longitude, Float latitude, Float radiusInMiles);
}
