/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.domain.enums.CacheType;
import com.polarrose.amazon.spring.sqs.MessageHandler;

import java.util.Collection;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 11/23/11
 * Time: 4:22 AM
 * Responsibility:
 */
public interface LocalCacheEvictionService extends MessageHandler {
    void evictStore(String key);
    void evictRole(String key);
    void evictQuestion(String key);
    void evictOffer(String key);
    void evictOwner(String key);
    void evictItem(String key);
    void evictItemCategory(String key);
    void evictFunctionalFilter(String key);
    void evictClientDetails(String key);
    void evictCategory(String key);
    void evictStaticContent(String key);
    void evictEvent(String key);
    void evictCampaign(String key);
    void evictAffiliateAccount(String key);

    void evictNavigationBar(String key);

    void evictSeo(String key);

    Collection<String> getCacheNames();

    void evictPress(String key);

    void evictQuiz(String key);

    void evictApplication(String key);

    void evictStoreSeo(String key);

    void evictOwnerSeo(String key);

    void evictComputedCategory(String key);

    void evictAllComputedCategory();

    void evictUserSupplement(String key);

    void evictProductGrid(String key);
    void evictUser(String key);
    
    void removeFromCache(String cacheName, String cacheKey);
    void removeFromCache(CacheType cacheType, String cacheKey);
    void removeFromCache(CacheType cacheType, List<String> cacheKey);
    void removeAllFromCache(CacheType cacheType);
    void removeAllFromCache(String cacheName);

    void evictDeal(String key);

    void evictCssStyle(String key);
}
