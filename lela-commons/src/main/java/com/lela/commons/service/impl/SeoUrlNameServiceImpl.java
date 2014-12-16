/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import com.lela.domain.ApplicationConstants;
import com.lela.commons.repository.SeoUrlNameRepository;
import com.lela.commons.service.CacheService;
import com.lela.commons.service.SeoUrlNameService;
import com.lela.domain.document.QSeoUrlName;
import com.lela.domain.document.SeoUrlName;
import com.lela.domain.enums.CacheType;
import com.lela.domain.enums.SeoUrlNameType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 4/13/12
 * Time: 11:19 PM
 * Responsibility:
 */
@Service("seoUrlNameService")
public class SeoUrlNameServiceImpl extends AbstractCacheableService implements SeoUrlNameService {
    private final static Logger log = LoggerFactory.getLogger(SeoUrlNameServiceImpl.class);
    private final SeoUrlNameRepository seoUrlNameRepository;

    @Autowired
    public SeoUrlNameServiceImpl(SeoUrlNameRepository seoUrlNameRepository, CacheService cacheService) {
        super(cacheService);
        this.seoUrlNameRepository = seoUrlNameRepository;
    }

    @PreAuthorize("hasAnyRole('RIGHT_INSERT_SEO_URL_NAME')")
    @Override
    public SeoUrlName saveSeoUrlName(SeoUrlName seoUrlName) {
        seoUrlName = seoUrlNameRepository.save(seoUrlName);

        removeFromCache(CacheType.SEO, seoUrlName.getSrlnm());

        return seoUrlName;
    }

    @PreAuthorize("hasAnyRole('RIGHT_READ_SEO_URL_NAME')")
    @Override
    public Page<SeoUrlName> findSeoUrlNames(Integer page, Integer maxResults) {
        return seoUrlNameRepository.findAll(new PageRequest(page, maxResults));
    }

    @Override
    public SeoUrlName validateSeoUrlName(String seoUrlName, SeoUrlNameType type) {
        SeoUrlName result  = null;
        Cache.ValueWrapper wrapper = retrieveFromCache(ApplicationConstants.SEO_CACHE, seoUrlName);

        if ((wrapper != null) && (wrapper.get() instanceof List)) {
            result = (SeoUrlName) wrapper.get();
        }

        // retrieve from db then put in cache
        if (result == null) {
            result = seoUrlNameRepository.findOne(QSeoUrlName.seoUrlName.srlnm.eq(seoUrlName).and(QSeoUrlName.seoUrlName.tp.eq(type)));

            if (result != null) {
                putInCache(ApplicationConstants.SEO_CACHE, seoUrlName, result);
            }
        }

        return result;
    }

    @PreAuthorize("hasAnyRole('RIGHT_READ_SEO_URL_NAME')")
    @Override
    public SeoUrlName findSeoUrlName(String seoUrlName) {
        return seoUrlNameRepository.findOne(QSeoUrlName.seoUrlName.srlnm.eq(seoUrlName));
    }

    @PreAuthorize("hasAnyRole('RIGHT_DELETE_SEO_URL_NAME')")
    @Override
    public void removeSeoUrlName(SeoUrlName seoUrlName) {
        seoUrlNameRepository.delete(seoUrlName);

        removeFromCache(CacheType.SEO, seoUrlName.getSrlnm());
    }

    @PreAuthorize("hasAnyRole('RIGHT_READ_SEO_URL_NAME')")
    @Override
    public Long findSeoUrlNameCount() {
        return seoUrlNameRepository.count();
    }
}
