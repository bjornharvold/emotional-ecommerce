/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.service.impl;

import com.lela.commons.repository.CssStyleRepository;
import com.lela.commons.service.CacheService;
import com.lela.commons.service.CssStyleService;
import com.lela.domain.ApplicationConstants;
import com.lela.domain.document.Application;
import com.lela.domain.document.CssStyle;
import com.lela.domain.enums.CacheType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Chris Tallent
 * Date: 12/4/12
 * Time: 4:44 PM
 */
@Service("cssStyleService")
public class CssStyleServiceImpl extends AbstractCacheableService implements CssStyleService {
    private final CssStyleRepository cssStyleRepository;

    @Autowired
    public CssStyleServiceImpl(CacheService cacheService, CssStyleRepository cssStyleRepository) {
        super(cacheService);
        this.cssStyleRepository = cssStyleRepository;
    }

    @Override
    public List<CssStyle> findCssStyles() {
        List<CssStyle>     result  = null;
        Cache.ValueWrapper wrapper = retrieveFromCache(ApplicationConstants.CSS_STYLE_CACHE, ApplicationConstants.CSS_STYLES_KEY);

        if ((wrapper != null) && (wrapper.get() instanceof List)) {
            result = (List<CssStyle>) wrapper.get();
        }

        // retrieve from db then put in cache
        if (result == null) {
            List<String> sortProps = new ArrayList<String>();

            sortProps.add("nm");
            result = (List<CssStyle>) cssStyleRepository.findAll(new Sort(Sort.Order.create(Sort.Direction.ASC,
                    sortProps)));

            if (result != null) {
                putInCache(ApplicationConstants.CSS_STYLE_CACHE, ApplicationConstants.CSS_STYLES_KEY, result);
            }
        }

        return result;
    }

    @Override
    public Page<CssStyle> findStyles(Integer page, Integer maxResults) {
        return cssStyleRepository.findAll(new PageRequest(page, maxResults));
    }

    @Override
    public CssStyle findStyleByUrlName(String urlName) {
        CssStyle result = null;

        Cache.ValueWrapper wrapper = retrieveFromCache(ApplicationConstants.CSS_STYLE_CACHE, urlName);

        if (wrapper != null && wrapper.get() != null && wrapper.get() instanceof Application) {
            result = (CssStyle) wrapper.get();
        } else {
            result = cssStyleRepository.findByUrlName(urlName);

            if (result != null) {
                putInCache(ApplicationConstants.CSS_STYLE_CACHE, urlName, result);
            }
        }

        return result;
    }

    @PreAuthorize("hasAnyRole('RIGHT_INSERT_CSS_STYLE')")
    @Override
    public CssStyle saveStyle(CssStyle style) {
        style = cssStyleRepository.save(style);
        removeFromCache(CacheType.CSS_STYLE, style.getRlnm());

        return style;
    }

    @PreAuthorize("hasAnyRole('RIGHT_DELETE_CSS_STYLE')")
    @Override
    public void removeStyle(String urlName) {
        CssStyle style = findStyleByUrlName(urlName);

        if (style != null) {
            cssStyleRepository.delete(style);

            removeFromCache(CacheType.CSS_STYLE, urlName);
        }
    }
}

