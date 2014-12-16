/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.domain.document.Store;
import com.lela.domain.dto.search.NameValueAggregate;
import com.lela.domain.dto.store.StoreAggregateQuery;
import com.lela.domain.dto.store.StoresSearchResult;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 4/27/12
 * Time: 1:03 AM
 * Responsibility:
 */
public interface StoreService {
    List<NameValueAggregate> findStoreAggregates(StoreAggregateQuery saq);
    StoresSearchResult findStoreAggregateDetails(StoreAggregateQuery saq);
    List<Store> saveStores(List<Store> list);
    Store saveStore(Store store);
    Store findStoreByUrlName(String urlName);
    Store findStoreByAffiliateUrlName(String affiliateUrlName);
    Store removeStore(String rlnm);
    List<Store> findStoresByUrlName(List<String> storeUrlNames);
    Store findStoreByMerchantId(String merchantId);
    List<Store> findStoresByMerchantIds(List<String> merchantIds);
    Page<Store> findStores(Integer page, Integer maxResults);
    Page<Store> findStores(Integer page, Integer maxResults, String firstLetter);
    Long findStoreCount();
    List<Store> findAllStores(Integer chunk);
    List<Store> findStoreUrlNamesByMerchantIds(List<String> merchantIds);

    List<Store> findStores(List<String> fields);
}
