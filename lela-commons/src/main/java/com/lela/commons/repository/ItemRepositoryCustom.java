/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.FunctionalFilter;
import com.lela.domain.document.Item;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 1/6/12
 * Time: 2:48 AM
 * Responsibility:
 */
public interface ItemRepositoryCustom {
    void updateAvailableStoresOnItem(Item item);

    List<Item> findItemsByCategoryUrlName(String categoryUrlName, int page, int maxResults, List<String> extraFields);

    List<Item> findItemsByUrlName(List<String> itemUrlNames);

    List<Item> findByCategoryUrlName(String categoryUrlName);

    Long findCountByCategoriesAndFilters(String categoryUrlName, List<FunctionalFilter> functionalFilters, Map<String, Map<String, String>> filters);
}
