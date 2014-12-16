/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import com.lela.domain.ApplicationConstants;
import com.lela.commons.repository.ComputedCategoryRepository;
import com.lela.commons.service.CacheService;
import com.lela.commons.service.ComputedCategoryService;
import com.lela.domain.document.ComputedCategory;
import com.lela.domain.enums.CacheType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 8/4/12
 * Time: 7:35 PM
 * Responsibility:
 */
@Service("computedCategoryService")
public class ComputedCategoryServiceImpl extends AbstractCacheableService implements ComputedCategoryService {

    private final ComputedCategoryRepository computedCategoryRepository;

    @Autowired
    public ComputedCategoryServiceImpl(CacheService cacheService,
                                       ComputedCategoryRepository computedCategoryRepository) {
        super(cacheService);
        this.computedCategoryRepository = computedCategoryRepository;
    }

    @Override
    public ComputedCategory findComputedCategory(String userCode, String categoryUrlName) {
        ComputedCategory result = null;

        String urlName = userCode + "|" + categoryUrlName;

        Cache.ValueWrapper wrapper = retrieveFromCache(ApplicationConstants.COMPUTED_CATEGORY_CACHE, urlName);

        if (wrapper != null && wrapper.get() != null && wrapper.get() instanceof ComputedCategory) {
            result = (ComputedCategory) wrapper.get();
        } else {
            result = computedCategoryRepository.findComputedCategory(userCode, categoryUrlName);

            if (result != null) {
                putInCache(ApplicationConstants.COMPUTED_CATEGORY_CACHE, urlName, result);
            }
        }

        return result;
    }

    @Override
    public void removeComputedCategory(String userCode, String categoryUrlName) {
        ComputedCategory cc = findComputedCategory(userCode, categoryUrlName);

        if (cc != null) {
            computedCategoryRepository.delete(cc);

            // remove from cache
            String urlName = userCode + "|" + categoryUrlName;
            removeFromCache(CacheType.COMPUTED_CATEGORY, urlName);
        }
    }

    /**
     * This method should be called after item ingestion as it is possible a recompute of user matches
     * needs to take place.
     */
    @Override
    public void setComputedCategoryToDirty(String userCode, String categoryUrlName) {
        ComputedCategory cc = findComputedCategory(userCode, categoryUrlName);

        if (cc != null) {
            // set dirty flag
            cc.setDrty(true);
            computedCategoryRepository.save(cc);

            // remove from cache
            String urlName = userCode + "|" + categoryUrlName;
            removeFromCache(CacheType.COMPUTED_CATEGORY, urlName);
        }
    }

    /**
     * This method should be called after item ingestion as it is possible a recompute of user matches
     * needs to take place.
     */
    @Override
    public void setComputedCategoriesToDirty(String userCode) {
        List<ComputedCategory> list = findComputedCategories(userCode);

        if (list != null && !list.isEmpty()) {
            for (ComputedCategory cc : list) {
                cc.setDrty(true);

                // remove from cache
                String urlName = userCode + "|" + cc.getRlnm();
                removeFromCache(CacheType.COMPUTED_CATEGORY, urlName);
            }

            computedCategoryRepository.save(list);
        }
    }

    /**
     * This method should be called after item ingestion as it is possible a recompute of user matches
     * needs to take place.
     * We want to clear out ALL categories and then flush the cache of everything
     */
    @Override
    public void setAllComputedCategoriesToDirty() {
        // set the dirty flag
        computedCategoryRepository.setAllComputedCategoriesToDirty();

        // clear the cache - if we don't the user won't see the dirty flags
        removeAllFromCache(CacheType.COMPUTED_CATEGORY);
    }

    @Override
    public void setAllComputedCategoryToDirty(String categoryUrlName) {
        // set the dirty flag
        computedCategoryRepository.setAllComputedCategoryToDirty(categoryUrlName);

        // clear the cache - if we don't the user won't see the dirty flags
        removeFromCache(CacheType.COMPUTED_CATEGORY, categoryUrlName);
    }

    @Override
    public ComputedCategory saveComputedCategory(ComputedCategory cc) {
        return computedCategoryRepository.save(cc);
    }

    private List<ComputedCategory> findComputedCategories(String userCode) {
        return computedCategoryRepository.findComputedCategories(userCode);
    }
}
