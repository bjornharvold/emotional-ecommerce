/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.domain.document.Item;
import com.lela.domain.dto.search.FacetSearchQuery;
import com.lela.domain.dto.search.GroupedSearchResult;
import com.lela.domain.dto.search.ItemSearchQuery;
import com.lela.domain.dto.search.ItemsForOwnerSearchQuery;
import com.lela.domain.dto.search.ItemsInStoreSearchQuery;
import com.lela.domain.dto.search.NameValueAggregate;
import com.lela.domain.dto.search.SearchResult;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 2/15/12
 * Time: 10:58 PM
 * Responsibility:
 */
public interface SearchService {

    public void indexItem(Item item) throws SearchException;

    public void indexItems(List<Item> list) throws SearchException;

    void deleteItemsByQuery(String query) throws SearchException;

    void deleteAll() throws SearchException;

    void deleteItemsById(List<String> ids) throws SearchException;

    SearchResult searchForItems(ItemSearchQuery query) throws SearchException;

    boolean isItemCoreSolrServerAvailable();

    void deleteItemByUrlName(String urlName) throws SearchException;

    void deleteItemsByUrlName(List<String> urlNames) throws SearchException;

    GroupedSearchResult searchForItemsInStore(ItemsInStoreSearchQuery query) throws SearchException;

    GroupedSearchResult searchForItemsForOwner(ItemsForOwnerSearchQuery query) throws SearchException;

    SearchResult searchForItems(ItemSearchQuery query, boolean dismax) throws SearchException;

    SearchResult searchForCategoryItemsInStore(ItemsInStoreSearchQuery query) throws SearchException;

    SearchResult searchForCategoryItemsForOwner(ItemsForOwnerSearchQuery query) throws SearchException;

    List<NameValueAggregate> searchForFacetAggregates(FacetSearchQuery query) throws SearchException;
}
