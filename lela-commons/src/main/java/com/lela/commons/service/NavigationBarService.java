/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.domain.document.CategoryGroup;
import com.lela.domain.document.NavigationBar;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Locale;

/**
 * Created by Bjorn Harvold
 * Date: 6/1/12
 * Time: 8:34 PM
 * Responsibility:
 */
public interface NavigationBarService {
    Page<NavigationBar> findNavigationBars(Integer page, Integer maxResults);
    List<NavigationBar> findNavigationBars(List<String> fields);
    NavigationBar findNavigationBarByUrlName(String urlName);
    List<NavigationBar> saveNavigationBars(List<NavigationBar> list);
    NavigationBar saveNavigationBar(NavigationBar nb);
    NavigationBar removeNavigationBar(String urlName);
    NavigationBar findDefaultNavigationBar(Locale locale);
    CategoryGroup findCategoryGroup(String navigationBarUrlName, String categoryGroupId);
    NavigationBar findNavigationBarByGroup(String categoryGroupUrlName);
    void saveCategoryGroup(String navigationBarUrlName, CategoryGroup categoryGroup);
    void removeCategoryGroup(String navigationBarUrlName, String categoryGroupId);
}
