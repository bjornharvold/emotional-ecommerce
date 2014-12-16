package com.lela.commons.service;

import com.lela.domain.Cacheable;
import com.lela.domain.enums.CacheType;
import org.springframework.cache.Cache;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 4/20/12
 * Time: 1:29 AM
 * Responsibility:
 */
public interface CacheService {
	
	Cacheable retrieveCacheable(String cacheName, String cacheKey); 
	
	List<? extends Cacheable> retrieveCacheableList(String cacheName, String cacheKey);
	
    Cache.ValueWrapper retrieveFromCache(String cacheName, String cacheKey);

    void putInCache(String cacheName, String cacheKey, Object cachedObject);

    void removeFromCache(CacheType cacheType, String cacheKey);

    void removeFromCache(CacheType cacheType, List<String> cacheKeys);

    void removeAllFromCache(CacheType cacheType);
    
    String getFromCache(String cacheName, String cacheKey);
}
