/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Service;

import com.lela.commons.LelaException;
import com.lela.commons.service.CacheService;
import com.lela.commons.service.DistributedCacheEvictionService;
import com.lela.commons.service.LocalCacheEvictionService;
import com.lela.domain.Cacheable;
import com.lela.domain.enums.CacheType;
import com.lela.util.utilities.jackson.CustomObjectMapper;

/**
 * Created by Bjorn Harvold
 * Date: 4/20/12
 * Time: 1:27 AM
 * Responsibility:
 */
@Service("cacheService")
public class CacheServiceImpl implements CacheService {
    private final static Logger log = LoggerFactory.getLogger(CacheServiceImpl.class);
    private final EhCacheCacheManager cacheManager;
    private final static ObjectMapper mapper = new CustomObjectMapper();
    

    /**
     * Sends SQS message to evict cache on app servers
     */
    private final DistributedCacheEvictionService distributedCacheEvictionService;
    private final LocalCacheEvictionService localCacheEvictionService;
    
    @Autowired
    public CacheServiceImpl(EhCacheCacheManager cacheManager, DistributedCacheEvictionService distributedCacheEvictionService, LocalCacheEvictionService localCacheEvictionService) {
        this.cacheManager = cacheManager;
        this.distributedCacheEvictionService = distributedCacheEvictionService;;
        this.localCacheEvictionService = localCacheEvictionService;
    }

    /**
     * This method returns a <code>Cacheable</code> object after ensuring that each is valid
     * 
     * When the ehCache runs out of memory, cached elements are partially returned because 
     * soft references are held by the cache and since memory runs out, the soft references end up returning 
     * null values.
     * 
     * This method ensures that each Cacheable element meets the condition implemented in the <code>validateCacheable</code>
     * method of the Cacheable implementation.
     * 
     */
    @Override
    public Cacheable retrieveCacheable(String cacheName, String cacheKey) {
    	Cacheable v = null;
    	boolean invalid = false;
    	Cache.ValueWrapper wrapper = this.retrieveFromCache(cacheName, cacheKey);
    	if (wrapper != null & wrapper.get() != null){
    		Object o = wrapper.get();
            if (o instanceof Cacheable){
    			if (!((Cacheable)o).validateCacheable()){
    				invalid = true;
    			}
    		}
    		if (!invalid){
    			v = (Cacheable)wrapper.get();
    		} else {
    			v = null;
    			log.warn(String.format("The cache has got corrupt for cache name %s and cache key %s. " +
    					"This could happen because the cache memory is low. Try increasing the value of " +
    					"maxBytesLocalHeap in ehcache.xml.", cacheName, cacheKey));
    		}
    		
    	}
    	return v;
    }
    
    /**
     * This method returns a List of <code>Cacheable</code> objects after ensuring that each is valid
     * 
     * When the ehCache runs out of memory, cached elements are partially returned because 
     * soft references are held by the cache and since memory runs out, the soft references end up returning 
     * null values.
     * 
     * This method ensures that each Cacheable element in the returned List meets the condition implemented in the <code>validateCacheable</code>
     * method of the Cacheable implementation.
     */
    @Override
    public List<? extends Cacheable> retrieveCacheableList(String cacheName, String cacheKey) {

    	List<Cacheable> v = null;
    	boolean invalid = false;
    	Cache.ValueWrapper wrapper = this.retrieveFromCache(cacheName, cacheKey);
    	if (wrapper != null & wrapper.get() != null){
    		Object o = wrapper.get();
    		if (o instanceof List<?>){
    			List<Cacheable> l = (List<Cacheable>)o;
    			for (Cacheable val : l) {
					if (!val.validateCacheable()) {
						invalid = true;
						break;
					}
				}
    		} 
    		if (!invalid){
    			v = (List<Cacheable>)wrapper.get();
    		} else {
    			v = null;
    			log.warn(String.format("The cache has got corrupt for cache name %s and cache key %s. " +
    					"This could happen because the cache memory is low. Try increasing the value of " +
    					"maxBytesLocalHeap in ehcache.xml.", cacheName, cacheKey));
    		}
    		
    	}
    	return v;
    }
    @Override
    public Cache.ValueWrapper retrieveFromCache(String cacheName, String cacheKey) {
        Cache.ValueWrapper result = null;
        Cache              cache  = cacheManager.getCache(cacheName);

        // try to retrieve from cache
        if (cache != null) {
            Cache.ValueWrapper wrapper = cache.get(cacheKey);

            if ((wrapper != null) && (wrapper.get() != null)) {
                result = wrapper;
            }
        } else {
            log.warn("Cache with name: " + cacheName + " does not exist.");
        }

        return result;
    }

    @Override
    public void putInCache(String cacheName, String cacheKey, Object cachedObject) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            if ((cachedObject != null) && (cachedObject instanceof Serializable)) {
                cache.put(cacheKey, cachedObject);
            } else {
                log.warn("Cache object is either null or does not implement Serializable: " + cachedObject);
            }
        } else {
            log.warn("Cache with name: " + cacheName + " does not exist.");
        }
    }
    
    @Override
    public void removeFromCache(CacheType cacheType, String cacheKey) {
        try { 
        	//Make the call to the local eviction service to prevent latency
        	localCacheEvictionService.removeFromCache(cacheType, cacheKey); 
            distributedCacheEvictionService.evict(cacheType, cacheKey);
        } catch (Exception e) {
            // Don't allow cache operation to fail 
            log.error("Failure on cache evict", e);
        }
    }

    @Override
    public void removeFromCache(CacheType cacheType, List<String> cacheKeys) {
        try {
        	//Make the call to the local eviction service to prevent latency
        	localCacheEvictionService.removeFromCache(cacheType, cacheKeys); 
            distributedCacheEvictionService.evict(cacheType, cacheKeys);
        } catch (Exception e) {
            // Don't allow cache operation to fail
            log.error("Failure on cache evict", e);
        }
    }

    @Override
    public void removeAllFromCache(CacheType cacheType) {
        try {
        	//Make the call to the local eviction service to prevent latency
        	localCacheEvictionService.removeAllFromCache(cacheType); 
            distributedCacheEvictionService.evictAll(cacheType);
        } catch (Exception e) {
            // Don't allow cache operation to fail 
            log.error("Failure on cache evict", e);
        }
    }
    
    /**
     * Returns the cached value in JSON format if it exists in the cache, null if it does not exist
     */
    @Override
    public String getFromCache(String cacheName, String cacheKey) {   
        String result = "";
        Cache cache  = cacheManager.getCache(cacheName);

        // try to retrieve from cache
        if (cache != null) {
            Cache.ValueWrapper wrapper = cache.get(cacheKey);
            Object o = null;
            if ((wrapper != null) && (wrapper.get() != null)) {
            	try {
            		o =  wrapper.get();
            		result = mapper.writeValueAsString(o);
            	} catch (Exception e) {
            		throw new LelaException("Cannot convert " + o.toString());
            	}
            }
        } else {
            log.warn("Cache with name: " + cacheName + " does not exist.");
        }

        return result;
    }
    
}
