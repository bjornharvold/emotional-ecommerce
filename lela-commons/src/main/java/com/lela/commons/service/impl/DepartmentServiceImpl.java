/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import com.lela.commons.comparator.FunctionalFilterComparator;
import com.lela.commons.service.DepartmentService;
import com.lela.commons.service.FunctionalFilterService;
import com.lela.commons.service.ItemService;
import com.lela.commons.service.ProductEngineService;
import com.lela.commons.service.UserService;
import com.lela.domain.document.FunctionalFilter;
import com.lela.domain.document.Item;
import com.lela.domain.document.Motivator;
import com.lela.domain.document.RelevantItem;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.CategoryItemsQuery;
import com.lela.domain.dto.ItemPage;
import com.lela.domain.dto.department.DepartmentLandingPage;
import com.lela.domain.dto.department.DepartmentLandingPageData;
import com.lela.domain.dto.department.DepartmentQuery;
import com.lela.domain.dto.department.DepartmentSearchResult;
import com.lela.domain.dto.department.DepartmentSearchResults;
import com.lela.domain.enums.FunctionalSortType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 11/1/12
 * Time: 4:52 PM
 * Responsibility: Handles all functionality related to department pages
 */
@Service("departmentService")
public class DepartmentServiceImpl implements DepartmentService {
    private final FunctionalFilterService functionalFilterService;
    private final ItemService itemService;
    private final ProductEngineService productEngineService;
    private final UserService userService;

    @Autowired
    public DepartmentServiceImpl(FunctionalFilterService functionalFilterService,
                                 ItemService itemService,
                                 ProductEngineService productEngineService,
                                 UserService userService) {
        this.functionalFilterService = functionalFilterService;
        this.itemService = itemService;
        this.productEngineService = productEngineService;
        this.userService = userService;
    }

    @Override
    public Long findFunctionalFilterCountByCategoryGroupUrlName(String categoryGroupUrlName) {
        return functionalFilterService.findFunctionalFilterCountByUrlName(categoryGroupUrlName);
    }

    @Override
    public DepartmentLandingPage findDepartmentLandingPage(String categoryGroupUrlName, String userCode) {
        DepartmentLandingPage result = new DepartmentLandingPage();

        if (userCode != null){
        	productEngineService.resetFunctionalFiltersForCategoryGroupAndUser(categoryGroupUrlName, userCode);
        }
        
        // retrieve filters for this category group
        List<FunctionalFilter> filters = functionalFilterService.findFunctionalFiltersByUrlName(categoryGroupUrlName);

        // sort filters by order
        if (filters != null && !filters.isEmpty()) {
            Collections.sort(filters, new FunctionalFilterComparator());
            result.setFilters(filters);
        }

        /*
        // retrieve categories for this category group
        List<Category> categories = categoryService.findCategoriesByCategoryGroupUrlName(categoryGroupUrlName);

        // sort categories by order
        if (categories != null && !categories.isEmpty()) {
            Collections.sort(categories, new CategoryOrderComparator());
            result.setCategories(categories);
        }
        */

        return result;
    }

    /**
     * Returns aggregate results based on categories and filter options user has made
     * @param query query
     * @return
     */
    @Override
    public DepartmentLandingPageData findDepartmentLandingPageData(DepartmentQuery query) {
        DepartmentLandingPageData result = new DepartmentLandingPageData(query);

        // if categories are empty it means we need to include all categories under Department
        if (query.getCategoryUrlNames() == null || query.getCategoryUrlNames().isEmpty()) {
            query.setCategoryUrlNames(query.getAvailableCategoryUrlNames());
        }

        // find the category filter
        List<String> categories = query.findCategoriesFromFilter();

        if (categories == null || categories.isEmpty()) {
            categories = query.getAvailableCategoryUrlNames();
        }

        // we have to make inefficient calls here because business wants result counts for every category
//        Map<String, Long> categoryCount = new HashMap<String, Long>(query.getCategoryUrlNames().size());
        Map<String, Long> categoryCount = new HashMap<String, Long>(categories.size());

        // functional filters for department
        List<FunctionalFilter> filters = functionalFilterService.findFunctionalFiltersByUrlName(query.getRlnm());

        Long count = 0L;
        for (String categoryUrlName : categories) {
            Long cCount = itemService.findItemCountByCategoryAndFilters(categoryUrlName, filters, query.getFilters());

            // result count for a category
            categoryCount.put(categoryUrlName, cCount);

            // total count
            count += cCount;
        }
        result.setCategoryCount(categoryCount);
        result.setCount(count);

        return result;
    }

    /**
     * This method will use the department query to retrieve search results from all the
     * categories under this department
     * @param query query
     * @return DepartmentSearchResults
     */
    @Override
    public DepartmentSearchResults findDepartmentSearchResults(String userCode, DepartmentQuery query) {
        DepartmentSearchResults result = new DepartmentSearchResults(query);

        // if categories are empty it means we need to include all categories under Department
        if (query.getCategoryUrlNames() == null || query.getCategoryUrlNames().isEmpty()) {
            query.setCategoryUrlNames(query.getAvailableCategoryUrlNames());
        }

        List<String> categories = query.getCategoryUrlNames();

        // grab the user supplement for this user
        UserSupplement us = userService.findUserSupplement(userCode);
        Motivator motivator = us.getMotivator();
        List<DepartmentSearchResult> list = new ArrayList<DepartmentSearchResult>(categories.size());
        Long totalItemCount = 0L;

        // loops through all categories and get the results from the product engine
        for (String categoryUrlName : categories) {
            DepartmentSearchResult dsr = new DepartmentSearchResult();
            CategoryItemsQuery ciq = new CategoryItemsQuery();
            ciq.setUserCode(userCode);
            ciq.setCategoryUrlName(categoryUrlName);
            ciq.setPage(0);
            ciq.setSize(4);
            ciq.setFilters(query.getFilters());
            ciq.setSort(true);
            // this just means we don't these settings to be saved as presets for the user
            ciq.setUpdate(true);

            // decide whether to use recommended results or popular
            if (motivator != null) {
                ciq.setSortBy(FunctionalSortType.RECOMMENDED);
                ItemPage<RelevantItem> relevantItems = productEngineService.findRelevantItemsByCategory(ciq);
                dsr.setRelevantItems(relevantItems);
                if (relevantItems.getTotalElements() > 0) {
                    dsr.setCategory(relevantItems.getContent().get(0).getCtgry());
                    totalItemCount += relevantItems.getTotalElements();
                }
            } else {
                ciq.setSortBy(FunctionalSortType.POPULARITY);
                ItemPage<Item> items = productEngineService.findItemsByCategory(ciq);
                dsr.setItems(items);
                if (items.getTotalElements() > 0) {
                    dsr.setCategory(items.getContent().get(0).getCtgry());
                    totalItemCount += items.getTotalElements();
                }
            }

            list.add(dsr);
        }

        result.setCategories(list);
        result.setCount(totalItemCount);

        return result;
    }

}
