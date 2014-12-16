/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import java.util.List;
import java.util.Locale;

import com.lela.domain.document.NavigationBar;

/**
 * Created by Bjorn Harvold
 * Date: 6/22/12
 * Time: 4:36 PM
 * Responsibility:
 */
public interface NavigationBarRepositoryCustom {
    void setDefaultFlagOnCollection(Locale locale, Boolean isDefault);
    List<NavigationBar> findNavigationBars(List<String> fields);

    boolean departmentExists(String departmentUrlName);
}
