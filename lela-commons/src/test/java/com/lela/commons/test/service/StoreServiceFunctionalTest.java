/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.test.service;

import com.lela.commons.service.CategoryService;
import com.lela.commons.service.ItemService;
import com.lela.commons.service.LocalCacheEvictionService;
import com.lela.commons.service.OwnerService;
import com.lela.commons.service.SearchException;
import com.lela.commons.service.SearchService;
import com.lela.commons.service.StoreService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.test.AbstractFunctionalTest;
import com.lela.domain.document.AvailableInStore;
import com.lela.domain.document.Category;
import com.lela.domain.document.Item;
import com.lela.domain.document.Owner;
import com.lela.domain.document.Store;
import com.lela.domain.dto.search.NameValueAggregate;
import com.lela.domain.dto.store.StoreAggregateQuery;
import com.lela.domain.dto.store.StoresSearchResult;
import com.lela.domain.enums.StoreType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Bjorn Harvold
 * Date: 6/23/11
 * Time: 11:16 AM
 * Responsibility:
 */
public class StoreServiceFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(StoreServiceFunctionalTest.class);
    private static final String STORE_URL_NAME = "StoreServiceFunctionalTestStore";
    private static final String ITEM_URL_NAME = "StoreServiceFunctionalTestItem";
    private static final String CATEGORY_URL_NAME = "StoreServiceFunctionalTestCategory";
    private static final String OWNER_URL_NAME = "StoreServiceFunctionalTestOwner";

    @Autowired
    private LocalCacheEvictionService localCacheEvictionService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private CategoryService categoryService;

    private Store store;
    private Item item;
    private Category category;
    private Owner owner;

    @Before
    public void setUp() {

        log.info("Starting set up...");
        SpringSecurityHelper.secureChannel();

        try {
            // create store
            store = new Store(STORE_URL_NAME, STORE_URL_NAME, StoreType.ONLINE);
            store.setMrchntd(STORE_URL_NAME);
            store.setFfltrlnm(STORE_URL_NAME);
            store = storeService.saveStore(store);
            assertNotNull("Store is null", store);
            assertNotNull("Store id is null", store.getId());

            // create owner
            owner = new Owner();
            owner.setNm(OWNER_URL_NAME);
            owner.setRlnm(OWNER_URL_NAME);
            owner.setSrlnm(OWNER_URL_NAME);
            owner = ownerService.saveOwner(owner);

            // create category
            category = new Category();
            category.setNm(CATEGORY_URL_NAME);
            category.setRlnm(CATEGORY_URL_NAME);
            category.setSrlnm(CATEGORY_URL_NAME);
            category = categoryService.saveCategory(category);
            assertNotNull("Category is null", category);
            assertNotNull("Category id is null", category.getId());

            // create item
            item = new Item();
            item.setNm(ITEM_URL_NAME);
            item.setRlnm(ITEM_URL_NAME);
            item.setSrlnm(ITEM_URL_NAME);
            item.setCtgry(category);
            item.setWnr(owner);

            List<AvailableInStore> availableInStores = new ArrayList<AvailableInStore>(1);
            AvailableInStore ais = new AvailableInStore(STORE_URL_NAME, STORE_URL_NAME, StoreType.ONLINE);
            availableInStores.add(ais);
            item.setStrs(availableInStores);

            item = itemService.saveItem(item);
            assertNotNull("Item is null", item);
            assertNotNull("Item id is null", item.getId());

            // index item
            List<Item> items = new ArrayList<Item>(1);
            items.add(item);
            searchService.indexItems(items);
        } catch (SearchException e) {
            log.error(e.getMessage());
            fail(e.getMessage());
        }
        SpringSecurityHelper.unsecureChannel();
        log.info("Set up complete");
    }

    @After
    public void tearDown() {
        log.info("Starting tear down...");
        SpringSecurityHelper.secureChannel();

        // remove store
        if (store != null) {
            storeService.removeStore(STORE_URL_NAME);

            localCacheEvictionService.evictStore(STORE_URL_NAME);

            store = storeService.findStoreByUrlName(STORE_URL_NAME);
            assertNull("Store did not get deleted", store);
        }

        if (category != null) {
            categoryService.removeCategory(category.getRlnm());

            localCacheEvictionService.evictCategory(CATEGORY_URL_NAME);

            category = categoryService.findCategoryByUrlName(CATEGORY_URL_NAME);

            assertNull("Category did not get deleted", category);
        }

        if (item != null) {
            itemService.removeItem(item.getRlnm());

            localCacheEvictionService.evictItem(ITEM_URL_NAME + "_" + CATEGORY_URL_NAME);

            item = itemService.findItemByUrlName(ITEM_URL_NAME);

            assertNull("Item did not get deleted", item);
        }

        if (owner != null) {
            ownerService.removeOwner(owner.getRlnm());

            localCacheEvictionService.evictOwner(OWNER_URL_NAME);

            owner = ownerService.findOwnerByUrlName(OWNER_URL_NAME);

            assertNull("Owner did not get deleted", owner);
        }

        SpringSecurityHelper.unsecureChannel();
        log.info("Tear down complete");
    }

    @Test
    public void testFindStoreByUrlName() {
        log.info("Finding store by url name");

        Store store = storeService.findStoreByUrlName(STORE_URL_NAME);
        assertNotNull("Store is null", store);

        log.info("Finding store by url name complete.");
    }

    @Test
    public void testFindStoreByAffiliateUrlName() {
        log.info("Finding store by affiliate url name");

        Store store = storeService.findStoreByAffiliateUrlName(STORE_URL_NAME);
        assertNotNull("Store is null", store);

        log.info("Finding store by affiliate url name complete.");
    }

    @Test
    public void testFindStoreByMerchantId() {
        log.info("Finding store by merchant id");

        Store store = storeService.findStoreByMerchantId(STORE_URL_NAME);
        assertNotNull("Store is null", store);

        log.info("Finding store by merchant id complete.");
    }

    @Test
    public void testFindStoresByUrlName() {
        log.info("Find stores by url name.");
        List<String> storeUrlNames = new ArrayList<String>();
        storeUrlNames.add(STORE_URL_NAME);
        List<Store> stores = storeService.findStoresByUrlName(storeUrlNames);
        assertNotNull("Stores is null", stores);
        assertEquals("Store size incorrect", 1, stores.size(), 0);
        for (Store store : stores) {
            assertNotNull("Store is null", store);
            assertTrue("Find stores by url name found wrong", store.getRlnm().contains(STORE_URL_NAME));
        }
        log.info("Find stores by url name complete.");
    }

    @Test
    public void testFindStores() {
        log.info("Find stores");
        Page<Store> page = storeService.findStores(0, 1);
        assertNotNull("Stores is null", page);
        assertTrue("Stores has no content", page.hasContent());
        assertEquals("Store size incorrect", 1, page.getNumberOfElements(), 0);
        log.info("Find stores complete.");
    }

    @Test
    public void testFindStoresByFirstLetter() {
        log.info("Find stores with first letter...");
        Page<Store> page = storeService.findStores(0, 1, "S");
        assertNotNull("Stores is null", page);
        assertTrue("Stores has no content", page.hasContent());
        assertEquals("Store size incorrect", 1, page.getNumberOfElements(), 0);
        log.info("Find stores complete.");
    }

    @Test
    public void testFindStoreCount() {
        log.info("Find store count...");
        Long count = storeService.findStoreCount();
        assertNotNull("Store count is null", count);
        assertTrue("Store size incorrect", count > 0);
        log.info("Find store count complete.");
    }

    @Test
    public void testFindStoreAggregates() {
        if (searchService.isItemCoreSolrServerAvailable()) {
            log.info("Find store aggregates");
            List<NameValueAggregate> stores = storeService.findStoreAggregates(null);
            assertNotNull("Stores is null", stores);
            assertTrue("Store size incorrect", stores.size() > 0);

            StoreAggregateQuery query = new StoreAggregateQuery();
            query.setFilterOnLetter(STORE_URL_NAME.substring(0, 1));
            stores = storeService.findStoreAggregates(query);
            assertNotNull("Stores is null", stores);
            assertTrue("Store size incorrect", stores.size() > 0);

            log.info("Find store aggregates complete.");
        }
    }

    @Test
    public void testFindStoreAggregateDetails() {
        if (searchService.isItemCoreSolrServerAvailable()) {
            log.info("Find extended store aggregates");
            long start1 = System.currentTimeMillis();
            StoresSearchResult stores = storeService.findStoreAggregateDetails(null);
            long end1 = System.currentTimeMillis();
            assertNotNull("Stores is null", stores);
            assertNotNull("Stores is null", stores.getStores());

            log.info("Let's call the same method again. This time caching should kick in and the call should be a lot faster.");
            long start2 = System.currentTimeMillis();
            stores = storeService.findStoreAggregateDetails(null);
            long end2 = System.currentTimeMillis();
            assertNotNull("Stores is null", stores);
            assertNotNull("Stores is null", stores.getStores());

            assertTrue("The time it took to execute the first call took less time than the second call", end1 - start1 > end2 - start2);

            StoreAggregateQuery query = new StoreAggregateQuery();
            query.setFilterOnLetter(STORE_URL_NAME.substring(0, 1));
            stores = storeService.findStoreAggregateDetails(query);
            assertNotNull("Stores is null", stores);
            assertNotNull("Stores is null", stores.getStores());

            log.info("Find store aggregates complete.");
        }
    }

    @Test
    public void testFindAllStores() {
        log.info("Find all stores");
        List<Store> stores = storeService.findAllStores(50);

        assertNotNull("Stores is null", stores);
        assertTrue("Store size is incorrect", stores.size() > 0);

        log.info("Find all stores complete.");
    }


}
