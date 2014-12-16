/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import com.lela.domain.ApplicationConstants;
import com.lela.commons.LelaException;
import com.lela.commons.repository.StoreRepository;
import com.lela.commons.service.CacheService;
import com.lela.commons.service.CategoryService;
import com.lela.commons.service.EventService;
import com.lela.commons.service.SearchException;
import com.lela.commons.service.SearchService;
import com.lela.commons.service.StoreService;
import com.lela.domain.document.Category;
import com.lela.domain.document.QStore;
import com.lela.domain.document.Store;
import com.lela.domain.dto.search.CategoryCount;
import com.lela.domain.dto.search.FacetSearchQuery;
import com.lela.domain.dto.search.NameValueAggregate;
import com.lela.domain.dto.store.StoreAggregate;
import com.lela.domain.dto.store.StoreAggregateQuery;
import com.lela.domain.dto.store.StoresSearchResult;
import com.lela.domain.enums.CacheType;
import com.lela.util.utilities.number.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 4/27/12
 * Time: 12:37 AM
 * Responsibility:
 */
@Service("storeService")
public class StoreServiceImpl extends AbstractCacheableService implements StoreService {
    private static final Logger log = LoggerFactory.getLogger(StoreServiceImpl.class);

    private static final String STORE_URL_NAME_SEARCH_INDEX = "strrlnms";
    private static final String CATEGORY_URL_NAME_INDEX = "ctgry";
    private final SearchService searchService;
    private final StoreRepository storeRepository;
    private final CategoryService categoryService;
    private final EventService eventService;

    @Autowired
    public StoreServiceImpl(SearchService searchService,
                            StoreRepository storeRepository,
                            CacheService cacheService,
                            CategoryService categoryService, EventService eventService) {
        super(cacheService);
        this.searchService = searchService;
        this.storeRepository = storeRepository;
        this.categoryService = categoryService;
        this.eventService = eventService;
    }

    /**
     * This method is used by the admin tool to quickly display how many items a store has
     *
     * @return List
     */
    @Override
    public List<NameValueAggregate> findStoreAggregates(StoreAggregateQuery saq) {
        List<NameValueAggregate> result = null;

        try {
            FacetSearchQuery query = new FacetSearchQuery();

            // this is the facet filter we want to use to create our aggregate solr results
            List<String> facetFields = new ArrayList<String>(1);
            facetFields.add(STORE_URL_NAME_SEARCH_INDEX);
            query.setFacetFields(facetFields);

            result = searchService.searchForFacetAggregates(query);

        } catch (SearchException e) {
            throw new LelaException(e.getMessage(), e);
        }

        return result;
    }

    /**
     * Method is used by the stores page to display aggregate store information to the user
     * and specifically to our search engines.
     *
     * @return StoresSearchResult
     */
    @Override
    public StoresSearchResult findStoreAggregateDetails(StoreAggregateQuery saq) {
        StoresSearchResult result = null;

        Cache.ValueWrapper wrapper = retrieveFromCache(ApplicationConstants.STORE_SEO_CACHE, ApplicationConstants.STORE_SEO_CACHE_KEY);

        if ((wrapper != null) && (wrapper.get() instanceof StoresSearchResult)) {
            result = (StoresSearchResult) wrapper.get();
        }

        // retrieve from db then put in cache
        if (result == null) {

            // first we need to grab the regular aggregates that contain the store url name and the number of items that store carries
            List<NameValueAggregate> storeAggregates = findStoreAggregates(saq);

            if (storeAggregates != null && !storeAggregates.isEmpty()) {
                result = new StoresSearchResult(findStoreAggregates(storeAggregates));
            }

            if (result != null) {
                putInCache(ApplicationConstants.STORE_SEO_CACHE, ApplicationConstants.STORE_SEO_CACHE_KEY, result);
            }
        }

        if (saq != null && StringUtils.isNotBlank(saq.getFilterOnLetter())) {
            // at this point we might need to filter stores by first letter
            // so we create a copy of what we just put in the cache and filter on it
            result = new StoresSearchResult(result, saq.getFilterOnLetter());
        }


        return result;
    }

    private List<StoreAggregate> findStoreAggregates(List<NameValueAggregate> aggregates) {
        List<StoreAggregate> result = null;

        if (aggregates != null && !aggregates.isEmpty()) {
            result = new ArrayList<StoreAggregate>(aggregates.size());

            for (NameValueAggregate aggregate : aggregates) {
                result.add(findStoreAggregate(aggregate));
            }
        }

        return result;
    }

    private StoreAggregate findStoreAggregate(NameValueAggregate aggregate) {
        StoreAggregate result = null;

        try {
            result = new StoreAggregate(aggregate);
            result.setStore(findStoreByUrlName(aggregate.getRlnm()));

            // here's the facet we'd like to group by
            List<String> facets = new ArrayList<String>(1);
            facets.add(CATEGORY_URL_NAME_INDEX);

            // here's the query param
            Map<String, String> map = new HashMap<String, String>(1);
            map.put(STORE_URL_NAME_SEARCH_INDEX, aggregate.getRlnm());

            FacetSearchQuery query = new FacetSearchQuery();
            query.setFacetFields(facets);
            query.setQueryMap(map);

            // this is the response back from the server
            List<NameValueAggregate> list = searchService.searchForFacetAggregates(query);

            if (list != null && !list.isEmpty()) {
                List<CategoryCount> categoryCounts = new ArrayList<CategoryCount>();

                for (NameValueAggregate nva : list) {
                    if (nva.getCnt() != null && nva.getCnt() > 0) {
                        Category category = categoryService.findCategoryByUrlName(nva.getRlnm());
                        CategoryCount cc = new CategoryCount(category, nva.getCnt());
                        categoryCounts.add(cc);
                    }
                }

                result.setCategories(categoryCounts);
            }
        } catch (SearchException e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public List<Store> findStoreUrlNamesByMerchantIds(List<String> merchantIds) {
        return storeRepository.findStoreUrlNamesByMerchantIds(merchantIds);
    }

    @Override
    public List<Store> findAllStores(Integer chunk) {
        List<Store> result = null;
        Long count = findStoreCount();

        if (count != null && count > 0) {
            result = new ArrayList<Store>(count.intValue());
            Integer iterations = NumberUtils.calculateIterations(count, chunk.longValue());

            // load up items in a paginated fashion from mongo
            for (int i = 0; i < iterations; i++) {
                result.addAll(findStores(i, chunk).getContent());
            }
        }

        return result;
    }

    @Override
    public List<Store> findStores(List<String> fields) {
        return storeRepository.findStores(fields);
    }

    /**
     * Method description
     *
     * @param merchantId merchantId
     * @return Return value
     */
    @Override
    public Store findStoreByMerchantId(String merchantId) {
        return storeRepository.findOne(QStore.store.mrchntd.eq(merchantId));
    }

    @Override
    public List<Store> findStoresByMerchantIds(List<String> merchantIds) {
        return (List<Store>) storeRepository.findAll(QStore.store.mrchntd.in(merchantIds));
    }

    /**
     * Method description
     *
     * @param urlName urlName
     * @return Return value
     */
    @Override
    public Store findStoreByUrlName(String urlName) {
        Store result = null;

        Cache.ValueWrapper wrapper = retrieveFromCache(ApplicationConstants.STORE_CACHE, urlName);

        if (wrapper != null && wrapper.get() != null && wrapper.get() instanceof Store) {
            result = (Store) wrapper.get();
        } else {
            result = storeRepository.findByUrlName(urlName);

            if (result != null) {
                putInCache(ApplicationConstants.STORE_CACHE, urlName, result);
            }
        }

        return result;
    }

    /**
     * Method description
     *
     * @param affiliateUrlName affiliateUrlName
     * @return Return value
     */
    @Override
    public Store findStoreByAffiliateUrlName(String affiliateUrlName) {
        return storeRepository.findByAffiliateUrlName(affiliateUrlName);
    }

    /**
     * TODO remove this method. Only allow paginated querying.
     *
     * @return Return value
     */
//    @Override
//    public List<Store> findStores() {
//        return (List<Store>) storeRepository.findAll();
//    }
    @Override
    public Page<Store> findStores(Integer page, Integer maxResults) {
        return storeRepository.findAll(new PageRequest(page, maxResults));
    }

    @Override
    public Page<Store> findStores(Integer page, Integer maxResults, String firstLetter) {
        return storeRepository.findAll(QStore.store.nm.startsWith(firstLetter), new PageRequest(page, maxResults));
    }

    @Override
    public Long findStoreCount() {
        return storeRepository.count();
    }

    /**
     * Convenience method to retrieve several stores at once
     * TODO we can look into potentially retrieving stores from the cache instead as a performance improvement.
     * The code for that has already been written anyway.
     *
     * @param storeUrlNames storeUrlNames
     * @return Stores
     */
    @Override
    public List<Store> findStoresByUrlName(List<String> storeUrlNames) {
        return (List<Store>) storeRepository.findAll(QStore.store.rlnm.in(storeUrlNames));
    }

    /**
     * Method description
     *
     * @param rlnm store
     */
    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public Store removeStore(String rlnm) {
        Store store = storeRepository.findByUrlName(rlnm);

        if (store != null) {
            storeRepository.delete(store);

            // remove the store
            removeFromCache(CacheType.STORE, store.getRlnm());

            // remove store seo
            removeFromCache(CacheType.STORE_SEO, ApplicationConstants.STORE_SEO_CACHE_KEY);
        }

        return store;
    }

    /**
     * Method description
     *
     * @param store store
     * @return Return value
     */
    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST', 'RIGHT_LOGIN_ONBOARD')")
    @Override
    public Store saveStore(Store store) {
        Store result = storeRepository.save(store);

        // Remove from cache
        removeFromCache(CacheType.STORE, store.getRlnm());

        // remove store seo
        removeFromCache(CacheType.STORE_SEO, ApplicationConstants.STORE_SEO_CACHE_KEY);

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
    public List<Store> saveStores(List<Store> list) {
        List<Store> result = (List<Store>) storeRepository.save(list);

        // Remove from cache
        if (list != null && !list.isEmpty()) {
            List<String> cacheKeys = new ArrayList<String>(list.size());

            for (Store item : list) {
                cacheKeys.add(item.getRlnm());
            }

            removeFromCache(CacheType.STORE, cacheKeys);

            // remove store seo
            removeFromCache(CacheType.STORE_SEO, ApplicationConstants.STORE_SEO_CACHE_KEY);
        }

        return result;
    }

    /**
     * Method description
     *
     * @param id id
     * @return Return value
     */
    private Store findStoreById(ObjectId id) {
        return storeRepository.findOne(id);
    }
}
