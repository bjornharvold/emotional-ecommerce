/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.domain.dto.department.DepartmentLandingPage;
import com.lela.domain.dto.department.DepartmentLandingPageData;
import com.lela.domain.dto.department.DepartmentQuery;
import com.lela.domain.dto.department.DepartmentSearchResults;

/**
 * Created by Bjorn Harvold
 * Date: 11/1/12
 * Time: 5:08 PM
 * Responsibility:
 */
public interface DepartmentService {
    Long findFunctionalFilterCountByCategoryGroupUrlName(String categoryGroupUrlName);

    DepartmentLandingPage findDepartmentLandingPage(String categoryGroupUrlName, String userCode);

    DepartmentLandingPageData findDepartmentLandingPageData(DepartmentQuery query);

    DepartmentSearchResults findDepartmentSearchResults(String userCode, DepartmentQuery query);
}
