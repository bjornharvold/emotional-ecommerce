/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import com.lela.commons.repository.DealRepository;
import com.lela.commons.service.CacheService;
import com.lela.commons.service.DealService;
import com.lela.domain.ApplicationConstants;
import com.lela.domain.document.Deal;
import com.lela.domain.enums.CacheType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 11/11/11
 * Time: 10:04 PM
 * Responsibility:
 */
@Service("dealService")
public class DealServiceImpl extends AbstractCacheableService implements DealService {
    private final DealRepository dealRepository;

    @Autowired
    public DealServiceImpl(CacheService cacheService, DealRepository dealRepository) {
        super(cacheService);
        this.dealRepository = dealRepository;
    }

    @Override
    public List<Deal> findDealsForStores(List<String> storeUrlNames) {
        List<Deal> result = null;

        if (storeUrlNames != null && !storeUrlNames.isEmpty()) {
            for (String urlName : storeUrlNames) {
                List<Deal> deals = findDealsForStore(urlName);

                if (deals != null && !deals.isEmpty()) {
                    if (result == null) {
                        result = new ArrayList<Deal>();
                    }
                    result.addAll(deals);
                }
            }
        }

        return result;
    }

    private List<Deal> findDealsForStore(String urlName) {
        List<Deal> deals = null;

        Cache.ValueWrapper wrapper = retrieveFromCache(ApplicationConstants.DEAL_CACHE, urlName);

        if (wrapper != null && wrapper.get() != null && wrapper.get() instanceof Deal) {
            deals = (List<Deal>) wrapper.get();
        } else {
            deals = dealRepository.findDealsForStore(urlName);

            if (deals != null) {

                putInCache(ApplicationConstants.DEAL_CACHE, urlName, deals);
            }
        }
        return deals;
    }

    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public List<Deal> removeAndSave(List<Deal> deals) {
        // first we remove all the deals
        dealRepository.deleteAll();

        // then resave the new ones
        return saveDeals(deals);
    }

    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public Deal removeAndSave(Deal deal) {
        // first we remove all the deals
        dealRepository.delete(deal.getId());

        // then resave the new ones
        return saveDeal(deal);
    }

    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public List<Deal> saveDeals(List<Deal> deals) {
        deals = (List<Deal>) dealRepository.save(deals);

        if (deals != null && !deals.isEmpty()) {
            Map<String, String> map = new HashMap<String, String>();

            // get unique stores
            for (Deal deal : deals) {
                map.put(deal.getRlnm(), deal.getRlnm());
            }

            for (String storeUrlName : map.keySet()) {
                removeFromCache(CacheType.DEAL, storeUrlName);
            }

        }

        return deals;
    }

    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public Deal saveDeal(Deal deal) {
        deal = dealRepository.save(deal);

        removeFromCache(CacheType.DEAL, deal.getRlnm());

        return deal;
    }

    @PreAuthorize("hasAnyRole('RIGHT_DELETE_APPLICATION')")
    @Override
    public void removeDealsForStore(String urlName) {
        List<Deal> deals = findDealsForStore(urlName);

        if (deals != null && !deals.isEmpty()) {
            dealRepository.delete(deals);

            removeFromCache(CacheType.DEAL, urlName);
        }
    }
}
