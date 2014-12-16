/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.domain.document.AbstractItem;
import com.lela.domain.document.Category;
import com.lela.domain.document.FunctionalFilter;
import com.lela.domain.document.Item;
import com.lela.domain.document.RelevantItem;
import com.lela.domain.document.User;
import com.lela.domain.document.UserAnswer;
import com.lela.domain.document.UserQuestion;
import com.lela.domain.dto.CategoryItemsQuery;
import com.lela.domain.dto.ItemPage;
import com.lela.domain.dto.RelevantItemQuery;
import com.lela.domain.dto.RelevantItemSearchQuery;
import com.lela.domain.dto.RelevantItemsForOwnerSearchQuery;
import com.lela.domain.dto.RelevantItemsInStoreSearchQuery;
import com.lela.domain.dto.RelevantOwnerItemsQuery;
import com.lela.domain.dto.productgrid.EnrichProductGridQuery;
import com.lela.domain.dto.quiz.QuizAnswers;
import com.lela.domain.dto.search.GroupedSearchResult;
import com.lela.domain.dto.search.ItemSearchQuery;
import com.lela.domain.dto.search.ItemsForOwnerSearchQuery;
import com.lela.domain.dto.search.ItemsInStoreSearchQuery;
import com.lela.domain.dto.search.SearchResult;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 7/14/11
 * Time: 10:11 PM
 * Responsibility: This service is responsible for matching users to products and interacting with users through questions.
 */
public interface ProductEngineService {
    ItemPage<Item> findItemsByCategory(CategoryItemsQuery query);

    ItemPage<RelevantItem> findRelevantItemsByCategory(CategoryItemsQuery query);

    RelevantItem findRelevantItem(RelevantItemQuery query);

    Integer computeTotalRelevancy(Map<String, Integer> motivatorRelevancy);

    Map<String, Integer> computeMotivatorRelevancy(Map<String, Integer> userMotivators, Map<String, Integer> itemMotivators);

    Map<String, List<RelevantItem>> findHighestMatchingItemPerCategory(List<Category> categories, String userCode, Integer page, Integer size);

    List<RelevantItem> findRelevantItemsByOwner(RelevantOwnerItemsQuery query);

    List<RelevantItem> retrievePreComputedUserMatches(List<Item> items, String userCode);

    RelevantItem retrievePreComputedUserMatch(Item item, String userCode);

    SearchResult searchForItemsUsingSolr(ItemSearchQuery query) throws SearchException;

    SearchResult searchForRelevantItemsUsingSolr(RelevantItemSearchQuery query) throws SearchException;

    GroupedSearchResult searchForItemsInStoreUsingSolr(ItemsInStoreSearchQuery query) throws SearchException;

    GroupedSearchResult searchForItemsForOwnerUsingSolr(ItemsForOwnerSearchQuery query) throws SearchException;

    GroupedSearchResult searchForRelevantItemsInStoreUsingSolr(RelevantItemsInStoreSearchQuery query) throws SearchException;

    GroupedSearchResult searchForRelevantItemsForOwnerUsingSolr(RelevantItemsForOwnerSearchQuery query) throws SearchException;

    SearchResult searchForCategoryItemsInStoreUsingSolr(ItemsInStoreSearchQuery query) throws SearchException;

    SearchResult searchForRelevantCategoryItemsInStoreUsingSolr(RelevantItemsInStoreSearchQuery query) throws SearchException;

    SearchResult searchForCategoryItemsForOwnerUsingSolr(ItemsForOwnerSearchQuery query) throws SearchException;

    SearchResult searchForRelevantCategoryItemsForOwnerUsingSolr(RelevantItemsForOwnerSearchQuery query) throws SearchException;

    List<AbstractItem> findSupplementaryItemsByItem(String userCode, AbstractItem item, Integer size);

    void answerQuestions(String userCode, QuizAnswers answers);
    
    void resetFunctionalFiltersForCategoryGroupAndUser(String categoryGroupUrl, String userCode);
}
