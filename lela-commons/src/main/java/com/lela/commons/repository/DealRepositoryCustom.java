/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.Deal;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 11/28/12
 * Time: 12:56 AM
 * Responsibility:
 */
public interface DealRepositoryCustom {
    List<Deal> findDealsForStore(String storeUrlName);
}
