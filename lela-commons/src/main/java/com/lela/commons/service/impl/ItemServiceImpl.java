/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import com.lela.commons.service.*;
import com.lela.domain.ApplicationConstants;
import com.lela.commons.repository.ItemRepository;
import com.lela.domain.document.Category;
import com.lela.domain.document.FunctionalFilter;
import com.lela.domain.document.Item;
import com.lela.domain.document.QItem;
import com.lela.domain.enums.CacheType;
import com.lela.util.utilities.number.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lela.domain.document.QItem.item;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by Bjorn Harvold
 * Date: 6/1/12
 * Time: 8:24 PM
 * Responsibility:
 */
@Service("itemService")
public class ItemServiceImpl extends AbstractCacheableService implements ItemService {

    private static final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);

    /**
     * Solr search service
     */
    private final SearchService searchService;

    /**
     * Field description
     */
    private final ItemRepository itemRepository;

    private final AffiliateService affiliateService;

    @Autowired
    public ItemServiceImpl(CacheService cacheService,
                           SearchService searchService,
                           ItemRepository itemRepository,
                           AffiliateService affiliateService) {
        super(cacheService);
        this.searchService = searchService;
        this.itemRepository = itemRepository;
        this.affiliateService = affiliateService;
    }

    /**
     * Method description
     *
     * @param urlName urlName
     * @return Return value
     */
    @Override
    public Item findItemByUrlName(String urlName) {
        Item result = null;

        Cache.ValueWrapper wrapper = retrieveFromCache(ApplicationConstants.ITEM_CACHE, urlName);

        if (wrapper != null && wrapper.get() != null && wrapper.get() instanceof Item) {
            result = (Item) wrapper.get();
        } else {
            result = itemRepository.findByUrlName(urlName);

            if (result != null) {
                putInCache(ApplicationConstants.ITEM_CACHE, urlName, result);
            }
        }

        return result;
    }

    /**
     * Return a list of all items for a specific category. Cache anc clone the results.
     *
     * @param categoryUrlName categoryUrlName
     * @return Return value
     */
    @Override
    public List<Item> findItemsByCategoryUrlName(String categoryUrlName) {
        List<Item>         result  = null;
        Cache.ValueWrapper wrapper = retrieveFromCache(ApplicationConstants.ITEM_CATEGORY_CACHE, categoryUrlName);

        if ((wrapper != null) && (wrapper.get() instanceof List)) {
            result = (List<Item>) wrapper.get();
        }

        // retrieve from db then put in cache
        if (result == null) {
            result = itemRepository.findByCategoryUrlName(categoryUrlName);

            if (result != null) {
                putInCache(ApplicationConstants.ITEM_CATEGORY_CACHE, categoryUrlName, result);
            }
        }

        // retrieve all items from the cache
        return cloneItemList(result);
    }

    /**
     * Returns a paginated list of items for a specific category.
     * Notice that it only queries a few fields in the mongo collection to save space.
     * @param categoryUrlName categoryUrlName
     * @param page page
     * @param maxResults maxResults
     * @return List
     */
    @Override
    public List<Item> findItemsByCategoryUrlName(String categoryUrlName, int page, int maxResults, List<String> extraFields) {
        return itemRepository.findItemsByCategoryUrlName(categoryUrlName, page, maxResults, extraFields);
    }

    @Override
    public List<Item> findItemsByCategoryUrlName(String categoryUrlName, int page, int maxResults) {
        return itemRepository.findItemsByCategoryUrlName(categoryUrlName, page, maxResults, null);
    }

    /**
     * Method will retrieve all items defined by a list of category url names. It will do so in an iterative fashion
     * to avoid memory concerns with Mongo
     * @param categoryUrlNames categoryUrlNames
     * @param maxResults maxResults
     * @return A map where the key is the category url name and the value is all the category items
     */
    @Override
    public Map<String, List<Item>> paginateThroughAllItemsPerCategories(List<String> categoryUrlNames, Integer maxResults) {
        Map<String, List<Item>> result = null;

        if (categoryUrlNames != null && !categoryUrlNames.isEmpty()) {

            for (String categoryUrlName : categoryUrlNames) {
                List<Item> items = paginateThroughAllItemsPerCategory(categoryUrlName, maxResults);

                if (result == null) {
                    result = new HashMap<String, List<Item>>(categoryUrlNames.size());
                }

                // add items to tms map. Category url name is the key
                result.put(categoryUrlName, items);
            }
        }

        return result;
    }

    /**
     * Method will retrieve all items defined by the category url name. It will do so in an iterative fashion
     * to avoid memory concerns with Mongo
     * @param categoryUrlName categoryUrlName
     * @param maxResults maxResults
     * @return List of items for the specified category
     */
    @Override
    public List<Item> paginateThroughAllItemsPerCategory(String categoryUrlName, Integer maxResults) {
        List<Item> result = null;
        Long count = findItemCountByCategoryUrlName(categoryUrlName);

        if (count != null && count > 0) {
            result = new ArrayList<Item>(count.intValue());

            Integer iterations = NumberUtils.calculateIterations(count, maxResults.longValue());

            // load up items in a paginated fashion from mongo
            for (int i = 0; i < iterations; i++) {
                result.addAll(findItemsByCategoryUrlName(categoryUrlName, i, maxResults.intValue()));
            }
        }

        return result;
    }

    /**
     * Method will retrieve all items defined by the category url name. It will do so in an iterative fashion
     * to avoid memory concerns with Mongo
     * @param categoryUrlName categoryUrlName
     * @param maxResults maxResults
     * @return List of items for the specified category
     */
    @Override
    public List<Item> paginateThroughAllItemsPerCategoryIncludingStores(String categoryUrlName, Integer maxResults) {
        List<Item> result = null;
        Long count = findItemCountByCategoryUrlName(categoryUrlName);

        if (count != null && count > 0) {
            result = new ArrayList<Item>(count.intValue());

            Integer iterations = NumberUtils.calculateIterations(count, maxResults.longValue());

            // load up items in a paginated fashion from mongo
            for (int i = 0; i < iterations; i++) {
                List<String> extraFields = new ArrayList<String>(1);
                extraFields.add("strs");
                result.addAll(findItemsByCategoryUrlName(categoryUrlName, i, maxResults.intValue(), extraFields));
            }
        }

        return result;
    }

    /**
     * Method description
     *
     * @param ownerName ownerName
     * @return Return value
     */
    @Override
    public List<Item> findItemsByOwnerName(String ownerName) {
        List<Item> result;

        if (StringUtils.isNotBlank(ownerName)) {
            result = (List<Item>) itemRepository.findAll(item.wnr.nm.startsWithIgnoreCase(ownerName));
        } else {
            result = (List<Item>) itemRepository.findAll();
        }

        return result;
    }

    /**
     * Method description
     *
     * @param itemUrlNames itemUrlNames
     * @return Return value
     */
    @Override
    public List<Item> findItemsByUrlName(List<String> itemUrlNames) {
        return itemRepository.findItemsByUrlName(itemUrlNames);
    }

    /**
     * Method description
     *
     * @param rlnm item
     */
    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public Item removeItem(String rlnm) {
        Item item = itemRepository.findByUrlName(rlnm);
        if (item != null) {
            itemRepository.delete(item);

            // Remove from cache
            removeFromCache(CacheType.ITEM, item.getRlnm());

            // remove from category items cache as well
            removeFromCache(CacheType.ITEM_CATEGORY, item.getCtgry().getRlnm());

            // index items with search server
            try {
                searchService.deleteItemByUrlName(rlnm);
            } catch (Exception e) {
                // Don't allow a Solr exception to cause the calling code to fail
                log.error("Solr delete failed for item: " + rlnm, e);
            }
        }

        return item;
    }

    /**
     * Method description
     *
     * @param item item
     * @return Return value
     */
    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public Item saveItem(Item item) {
        Item result = itemRepository.save(item);

        List<Category> excludedCategories = affiliateService.findExcludedCategories();

        // Remove from cache
        removeFromCache(CacheType.ITEM, item.getRlnm());

        // remove from category items cache as well
        removeFromCache(CacheType.ITEM_CATEGORY, item.getCtgry().getRlnm());

        if(!excludedCategories.contains(item.getCtgry()))
        {
            // index items with search server
            try {
                searchService.indexItem(result);
            } catch (SearchException e) {
                log.error(e.getMessage(), e);
            }
        }

        return result;
    }

    /**
     * Update the embedded stores list on Item
     *
     * @param item item
     */
    @Override
    public void updateAvailableStoresOnItem(Item item) {
        itemRepository.updateAvailableStoresOnItem(item);

        // Remove from cache
        removeFromCache(CacheType.ITEM, item.getRlnm());

        // remove from category items cache as well
        removeFromCache(CacheType.ITEM_CATEGORY, item.getCtgry().getRlnm());
    }

    /**
     *
     * @param list list
     * @return Return value
     */
    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public List<Item> saveItems(List<Item> list) {
        List<Item> result = (List<Item>) itemRepository.save(list);

        if (list != null && !list.isEmpty()) {
            Map<String, String> itemCacheKeys = new HashMap<String, String>(list.size());
            Map<String, String> categoryCacheKeys = new HashMap<String, String>();
            List<Item> availableItems = new ArrayList<Item>();
            List<String> unavailableItemIds = new ArrayList<String>();

            List<Category> excludedCategories = affiliateService.findExcludedCategories();

            for (Item item : list) {
                // for cache flushing
                itemCacheKeys.put(item.getRlnm(), item.getRlnm());
                categoryCacheKeys.put(item.getCtgry().getRlnm(), item.getCtgry().getRlnm());

                // index only available items and non affiliate items
                if ((item.getVlbl() == null || item.getVlbl())&& !excludedCategories.contains(item.getCtgry())) {
                    availableItems.add(item);
                } else {
                    unavailableItemIds.add(item.getIdString());
                }
            }



            // Remove from cache
            removeFromCache(CacheType.ITEM, new ArrayList<String>(itemCacheKeys.keySet()));

            // remove from category items cache as well
            removeFromCache(CacheType.ITEM_CATEGORY, new ArrayList<String>(categoryCacheKeys.keySet()));

            if (!unavailableItemIds.isEmpty()) {
                try {
                    // remove unavailable items from solr
                    searchService.deleteItemsById(unavailableItemIds);

                    // sleep a bit here so Solr has a chance to process everything
                    Thread.sleep(5000);
                } catch (SearchException e) {
                    log.error(e.getMessage(), e);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
            }

            if (!availableItems.isEmpty()) {
                try {
                    // index items with search server
                    searchService.indexItems(availableItems);

                    // sleep a bit here so Solr has a chance to process everything
                    Thread.sleep(5000);
                } catch (SearchException e) {
                    log.error(e.getMessage(), e);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

        return result;
    }

    @Override
    public Long findItemCountByCategoryUrlName(String categoryUrlName) {
        return itemRepository.count(QItem.item.ctgry.rlnm.eq(categoryUrlName));
    }

    @Override
    public List<Item> findItemsByStoreAndCategoryUrlNames(String storeUrlName, String categoryUrlName) {
        return itemRepository.findByStoreUrlNameAndCategoryUrlNames(storeUrlName, categoryUrlName);
    }

    @Override
    public List<Item> findItemsByOwnerAndCategoryUrlNames(String ownerUrlName, String categoryUrlName) {
        return itemRepository.findByOwnerUrlNameAndCategoryUrlNames(ownerUrlName, categoryUrlName);
    }

    @Override
    public Item findItemById(ObjectId id) {
        return itemRepository.findOne(id);
    }

    @Override
    public List<Item> findUnavailableItemIdsByCategoryUrlName(String categoryUrlName) {
        return itemRepository.findUnavailableItemIdsByCategoryUrlName(categoryUrlName);
    }

    /**
     * Method will loop through all categories or just the ones defined and try to find
     * items matching the filters
     * @param categoryUrlName categoryUrlName
     * @param filters filters
     * @return Count
     */
    @Override
    public Long findItemCountByCategoryAndFilters(String categoryUrlName, List<FunctionalFilter> functionalFilters, Map<String, Map<String, String>> filters) {
        return itemRepository.findCountByCategoriesAndFilters(categoryUrlName, functionalFilters, filters);
    }


    /**
     * Method description
     *
     * @param list list
     * @return Return value
     */
    private List<Item> cloneItemList(List<Item> list) {
        List<Item> result = null;

        if ((list != null) && !list.isEmpty()) {
            result = new ArrayList<Item>(list.size());

            for (Item item : list) {
                result.add(new Item(item));
            }
        }

        return result;
    }
}
