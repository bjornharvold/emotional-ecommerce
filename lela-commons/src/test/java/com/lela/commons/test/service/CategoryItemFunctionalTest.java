/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.test.service;

import com.lela.commons.comparator.ItemOwnerComparator;
import com.lela.commons.service.CategoryService;
import com.lela.commons.service.ItemService;
import com.lela.commons.service.LocalCacheEvictionService;
import com.lela.commons.service.OwnerService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.test.AbstractFunctionalTest;
import com.lela.domain.document.AvailableInStore;
import com.lela.domain.document.Category;
import com.lela.domain.document.Item;
import com.lela.domain.document.Owner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Bjorn Harvold
 * Date: 6/4/12
 * Time: 7:53 PM
 * Responsibility:
 */
public class CategoryItemFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(CategoryItemFunctionalTest.class);
    private static final String CATEGORY_NAME = CategoryItemFunctionalTest.class.getSimpleName() + "_category";
    private static final String ITEM_NAME = CategoryItemFunctionalTest.class.getSimpleName() + "_item";
    private static final String OWNER_NAME = CategoryItemFunctionalTest.class.getSimpleName() + "_owner";
    private static final String CATEGORY_URL_NAME = CATEGORY_NAME;
    private static final String OWNER_URL_NAME = OWNER_NAME;
    private static final String ITEM_URL_NAME = ITEM_NAME;
    private static final String ITEM1_URL_NAME = "teststroller";
    private static final String ITEM2_URL_NAME = "teststroller2";
    private static final String FUNKYCRAZYURLNAME_123 = "funkycrazyurlname123";

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private LocalCacheEvictionService localCacheEvictionService;

    private Item item;
    private Item item1;
    private Item item2;
    private Category category;
    private Owner owner;

    @Before
    public void setup() {
        SpringSecurityHelper.secureChannel();
        item1 = loadToDatabase(Item.class, ITEM1_URL_NAME);
        item2 = loadToDatabase(Item.class, ITEM2_URL_NAME);

        category = new Category();
        category.setNm(CATEGORY_NAME);
        category.setRlnm(CATEGORY_URL_NAME);

        owner = new Owner();
        owner.setNm(OWNER_NAME);
        owner.setRlnm(OWNER_URL_NAME);

        log.info("First we create a category for the item");
        category = categoryService.saveCategory(category);
        assertNotNull("Category is missing", category);
        assertNotNull("Category is missing an id", category.getId());

        log.info("Then we create an owner for the item");
        owner = ownerService.saveOwner(owner);
        assertNotNull("Owner is null", owner);
        assertNotNull("Owner is missing an id", owner.getId());

        item = new Item();
        item.setNm(ITEM_NAME);
        item.setRlnm(ITEM_URL_NAME);
        item.setWnr(owner);
        item.setCtgry(category);
        item = itemService.saveItem(item);
        assertNotNull("Item is missing an id", item.getId());
        assertNotNull("Item is missing an owner", item.getWnr());
        assertNotNull("Item is missing an category", item.getCtgry());

        SpringSecurityHelper.unsecureChannel();
    }

    @After
    public void tearDown() {
        SpringSecurityHelper.secureChannel();
        if (category != null) {
            categoryService.removeCategory(category.getRlnm());
        }
        if (item != null) {
            itemService.removeItem(item.getRlnm());
        }
        if (owner != null) {
            ownerService.removeOwner(owner.getRlnm());
        }

        // Load the item to be tested from a known state for each test
        localCacheEvictionService.evictItem(ITEM1_URL_NAME);
        localCacheEvictionService.evictItem(ITEM2_URL_NAME);
        localCacheEvictionService.evictCategory(CATEGORY_URL_NAME);
        localCacheEvictionService.evictOwner(OWNER_URL_NAME);

        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testItemCategoryOwner() {
        log.info("Create an item and save it");

        try {
            item = itemService.saveItem(item);
            fail("Item should not be able to be saved here. Missing credentials.");
        } catch (Exception ex) {
            log.info("Tried to save item without credentials. An exception was expected: " + ex.getMessage());
        }

        log.info("Securing channel...");
        SpringSecurityHelper.secureChannel();
        log.info("Channel secured");

        category = categoryService.findCategoryByUrlName(CATEGORY_URL_NAME);
        assertNotNull("Category is null", category);
        assertNotNull("Category ID is null", category.getId());

        log.info("Retrieving categories successfully");

        log.info("Retrieving items...");

        item = itemService.findItemByUrlName(ITEM_URL_NAME);
        assertNotNull("Item is missing", item);
        assertNotNull("Item is missing an id", item.getId());

        log.info("Items retrieved successfully");

        log.info("Deleting items...");
        itemService.removeItem(item.getRlnm());

        item = itemService.findItemByUrlName(ITEM_URL_NAME);
        assertNull("Item still exists", item);

        log.info("Deleted items successfully");

        log.info("Deleting owner...");
        ownerService.removeOwner(owner.getRlnm());

        owner = ownerService.findOwnerByUrlName(OWNER_URL_NAME);
        assertNull("Owner still exists", owner);
        log.info("Deleted owner successfully");

        log.info("Deleting categories...");

        categoryService.removeCategory(category.getRlnm());

        category = categoryService.findCategoryByUrlName(CATEGORY_URL_NAME);
        assertNull("Category still exists", category);

        log.info("Deleted category successfully");

        log.info("Test complete!");
        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testCategories() {
        List<Category> list = categoryService.findCategories();
        assertNotNull("Categories are null", list);
        assertTrue("Categories are empty", list.size() > 0);
    }

    @Test
    public void testFindCategory() {
        log.info("Test find category by Id");
        Category category1 = categoryService.findCategoryByUrlName(CATEGORY_URL_NAME);
        Category categoryById = categoryService.findCategoryById(category1.getId());
        assertNotNull("Category by Url Name are null", category1);
        assertNotNull("Category by Id are null", categoryById);
        assertEquals("Show Network tile view is incorrect", category1.getId(), categoryById.getId());
        log.info("Test find category by Id complete.");
    }

    @Test
    public void testFindItemsByUrlName() {
        log.info("Find items by URL name.");
        List<String> itemsUrlNames = new ArrayList<String>();
        itemsUrlNames.add(ITEM1_URL_NAME);
        itemsUrlNames.add(ITEM2_URL_NAME);
        List<Item> items = itemService.findItemsByUrlName(itemsUrlNames);
        assertNotNull("Items are null", items);
        assertEquals("Show Network tile view is incorrect", items.size(), 2);
        log.info("Find items by URL name complete.");

        Collections.sort(items, new ItemOwnerComparator());

        assertTrue("ItemOwner sorting incorrect", items.get(0).getRlnm().equals(ITEM2_URL_NAME));
    }

    @Test
    public void testUpdateAvailableInStoresOnItem() {
        SpringSecurityHelper.secureChannel();
        log.info("Testing updating available stores on item...");

        itemService.removeItem(FUNKYCRAZYURLNAME_123);

        Item item = new Item();
        item.setRlnm(FUNKYCRAZYURLNAME_123);
        item.setNm("Funky Crazy Name 123");
        item.setCtgry(category);
        item = itemService.saveItem(item);

        assertNotNull("Item id is null", item.getId());

        log.info("Now we update this item with available stores");
        List<AvailableInStore> stores = new ArrayList<AvailableInStore>(1);
        AvailableInStore store = new AvailableInStore();
        store.setRlnm("crazyfunkystoreurlname");
        store.setNm("Crazy Funky Store Name");
        stores.add(store);
        item.setStrs(stores);

        itemService.updateAvailableStoresOnItem(item);

        Item item1 = itemService.findItemByUrlName(item.getRlnm());
        assertNotNull("Item is null", item1);
        assertNotNull("Stores list is empty", item1.getStrs());

        itemService.removeItem(item1.getRlnm());
        log.info("Testing updating available stores on item...");
        SpringSecurityHelper.unsecureChannel();
    }
}
