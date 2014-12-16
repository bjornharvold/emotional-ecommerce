/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import com.lela.domain.ApplicationConstants;
import com.lela.commons.repository.FunctionalFilterRepository;
import com.lela.commons.service.CacheService;
import com.lela.commons.service.FunctionalFilterService;
import com.lela.domain.document.FunctionalFilter;
import com.lela.domain.document.QFunctionalFilter;
import com.lela.domain.enums.CacheType;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by Bjorn Harvold
 * Date: 6/4/12
 * Time: 8:02 PM
 * Responsibility:
 */
@Service("functionalFilterService")
public class FunctionalFilterServiceImpl extends AbstractCacheableService implements FunctionalFilterService {
    private final FunctionalFilterRepository functionalFilterRepository;

    @Autowired
    public FunctionalFilterServiceImpl(CacheService cacheService, FunctionalFilterRepository functionalFilterRepository) {
        super(cacheService);
        this.functionalFilterRepository = functionalFilterRepository;
    }

    /**
     * Method description
     *
     * @param urlName categoryUrlName
     * @param key             key
     * @return Return value
     */
    @Override
    public FunctionalFilter findFunctionalFilterByUrlNameAndKey(String urlName, String key) {
        return functionalFilterRepository.findOne(
                QFunctionalFilter.functionalFilter.rlnm.eq(urlName).and(
                        QFunctionalFilter.functionalFilter.ky.eq(key)));
    }

    /**
     * Return number of functional filters for url name
     * @param urlName urlName
     * @return Count
     */
    @Override
    public Long findFunctionalFilterCountByUrlName(String urlName) {
        return functionalFilterRepository.count(QFunctionalFilter.functionalFilter.rlnm.eq(urlName));
    }

    /**
     * Method description
     *
     * @param urlName categoryUrlName
     * @return Return value
     */
    @Override
    public List<FunctionalFilter> findFunctionalFiltersByUrlName(String urlName) {
        List<FunctionalFilter> result  = null;
        Cache.ValueWrapper     wrapper = retrieveFromCache(ApplicationConstants.FUNCTIONAL_FILTER_CACHE, urlName);

        if ((wrapper != null) && (wrapper.get() instanceof List)) {
            result = (List<FunctionalFilter>) wrapper.get();
        }

        // retrieve from db then put in cache
        if (result == null) {
            List<String> sortProps = new ArrayList<String>();

            sortProps.add("rdr");
            result = functionalFilterRepository.findByRlnm(urlName, new Sort(Sort.Direction.ASC, sortProps));

            if (result != null) {
                putInCache(ApplicationConstants.FUNCTIONAL_FILTER_CACHE, urlName, result);
            }
        }

        return cloneFunctionalFilterList(result);
    }

    /**
     * Method description
     *
     * @param id ff
     */
    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public FunctionalFilter removeFunctionalFilter(ObjectId id) {
        FunctionalFilter ff = functionalFilterRepository.findOne(id);

        if (ff != null) {
            functionalFilterRepository.delete(ff);

            // Remove from cache
            removeFromCache(CacheType.FUNCTIONAL_FILTER, ff.getRlnm());
        }

        return ff;
    }

    /**
     * Method description
     *
     * @param ff ff
     * @return Return value
     */
    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public FunctionalFilter saveFunctionalFilter(FunctionalFilter ff) {
        FunctionalFilter result = functionalFilterRepository.save(ff);

        // Remove from cache
        removeFromCache(CacheType.FUNCTIONAL_FILTER, ff.getRlnm());

        return result;
    }

    /**
     * Method description
     *
     * @param list list
     * @return Return value
     */
    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public List<FunctionalFilter> saveFunctionalFilters(List<FunctionalFilter> list) {
        List<FunctionalFilter> result = (List<FunctionalFilter>) functionalFilterRepository.save(list);

        // Remove from cache
        if (list != null && !list.isEmpty()) {
            List<String> cacheKeys = new ArrayList<String>(list.size());

            for (FunctionalFilter item : list) {
                cacheKeys.add(item.getRlnm());
            }

            removeFromCache(CacheType.FUNCTIONAL_FILTER, cacheKeys);
        }

        return result;
    }

    /**
     * Method description
     *
     * @param list list
     * @return Return value
     */
    private List<FunctionalFilter> cloneFunctionalFilterList(List<FunctionalFilter> list) {
        List<FunctionalFilter> result = null;

        if ((list != null) && !list.isEmpty()) {
            result = new ArrayList<FunctionalFilter>(list.size());

            for (FunctionalFilter ff : list) {
                result.add(new FunctionalFilter(ff));
            }
        }

        return result;
    }
}
