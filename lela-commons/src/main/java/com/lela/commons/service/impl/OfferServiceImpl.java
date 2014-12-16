/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import com.lela.domain.ApplicationConstants;
import com.lela.commons.repository.OfferRepository;
import com.lela.commons.service.CacheService;
import com.lela.commons.service.OfferService;
import com.lela.domain.document.Offer;
import com.lela.domain.enums.CacheType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 6/4/12
 * Time: 8:39 PM
 * Responsibility:
 */
@Service("offerService")
public class OfferServiceImpl extends AbstractCacheableService implements OfferService {
    private final OfferRepository offerRepository;

    @Autowired
    public OfferServiceImpl(CacheService cacheService, OfferRepository offerRepository) {
        super(cacheService);
        this.offerRepository = offerRepository;
    }

    /**
     * @param offer offer
     *
     * @return Return value
     */
    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public Offer saveOffer(Offer offer) {
        Offer result = offerRepository.save(offer);

        // Remove from cache
        removeFromCache(CacheType.OFFER, offer.getRlnm());

        return result;
    }

    /**
     * @param list list
     *
     * @return Return value
     */
    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public List<Offer> saveOffers(List<Offer> list) {
        List<Offer> result = (List<Offer>) offerRepository.save(list);

        // Remove from cache
        if (list != null && !list.isEmpty()) {
            List<String> cacheKeys = new ArrayList<String>(list.size());

            for (Offer item : list) {
                cacheKeys.add(item.getRlnm());
            }

            removeFromCache(CacheType.OFFER, cacheKeys);
        }

        return result;
    }

    @Override
    public Offer findOfferByUrlName(String urlName) {
        Offer result = null;
        Cache.ValueWrapper wrapper = retrieveFromCache(ApplicationConstants.OFFER_CACHE, urlName);

        if (wrapper != null && wrapper.get() != null && wrapper.get() instanceof Offer) {
            result = (Offer) wrapper.get();
        } else {
            result = offerRepository.findByUrlName(urlName);

            if (result != null) {
                putInCache(ApplicationConstants.OFFER_CACHE, urlName, result);
            }
        }

        return result;
    }

    @Override
    public List<Offer> findOffersByBranchUrlName(String branchUrlName) {
        return offerRepository.findByBranchUrlName(branchUrlName);
    }

    @Override
    public List<Offer> findValidOffersByBranchUrlName(String branchUrlName) {
        return offerRepository.findValidByBranchUrlName(branchUrlName);
    }

    @Override
    public Offer removeOffer(String rlnm) {
        Offer offer = offerRepository.findByUrlName(rlnm);

        if (offer != null) {
            offerRepository.delete(offer);

            // Remove from cache
            removeFromCache(CacheType.OFFER, offer.getRlnm());
        }

        return offer;
    }
}
