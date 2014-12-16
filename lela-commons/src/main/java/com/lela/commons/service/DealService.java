/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.domain.document.Deal;
import com.lela.domain.dto.department.DepartmentLandingPage;
import com.lela.domain.dto.department.DepartmentLandingPageData;
import com.lela.domain.dto.department.DepartmentQuery;
import com.lela.domain.dto.department.DepartmentSearchResults;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 11/1/12
 * Time: 5:08 PM
 * Responsibility:
 */
public interface DealService {

    List<Deal> findDealsForStores(List<String> storeUrlNames);

    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    List<Deal> saveDeals(List<Deal> deals);

    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    Deal saveDeal(Deal deal);

    @PreAuthorize("hasAnyRole('RIGHT_DELETE_APPLICATION')")
    void removeDealsForStore(String urlName);

    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    List<Deal> removeAndSave(List<Deal> deals);

    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    Deal removeAndSave(Deal deal);
}
