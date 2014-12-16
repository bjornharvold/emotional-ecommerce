/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.domain.enums.CacheType;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 11/23/11
 * Time: 4:22 AM
 * Responsibility:
 */
public interface DistributedCacheEvictionService {
    void evict(CacheType cacheType, String key);
    void evict(CacheType cacheType, List<String> keys);

    void evictAll(CacheType cacheType);
}
