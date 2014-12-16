/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Service;

import com.lela.commons.service.LocalCacheEvictionService;
import com.lela.domain.ApplicationConstants;
import com.lela.domain.dto.CacheEviction;
import com.lela.domain.dto.CacheEvictions;
import com.lela.domain.enums.CacheType;
import com.lela.util.utilities.jackson.CustomObjectMapper;
import com.polarrose.amazon.sqs.SqsMessage;

/**
 * Created by Bjorn Harvold
 * Date: 11/23/11
 * Time: 3:45 AM
 * Responsibility:
 */
@Service("localCacheEvictionService")
public class LocalCacheEvictionServiceImpl implements LocalCacheEvictionService {
    private static final Logger log = LoggerFactory.getLogger(LocalCacheEvictionServiceImpl.class);
    private static final Logger MESSAGE_LOG = LoggerFactory.getLogger("SQS_RECEIVED");

    private final static ObjectMapper mapper = new CustomObjectMapper();

    private final EhCacheCacheManager cacheManager;
    
    private static String hostName = "UNKNOWN";
    static {
    	try {
    		hostName = java.net.InetAddress.getLocalHost().getHostName();
    	} catch (Exception e){
    		//Ignore;
    	}
    }
    
    @Autowired
    public LocalCacheEvictionServiceImpl(EhCacheCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void handle(SqsMessage message) {
        try {
            log.info("Preparing to handle message on Host:" + hostName + ", messageId:" + message.getId() + ", messageBody:" + message.getBody());

            if (MESSAGE_LOG.isInfoEnabled()) {
                MESSAGE_LOG.info(message.getBody());
            }
            
            if (StringUtils.isNotBlank(message.getBody())) {
                CacheEvictions dto = mapper.readValue(message.getBody(), CacheEvictions.class);

                if (dto != null && dto.getList() != null && !dto.getList().isEmpty()) {
                    for (CacheEviction ce : dto.getList()) {
                    	this.invokeCacheRemovalBasedOnCacheType(ce.getType(), ce.getKey());
                    }
                } else {
                    if (log.isWarnEnabled()) {
                        log.warn("Nothing to do");
                    }
                }
            } else {
                if (log.isWarnEnabled()) {
                    log.warn("Sqs message body is empty");
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void evictProductGrid(String key) {
        removeFromCache(ApplicationConstants.PRODUCT_GRID_CACHE, key);
    }

    @Override
    public void evictDeal(String key) {
        removeFromCache(ApplicationConstants.DEAL_CACHE, key);
    }

    @Override
    public void evictCssStyle(String key) {
        removeFromCache(ApplicationConstants.CSS_STYLE_CACHE, key);
    }

    @Override
    public void evictUserSupplement(String key) {
        removeFromCache(ApplicationConstants.USER_SUPPLEMENT_CACHE, key);
    }

    @Override
    public void evictUser(String key) {
        removeFromCache(ApplicationConstants.USER_CACHE, key);
    }

    @Override
    public void evictStoreSeo(String key) {
        removeFromCache(ApplicationConstants.STORE_SEO_CACHE, key);
    }

    @Override
    public void evictComputedCategory(String key) {
        removeFromCache(ApplicationConstants.COMPUTED_CATEGORY_CACHE, key);
    }

    @Override
    public void evictAllComputedCategory() {
        removeAllFromCache(ApplicationConstants.COMPUTED_CATEGORY_CACHE);
    }

    @Override
    public void evictOwnerSeo(String key) {
        removeFromCache(ApplicationConstants.OWNER_SEO_CACHE, key);
    }

    @Override
    public void evictApplication(String key) {
        removeFromCache(ApplicationConstants.APPLICATION_CACHE, key);
    }

    @Override
    public void evictQuiz(String key) {
        removeFromCache(ApplicationConstants.QUIZ_CACHE, key);
    }

    @Override
    public void evictPress(String key) {
        removeFromCache(ApplicationConstants.PRESS_CACHE, key);
    }

    @Override
    public void evictSeo(String key) {
        removeFromCache(ApplicationConstants.SEO_CACHE, key);
    }

    @Override
    public void evictNavigationBar(String key) {
        removeFromCache(ApplicationConstants.NAVIGATION_BAR_CACHE, key);
    }

    @Override
    public void evictStore(String key) {
        removeFromCache(ApplicationConstants.STORE_CACHE, key);

        // we also need to remove all seo caches
        removeFromCache(ApplicationConstants.STORE_SEO_CACHE, ApplicationConstants.STORE_SEO_CACHE_KEY);
    }

    @Override
    public void evictRole(String key) {
        removeFromCache(ApplicationConstants.ROLE_CACHE, key);
    }

    @Override
    public void evictQuestion(String key) {
        removeFromCache(ApplicationConstants.QUESTION_CACHE, key);
    }

    @Override
    public void evictOffer(String key) {
        removeFromCache(ApplicationConstants.OFFER_CACHE, key);
    }

    @Override
    public void evictOwner(String key) {
        removeFromCache(ApplicationConstants.OWNER_CACHE, key);

        // we also need to remove all seo caches
        removeFromCache(ApplicationConstants.OWNER_SEO_CACHE, ApplicationConstants.OWNER_SEO_CACHE_KEY);
    }

    @Override
    public void evictStaticContent(String key) {
        removeFromCache(ApplicationConstants.STATIC_CONTENT_CACHE, key);
    }

    @Override
    public void evictEvent(String key) {
        removeFromCache(ApplicationConstants.EVENT_CACHE, key);
    }

    @Override
    public void evictCampaign(String key) {
        removeFromCache(ApplicationConstants.CAMPAIGN_CACHE, key);
    }

    @Override
    public void evictAffiliateAccount(String key) {
        removeFromCache(ApplicationConstants.AFFILIATE_ACCOUNT_CACHE, key);

        removeAllFromCache(ApplicationConstants.DOMAIN_CACHE);
    }

    @Override
    public void evictItem(String key) {
        removeFromCache(ApplicationConstants.ITEM_CACHE, key);
    }

    @Override
    public void evictItemCategory(String key) {
        removeFromCache(ApplicationConstants.ITEM_CATEGORY_CACHE, key);
    }

    @Override
    public void evictFunctionalFilter(String key) {
        removeFromCache(ApplicationConstants.FUNCTIONAL_FILTER_CACHE, key);
    }

    @Override
    public void evictClientDetails(String key) {
        removeFromCache(ApplicationConstants.CLIENT_DETAILS_CACHE, key);
    }

    @Override
    public void evictCategory(String key) {
        // first just remove the category
        removeFromCache(ApplicationConstants.CATEGORY_CACHE, key);

        // remove the category from the list of categories
        removeFromCache(ApplicationConstants.CATEGORY_CACHE, ApplicationConstants.CATEGORIES_KEY);

        // remove the category items cache
        evictItemCategory(key);

        // Flush the navbars
        removeAllFromCache(ApplicationConstants.NAVIGATION_BAR_CACHE);
    }

    /**
     * Returns a collection of all our configured caches
     *
     * @return collection of cache names
     */
    @Override
    public Collection<String> getCacheNames() {
        return cacheManager.getCacheNames();
    }

    /**
     * Method description
     *
     * @param cacheName cacheName
     * @param cacheKey  cacheKey
     */

    public void removeFromCache(String cacheName, String cacheKey) {
        Cache cache = cacheManager.getCache(cacheName);

        if (cache != null) {
            if (cache.get(cacheKey) != null) {
                cache.evict(cacheKey);
                log.debug("Removed from cache: " + cacheName + ", key: " + cacheKey);
            } else {
                if (log.isInfoEnabled()) {
                    log.info("Object not cached. Cannot remove for key: " + cacheKey);
                }
            }
        } else {
            if (log.isInfoEnabled()) {
                log.info("Cache with name: " + cacheName + " does not exist.");
            }
        }
    }

    /**
     * Removes every element for the specific cache
     *
     * @param cacheName cacheName
     */
    public void removeAllFromCache(String cacheName) {

        log.info("Remove all from cache: " + cacheName);
        Cache cache = cacheManager.getCache(cacheName);

        if (cache != null) {
            net.sf.ehcache.Cache ehcache = (net.sf.ehcache.Cache) cache.getNativeCache();
            ehcache.removeAll();
        } else {
            if (log.isInfoEnabled()) {
                log.info("Cache with name: " + cacheName + " does not exist.");
            }
        }
    }
    
    @Override
    public void removeFromCache(CacheType cacheType, String cacheKey){
    	invokeCacheRemovalBasedOnCacheType(cacheType, cacheKey);
    }

    @Override
    public void removeFromCache(CacheType cacheType, List<String> cacheKeyList){ 
    	for (String key : cacheKeyList) {
    		invokeCacheRemovalBasedOnCacheType(cacheType, key);
		}
    }
    
    @Override 
    public void removeAllFromCache(CacheType cacheType){
    	invokeCacheRemovalBasedOnCacheType(cacheType, null);
    }  
    
    private void invokeCacheRemovalBasedOnCacheType(CacheType type, String key){
        if (type != null && StringUtils.isNotBlank(key)) {
            switch (type) {
                case CATEGORY:
                    evictCategory(key);
                    break;
                case CLIENT_DETAILS:
                    evictClientDetails(key);
                    break;
                case FUNCTIONAL_FILTER:
                    evictFunctionalFilter(key);
                    break;
                case ITEM:
                    evictItem(key);
                    break;
                case ITEM_CATEGORY:
                    evictItemCategory(key);
                    break;
                case OFFER:
                    evictOffer(key);
                    break;
                case OWNER:
                    evictOwner(key);
                    break;
                case QUESTION:
                    evictQuestion(key);
                    break;
                case ROLE:
                    evictRole(key);
                    break;
                case STORE:
                    evictStore(key);
                    break;
                case STATIC_CONTENT:
                    evictStaticContent(key);
                    break;
                case EVENT:
                    evictEvent(key);
                    break;
                case CAMPAIGN:
                    evictCampaign(key);
                    break;
                case AFFILIATE_ACCOUNT:
                    evictAffiliateAccount(key);
                    break;
                case NAVIGATION_BAR:
                    evictNavigationBar(key);
                    break;
                case SEO:
                    evictSeo(key);
                    break;
                case PRESS:
                    evictPress(key);
                    break;
                case QUIZ:
                    evictQuiz(key);
                    break;
                case APPLICATION:
                    evictApplication(key);
                    break;
                case STORE_SEO:
                    evictStoreSeo(key);
                    break;
                case OWNER_SEO:
                    evictOwnerSeo(key);
                    break;
                case COMPUTED_CATEGORY:
                    evictComputedCategory(key);
                    break;
                case USER_SUPPLEMENT:
                    evictUserSupplement(key);
                    break;
                case PRODUCT_GRID:
                    evictProductGrid(key);
                    break;
                case USER:
                    evictUser(key);
                    break;
                case DEAL:
                    evictDeal(key);
                    break;
                case CSS_STYLE:
                    evictCssStyle(key);
                    break;
                default:
                    if (log.isWarnEnabled()) {
                        log.warn("Does not support type: " + type);
                    }
            }
        } else if (type != null) {
            switch (type) {
                case COMPUTED_CATEGORY:
                    evictAllComputedCategory();
                    break;
                case ITEM:
                    this.removeAllFromCache(ApplicationConstants.ITEM_CACHE);
                    break;
                case FUNCTIONAL_FILTER:
                    this.removeAllFromCache(ApplicationConstants.FUNCTIONAL_FILTER_CACHE);
                    break;                    
                default:
                    if (log.isWarnEnabled()) {
                        log.warn("Does not support type: " + type);
                    }
            }
        } else {
            if (log.isWarnEnabled()) {
                log.warn("The cache eviction is missing a cache type");
            }
        }
    }
}
