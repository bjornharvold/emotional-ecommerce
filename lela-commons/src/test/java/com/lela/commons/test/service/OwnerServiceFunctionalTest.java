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
import com.lela.domain.dto.owner.OwnerAggregateQuery;
import com.lela.domain.dto.owner.OwnersSearchResult;
import com.lela.domain.dto.search.NameValueAggregate;
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
public class OwnerServiceFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(OwnerServiceFunctionalTest.class);
    private static final String STORE_URL_NAME = "OwnerServiceFunctionalTestStore";
    private static final String ITEM_URL_NAME = "OwnerServiceFunctionalTestItem";
    private static final String CATEGORY_URL_NAME = "OwnerServiceFunctionalTestCategory";
    private static final String OWNER_URL_NAME = "OwnerServiceFunctionalTestOwner";

    @Autowired
    private LocalCacheEvictionService localCacheEvictionService;

    @Autowired
    private OwnerService ownerService;
    
    @Autowired
    private StoreService storeService;

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
    public void testFindOwnerByUrlName() {
        log.info("Finding owner by url name");

        Owner owner1 = ownerService.findOwnerByUrlName(OWNER_URL_NAME);
        assertNotNull("Owner is null", owner1);

        log.info("Finding owner by url name complete.");
    }

    @Test
    public void testFindOwnerCount() {
        log.info("Finding owner count");

        Long count = ownerService.findOwnerCount();
        assertTrue("Count is incorrect", count > 0);

        log.info("Finding owner count complete.");
    }

    @Test
    public void testFindOwners() {
        log.info("Find owners");
        Page<Owner> page = ownerService.findOwners(0, 1);
        assertNotNull("Owners is null", page);
        assertTrue("Owners has no content", page.hasContent());
        assertEquals("Owner size incorrect", 1, page.getNumberOfElements(), 0);
        log.info("Find owners complete.");
    }

    @Test
    public void testFindOwnersByFirstLetter() {
        log.info("Find owners with first letter...");
        Page<Owner> page = ownerService.findOwners(0, 1, "O");
        assertNotNull("Owners is null", page);
        assertTrue("Owners has no content", page.hasContent());
        assertEquals("Owner size incorrect", 1, page.getNumberOfElements(), 0);
        log.info("Find owners complete.");
    }
    
    @Test
    public void testFindOwnerAggregates() {
        if (searchService.isItemCoreSolrServerAvailable()) {
            log.info("Find owner aggregates");
            List<NameValueAggregate> owners = ownerService.findOwnerAggregates(null);
            assertNotNull("Owners is null", owners);
            assertTrue("Owner size incorrect", owners.size() > 0);

            OwnerAggregateQuery query = new OwnerAggregateQuery();
            query.setFilterOnLetter(OWNER_URL_NAME.substring(0, 1));
            owners = ownerService.findOwnerAggregates(query);
            assertNotNull("Owners is null", owners);
            assertTrue("Owner size incorrect", owners.size() > 0);

            log.info("Find owner aggregates complete.");
        }
    }

    @Test
    public void testFindExtendedOwnerAggregates() {
        if (searchService.isItemCoreSolrServerAvailable()) {
            log.info("Find extended owner aggregates");
            long start1 = System.currentTimeMillis();
            OwnersSearchResult owners = ownerService.findOwnerAggregateDetails(new OwnerAggregateQuery());
            long end1 = System.currentTimeMillis();
            assertNotNull("Owners is null", owners);
            assertNotNull("Owners is null", owners.getOwners());

            log.info("Let's call the same method again. This time caching should kick in and the call should be a lot faster.");
            long start2 = System.currentTimeMillis();
            owners = ownerService.findOwnerAggregateDetails(new OwnerAggregateQuery());
            long end2 = System.currentTimeMillis();
            assertNotNull("Owners is null", owners);
            assertNotNull("Owners is null", owners.getOwners());

            assertTrue("The time it took to execute the first call took less time than the second call", end1 - start1 > end2 - start2);

            OwnerAggregateQuery query = new OwnerAggregateQuery();
            query.setFilterOnLetter(OWNER_URL_NAME.substring(0, 1));
            owners = ownerService.findOwnerAggregateDetails(query);
            assertNotNull("Owners is null", owners);
            assertNotNull("Owners is null", owners.getOwners());

            log.info("Find owner aggregates complete.");
        }
    }

    @Test
    public void testFindAllOwners() {
        log.info("Find all owners");
        List<Owner> owners = ownerService.findAllOwners(50);

        assertNotNull("Owners is null", owners);
        assertTrue("Owner size is incorrect", owners.size() > 0);

        log.info("Find all owners complete.");
    }


}
