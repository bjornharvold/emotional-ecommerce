/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.domain.document.FunctionalFilter;
import com.lela.domain.document.Item;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 6/1/12
 * Time: 8:17 PM
 * Responsibility:
 */
public interface ItemService {
    Item findItemByUrlName(String urlName);
    List<Item> findItemsByCategoryUrlName(String categoryUrlName);
    List<Item> findItemsByCategoryUrlName(String categoryUrlName, int page, int maxResults, List<String> extraFields);
    Item saveItem(Item item);
    List<Item> saveItems(List<Item> list);
    Item removeItem(String rlnm);
    List<Item> findItemsByOwnerName(String ownerName);
    List<Item> findItemsByUrlName(List<String> itemUrlNames);
    List<Item> findItemsByStoreAndCategoryUrlNames(String storeUrlName, String categoryUrlName);
    List<Item> findItemsByOwnerAndCategoryUrlNames(String ownerUrlName, String categoryUrlName);
    Long findItemCountByCategoryUrlName(String categoryUrlName);
    Map<String, List<Item>> paginateThroughAllItemsPerCategories(List<String> categorUrlNames, Integer maxResults);
    List<Item> paginateThroughAllItemsPerCategory(String categoryUrlName, Integer maxResults);
    List<Item> findItemsByCategoryUrlName(String categoryUrlName, int page, int maxResults);
    List<Item> paginateThroughAllItemsPerCategoryIncludingStores(String categoryUrlName, Integer maxResults);
    void updateAvailableStoresOnItem(Item item);
    Item findItemById(ObjectId id);
    List<Item> findUnavailableItemIdsByCategoryUrlName(String categoryUrlName);
    Long findItemCountByCategoryAndFilters(String categoryUrlName, List<FunctionalFilter> functionalFilters, Map<String, Map<String, String>> filters);
}
