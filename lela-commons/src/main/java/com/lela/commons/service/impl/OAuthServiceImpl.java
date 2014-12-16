/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import com.lela.domain.ApplicationConstants;
import com.lela.commons.repository.OAuthClientDetailsRepository;
import com.lela.commons.service.CacheService;
import com.lela.commons.service.OAuthService;
import com.lela.domain.document.OAuthClientDetail;
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
 * Time: 7:15 PM
 * Responsibility:
 */
@Service("oAuthService")
public class OAuthServiceImpl extends AbstractCacheableService implements OAuthService {
    /** Field description */
    private final OAuthClientDetailsRepository oAuthClientDetailsRepository;

    @Autowired
    public OAuthServiceImpl(CacheService cacheService, OAuthClientDetailsRepository oAuthClientDetailsRepository) {
        super(cacheService);
        this.oAuthClientDetailsRepository = oAuthClientDetailsRepository;
    }

    /**
     * Method description
     *
     *
     * @param detail detail
     */
    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public void saveOAuthClientDetail(OAuthClientDetail detail) {
        oAuthClientDetailsRepository.save(detail);

        // Remove from cache
        removeFromCache(CacheType.CLIENT_DETAILS, detail.getClientId());
    }

    /**
     * Method description
     *
     *
     * @param list list
     */
    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public void saveOAuthClientDetails(List<OAuthClientDetail> list) {
        oAuthClientDetailsRepository.save(list);

        // Remove from cache
        if (list != null && !list.isEmpty()) {
            List<String> cacheKeys = new ArrayList<String>(list.size());

            for (OAuthClientDetail item : list) {
                cacheKeys.add(item.getClientId());
            }

            removeFromCache(CacheType.CLIENT_DETAILS, cacheKeys);
        }

    }

    /**
     * Method description
     *
     *
     * @param clientId clientId
     *
     * @return Return value
     */
    @Override
    public OAuthClientDetail findOAuthClientDetailsByClientId(String clientId) {
        OAuthClientDetail result = null;

        Cache.ValueWrapper wrapper = retrieveFromCache(ApplicationConstants.CLIENT_DETAILS_CACHE, clientId);

        if (wrapper != null && wrapper.get() != null && wrapper.get() instanceof OAuthClientDetail) {
            result = (OAuthClientDetail) wrapper.get();
        } else {
            result = oAuthClientDetailsRepository.findByClientId(clientId);

            if (result != null) {
                putInCache(ApplicationConstants.CLIENT_DETAILS_CACHE, clientId, result);
            }
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @param clientId detail
     */
    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public OAuthClientDetail removeOAuthClientDetails(String clientId) {
        OAuthClientDetail detail = oAuthClientDetailsRepository.findByClientId(clientId);

        if (detail != null) {
            oAuthClientDetailsRepository.delete(detail);

            // Remove from cache
            removeFromCache(CacheType.CLIENT_DETAILS, detail.getClientId());
        }

        return detail;
    }
}
