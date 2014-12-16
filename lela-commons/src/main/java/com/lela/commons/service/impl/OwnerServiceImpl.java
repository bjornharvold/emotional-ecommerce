/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import com.lela.domain.ApplicationConstants;
import com.lela.commons.LelaException;
import com.lela.commons.repository.OwnerRepository;
import com.lela.commons.service.CacheService;
import com.lela.commons.service.CategoryService;
import com.lela.commons.service.OwnerService;
import com.lela.commons.service.SearchException;
import com.lela.commons.service.SearchService;
import com.lela.domain.document.Category;
import com.lela.domain.document.Owner;
import com.lela.domain.document.QOwner;
import com.lela.domain.dto.Letter;
import com.lela.domain.dto.owner.OwnerAggregate;
import com.lela.domain.dto.owner.OwnerAggregateQuery;
import com.lela.domain.dto.owner.OwnersSearchResult;
import com.lela.domain.dto.search.CategoryCount;
import com.lela.domain.dto.search.FacetSearchQuery;
import com.lela.domain.dto.search.NameValueAggregate;
import com.lela.domain.enums.CacheType;
import com.lela.util.utilities.number.NumberUtils;
import com.lela.util.utilities.string.StringUtility;
import org.apache.commons.lang.StringUtils;
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
 * Date: 5/2/12
 * Time: 2:42 PM
 * Responsibility:
 */
@Service("ownerService")
public class OwnerServiceImpl extends AbstractCacheableService implements OwnerService {
    private static final Logger log = LoggerFactory.getLogger(OwnerServiceImpl.class);
    private static final String OWNER_URL_NAME_SEARCH_INDEX = "wnrrlnm";
    private static final String CATEGORY_URL_NAME_INDEX = "ctgry";
    private final OwnerRepository ownerRepository;
    private final SearchService searchService;
    private final CategoryService categoryService;

    @Autowired
    public OwnerServiceImpl(OwnerRepository ownerRepository,
                            CacheService cacheService,
                            SearchService searchService,
                            CategoryService categoryService) {
        super(cacheService);
        this.ownerRepository = ownerRepository;
        this.searchService = searchService;
        this.categoryService = categoryService;
    }

    /**
     * Method description
     *
     * @param urlName urlName
     * @return Return value
     */
    @Override
    public Owner findOwnerByUrlName(String urlName) {
        Owner result = null;

        Cache.ValueWrapper wrapper = retrieveFromCache(ApplicationConstants.OWNER_CACHE, urlName);

        if (wrapper != null && wrapper.get() != null && wrapper.get() instanceof Owner) {
            result = (Owner) wrapper.get();
        } else {
            result = ownerRepository.findByUrlName(urlName);

            if (result != null) {
                putInCache(ApplicationConstants.OWNER_CACHE, urlName, result);
            }
        }

        return result;
    }

    @Override
    public Page<Owner> findOwners(Integer page, Integer maxResults, String firstLetter) {
        return ownerRepository.findAll(QOwner.owner.nm.startsWith(firstLetter), new PageRequest(page, maxResults));
    }

    @Override
    public Long findOwnerCount() {
        return ownerRepository.count();
    }

    /**
     * Method description
     *
     * @param rlnm owner
     */
    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public Owner removeOwner(String rlnm) {
        Owner owner = ownerRepository.findByUrlName(rlnm);
        if (owner != null) {
            ownerRepository.delete(owner);

            // Remove from cache
            removeFromCache(CacheType.OWNER, owner.getRlnm());

            // remove store seo
            removeFromCache(CacheType.OWNER_SEO, ApplicationConstants.OWNER_SEO_CACHE_KEY);
        }

        return owner;
    }

    /**
     * Method description
     *
     * @param owner owner
     * @return Return value
     */
    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public Owner saveOwner(Owner owner) {
        Owner result = ownerRepository.save(owner);

        // Remove from cache
        removeFromCache(CacheType.OWNER, owner.getRlnm());

        // remove store seo
        removeFromCache(CacheType.OWNER_SEO, ApplicationConstants.OWNER_SEO_CACHE_KEY);

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
    public List<Owner> saveOwners(List<Owner> list) {
        List<Owner> result = (List<Owner>) ownerRepository.save(list);

        // Remove from cache
        if (list != null && !list.isEmpty()) {
            List<String> cacheKeys = new ArrayList<String>(list.size());

            for (Owner item : list) {
                cacheKeys.add(item.getRlnm());
            }

            removeFromCache(CacheType.OWNER, cacheKeys);

            // remove store seo
            removeFromCache(CacheType.OWNER_SEO, ApplicationConstants.OWNER_SEO_CACHE_KEY);
        }

        return result;
    }

    @Override
    public Page<Owner> findOwners(Integer page, Integer maxResults) {
        return ownerRepository.findAll(new PageRequest(page, maxResults));
    }

    /**
     * This method is used by the admin tool to quickly display how many items a store has
     *
     * @return List
     */
    @Override
    public List<NameValueAggregate> findOwnerAggregates(OwnerAggregateQuery oaq) {
        List<NameValueAggregate> result = null;

        try {
            FacetSearchQuery query = new FacetSearchQuery();

            // this is the facet filter we want to use to create our aggregate solr results
            List<String> facets = new ArrayList<String>(1);
            facets.add(OWNER_URL_NAME_SEARCH_INDEX);
            query.setFacetFields(facets);

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
    public OwnersSearchResult findOwnerAggregateDetails(OwnerAggregateQuery oaq) {
        OwnersSearchResult result = null;
        Cache.ValueWrapper wrapper = null;

        wrapper = retrieveFromCache(ApplicationConstants.OWNER_SEO_CACHE, ApplicationConstants.OWNER_SEO_CACHE_KEY);

        if ((wrapper != null) && (wrapper.get() instanceof OwnersSearchResult)) {
            result = (OwnersSearchResult) wrapper.get();
        }

        // retrieve from db then put in cache
        if (result == null) {

            // first we need to grab the regular aggregates that contain the store url name and the number of items that store carries
            List<NameValueAggregate> ownerAggregates = findOwnerAggregates(oaq);

            if (ownerAggregates != null && !ownerAggregates.isEmpty()) {
                result = new OwnersSearchResult(findOwnerAggregates(ownerAggregates));
            }

            if (result != null) {
                putInCache(ApplicationConstants.OWNER_SEO_CACHE, ApplicationConstants.OWNER_SEO_CACHE_KEY, result);
            }
        }

        if (StringUtils.isNotBlank(oaq.getFilterOnLetter())) {
            // at this point we might need to filter stores by first letter
            // so we create a copy of what we just put in the cache and filter on it
            result = new OwnersSearchResult(result, oaq.getFilterOnLetter());
        }

        return result;
    }

    @Override
    public List<Owner> findOwners(List<String> fields) {
        return ownerRepository.findOwners(fields);
    }

    private List<OwnerAggregate> findOwnerAggregates(List<NameValueAggregate> aggregates) {

        // here's our list of stores
        List<OwnerAggregate> result = null;

        if (aggregates != null && !aggregates.isEmpty()) {
            result = new ArrayList<OwnerAggregate>(aggregates.size());

            for (NameValueAggregate aggregate : aggregates) {
                result.add(findOwnerAggregate(aggregate));
            }

        }

        return result;
    }

    private OwnerAggregate findOwnerAggregate(NameValueAggregate aggregate) {
        OwnerAggregate result = null;

        try {
            // since we faceted on category we are saving the category url names and their product count
            result = new OwnerAggregate(aggregate);
            result.setOwner(findOwnerByUrlName(aggregate.getRlnm()));

            // here's the facet we'd like to group by
            List<String> facets = new ArrayList<String>(1);
            facets.add(CATEGORY_URL_NAME_INDEX);

            // here's the query param
            Map<String, String> map = new HashMap<String, String>(1);
            map.put(OWNER_URL_NAME_SEARCH_INDEX, aggregate.getRlnm());

            FacetSearchQuery query = new FacetSearchQuery();
            query.setFacetFields(facets);
            query.setQueryMap(map);

            // this is the response back from the server
            List<NameValueAggregate> list = searchService.searchForFacetAggregates(query);

            if (list != null && !list.isEmpty()) {

                // we want to loop through the facet results and save them
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

    private List<Letter> createAlphabet(List<OwnerAggregate> owners) {
        // finally we can create the alphabet
        List<Letter> result = null;

        if (!owners.isEmpty()) {
            result = new ArrayList<Letter>();

            Map<String, String> letterMap = new HashMap<String, String>();
            for (OwnerAggregate owner : owners) {
                letterMap.put(owner.getRlnm().substring(0, 1).toLowerCase(), owner.getRlnm().substring(0, 1).toLowerCase());
            }

            if (!letterMap.isEmpty()) {
                for (String letter : StringUtility.getAlphabet()) {
                    if (letterMap.containsKey(letter)) {
                        result.add(new Letter(letter, true));
                    } else {
                        result.add(new Letter(letter, false));
                    }
                }
            }
        }

        return result;
    }

    @Override
    public List<Owner> findAllOwners(Integer chunk) {
        List<Owner> result = null;
        Long count = findOwnerCount();

        if (count != null && count > 0) {
            result = new ArrayList<Owner>(count.intValue());
            Integer iterations = NumberUtils.calculateIterations(count, chunk.longValue());

            // load up items in a paginated fashion from mongo
            for (int i = 0; i < iterations; i++) {
                result.addAll(findOwners(i, chunk).getContent());
            }
        }

        return result;
    }

}
