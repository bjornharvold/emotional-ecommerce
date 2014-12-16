/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.test.service;

import com.lela.commons.service.ProfileService;
import com.lela.domain.ApplicationConstants;
import com.lela.commons.service.CategoryService;
import com.lela.commons.service.FunctionalFilterService;
import com.lela.commons.service.ItemService;
import com.lela.commons.service.OwnerService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.domain.document.Attribute;
import com.lela.domain.document.Category;
import com.lela.domain.document.FunctionalFilter;
import com.lela.domain.document.FunctionalFilterOption;
import com.lela.domain.document.Item;
import com.lela.domain.document.Motivator;
import com.lela.domain.document.Owner;
import com.lela.domain.document.RelevantItem;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.CategoryItemsQuery;
import com.lela.domain.dto.ItemPage;
import com.lela.commons.service.ProductEngineService;
import com.lela.commons.test.AbstractFunctionalTest;
import com.lela.domain.enums.FunctionalFilterDomainType;
import com.lela.domain.enums.FunctionalFilterType;
import com.lela.domain.enums.MotivatorSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Bjorn Harvold
 * Date: 6/23/11
 * Time: 11:16 AM
 * Responsibility: Fully tests the ProductEngineService from front to back.
 */
public class ProductEngineServiceFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(ProductEngineServiceFunctionalTest.class);
    private static final String CATEGORY_URL_NAME = "ProductEngineServiceFunctionalTest";

    @Autowired
    private ProductEngineService productEngineService;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FunctionalFilterService functionalFilterService;

    @Autowired
    private ProfileService profileService;

    private Category category = null;
    private Item item1 = null;
    private Item item2 = null;
    private FunctionalFilter priceRangeFilter = null;
    private FunctionalFilter brandFilter = null;
    private FunctionalFilter bestForFilter = null;
    private FunctionalFilter typeFilter = null;
    private FunctionalFilter categoryFilter = null;
    private Owner jeep = null;
    private Owner lotus = null;
    private User user = null;
    private UserSupplement us = null;

    @Before
    public void setUp() {
        SpringSecurityHelper.secureChannel();

        log.info("Set up test data for functional test...");
        jeep = new Owner();
        jeep.setNm("Jeep");
        jeep.setRlnm("jeep");

        jeep = ownerService.saveOwner(jeep);

        lotus = new Owner();
        lotus.setNm("Lotus");
        lotus.setRlnm("lotus");

        lotus = ownerService.saveOwner(lotus);

        category = new Category();
        category.setNm(CATEGORY_URL_NAME);
        category.setRlnm(CATEGORY_URL_NAME);
        category.setSrlnm(CATEGORY_URL_NAME);

        category = categoryService.saveCategory(category);
        assertNotNull("Category 2 is null", category);
        assertNotNull("Category 2 ID is null", category.getId());

        priceRangeFilter = new FunctionalFilter();
        priceRangeFilter.setRlnm(CATEGORY_URL_NAME);
        priceRangeFilter.setKy("price.range");
        priceRangeFilter.setDtky("LowestPrice");
        priceRangeFilter.setTp(FunctionalFilterType.DYNAMIC_RANGE);
        priceRangeFilter.setDtp(FunctionalFilterDomainType.CATEGORY);
        priceRangeFilter.setRdr(1);

        List<FunctionalFilterOption> options = new ArrayList<FunctionalFilterOption>(2);
        FunctionalFilterOption option1 = new FunctionalFilterOption(ApplicationConstants.DYNAMIC_RANGE_LOW, 10, 1, false);
        FunctionalFilterOption option2 = new FunctionalFilterOption(ApplicationConstants.DYNAMIC_RANGE_HIGH, 1000, 3, false);
        options.add(option1);
        options.add(option2);

        priceRangeFilter.setPtns(options);

        priceRangeFilter = functionalFilterService.saveFunctionalFilter(priceRangeFilter);

        brandFilter = new FunctionalFilter();
        brandFilter.setRlnm(CATEGORY_URL_NAME);
        brandFilter.setKy("by.brand");
        brandFilter.setTp(FunctionalFilterType.BRAND);
        brandFilter.setDtp(FunctionalFilterDomainType.CATEGORY);
        brandFilter.setRdr(2);

        options = new ArrayList<FunctionalFilterOption>(2);
        option1 = new FunctionalFilterOption(jeep.getRlnm(), jeep.getNm(), null, false);
        option2 = new FunctionalFilterOption(lotus.getRlnm(), lotus.getNm(), null, false);
        options.add(option1);
        options.add(option2);
        brandFilter.setPtns(options);

        brandFilter = functionalFilterService.saveFunctionalFilter(brandFilter);

        bestForFilter = new FunctionalFilter();
        bestForFilter.setRlnm(CATEGORY_URL_NAME);
        bestForFilter.setKy("best.for");
        bestForFilter.setTp(FunctionalFilterType.MULTIPLE_CHOICE_MULTIPLE_ANSWER_AND);
        bestForFilter.setDtp(FunctionalFilterDomainType.CATEGORY);
        bestForFilter.setRdr(4);

        options = new ArrayList<FunctionalFilterOption>(2);
        option1 = new FunctionalFilterOption("BestForDriving", "true", 1, false);
        option2 = new FunctionalFilterOption("BestForSleeping", "true", 3, false);
        options.add(option1);
        options.add(option2);

        bestForFilter.setPtns(options);

        bestForFilter = functionalFilterService.saveFunctionalFilter(bestForFilter);

        typeFilter = new FunctionalFilter();
        typeFilter.setRlnm(CATEGORY_URL_NAME);
        typeFilter.setKy("stroller.type");
        typeFilter.setTp(FunctionalFilterType.MULTIPLE_CHOICE_MULTIPLE_ANSWER_OR);
        typeFilter.setDtp(FunctionalFilterDomainType.CATEGORY);
        typeFilter.setRdr(5);

        options = new ArrayList<FunctionalFilterOption>(2);
        option1 = new FunctionalFilterOption("LowRider", "true", 1, false);
        option2 = new FunctionalFilterOption("Convertible", "true", 3, false);
        options.add(option1);
        options.add(option2);

        typeFilter.setPtns(options);

        typeFilter = functionalFilterService.saveFunctionalFilter(typeFilter);

        categoryFilter = new FunctionalFilter();
        categoryFilter.setRlnm(CATEGORY_URL_NAME);
        categoryFilter.setKy("category");
        categoryFilter.setTp(FunctionalFilterType.CATEGORY);
        categoryFilter.setDtp(FunctionalFilterDomainType.DEPARTMENT);
        categoryFilter.setRdr(6);

        options = new ArrayList<FunctionalFilterOption>(1);
        option1 = new FunctionalFilterOption(CATEGORY_URL_NAME, CATEGORY_URL_NAME, 1, false);
        options.add(option1);

        categoryFilter = functionalFilterService.saveFunctionalFilter(categoryFilter);

        item1 = new Item();
        item1.setCtgry(category);
        item1.setWnr(jeep);
        item1.setNm("categorycontrollertestitem1");
        item1.setRlnm("categorycontrollertestitem1");
        List<Attribute> attrs = new ArrayList<Attribute>(1);
        Attribute attr1 = new Attribute("LowestPrice", 1.99d);
        Attribute attr2 = new Attribute("BestForDriving", "true");
        Attribute attr3 = new Attribute("LowRider", "true");
        attrs.add(attr1);
        attrs.add(attr2);
        attrs.add(attr3);
        item1.setAttrs(attrs);
        item1.setSbttrs(attrs);

        Map<String, Integer> motivators = new HashMap<String, Integer>();
        motivators.put("A", 9);
        motivators.put("B", 0);
        motivators.put("C", 9);
        motivators.put("D", 0);
        motivators.put("E", 9);
        motivators.put("F", 0);
        motivators.put("G", 9);
        item1.setMtvtrs(motivators);

        item1 = itemService.saveItem(item1);
        assertNotNull("Item 1 is null", item1);
        assertNotNull("Item 1 ID is null", item1.getId());

        item2 = new Item();
        item2.setCtgry(category);
        item2.setWnr(lotus);
        item2.setNm("categorycontrollertestitem2");
        item2.setRlnm("categorycontrollertestitem2");
        attrs = new ArrayList<Attribute>(1);
        attr1 = new Attribute("LowestPrice", 10.99d);
        attr2 = new Attribute("BestForSleeping", "true");
        attr3 = new Attribute("Convertible", "true");
        attrs.add(attr1);
        attrs.add(attr2);
        attrs.add(attr3);
        item2.setAttrs(attrs);
        item2.setSbttrs(attrs);
        item2.setMtvtrs(motivators);

        item2 = itemService.saveItem(item2);
        assertNotNull("Item 2 is null", item2);
        assertNotNull("Item 2 ID is null", item2.getId());

        user = createRandomUser(true);
        us = createRandomUserSupplement(user);
        us = userService.saveUserSupplement(us);
        user = profileService.registerTestUser(user);

        log.info("All test data has been created for this functional test");
        SpringSecurityHelper.unsecureChannel();
    }

    @After
    public void tearDown() {
        SpringSecurityHelper.secureChannel();

        if (category != null) {
            categoryService.removeCategory(category.getRlnm());
        }

        if (item1 != null) {
            itemService.removeItem(item1.getRlnm());
        }

        if (item2 != null) {
            itemService.removeItem(item2.getRlnm());
        }

        if (priceRangeFilter != null) {
            functionalFilterService.removeFunctionalFilter(priceRangeFilter.getId());
        }

        if (brandFilter != null) {
            functionalFilterService.removeFunctionalFilter(brandFilter.getId());
        }

        if (bestForFilter != null) {
            functionalFilterService.removeFunctionalFilter(bestForFilter.getId());
        }

        if (typeFilter != null) {
            functionalFilterService.removeFunctionalFilter(typeFilter.getId());
        }

        if (categoryFilter != null) {
            functionalFilterService.removeFunctionalFilter(categoryFilter.getId());
        }

        if (jeep != null) {
            ownerService.removeOwner(jeep.getRlnm());
        }

        if (lotus != null) {
            ownerService.removeOwner(lotus.getRlnm());
        }

        if (user != null) {
            userService.removeUser(user);
        }

        SpringSecurityHelper.unsecureChannel();
    }
    
    @Test
    public void testItemsWithFilters() {
        log.info("Testing in-memory item retrieval...");

        try {
            Category category = categoryService.findCategoryByUrlName(CATEGORY_URL_NAME);
            assertNotNull("Category is null", category);
            List<FunctionalFilter> functionalFilters = functionalFilterService.findFunctionalFiltersByUrlName(category.getRlnm());
            assertNotNull("Functional filters are null", category);
            assertNotNull("Functional filter list is null", functionalFilters);
            assertFalse("Functional filter list is empty", functionalFilters.isEmpty());

            CategoryItemsQuery query = new CategoryItemsQuery();
            query.setCategoryUrlName(category.getRlnm());
            query.setUserCode(user.getCd());
            ItemPage<Item> page1 = productEngineService.findItemsByCategory(query);

            assertNotNull("Page is null", page1);
            assertNotNull("Page contents is null", page1.getContent());
            assertTrue("Page size is null", page1.getTotalElements() > 0);
            assertNotNull("Page filters are null", page1.getFilters());

            log.info("That should've retrieved the items but also cached them for the first time");

            log.info("Now we're going to use the filters and narrow the result set");
            Map<String, Map<String, String>> filters = new HashMap<String, Map<String, String>>();

            String priceRangeKey = "price.range";
            Map<String, String> priceRangeValues = new HashMap<String, String>(2);
            Integer high = (Integer) page1.getFilters().get(0).getFunctionalFilterOption("high").getVl();
            Integer low = (Integer) page1.getFilters().get(0).getFunctionalFilterOption("low").getVl();

            // narrow range
            high = high - 100;
            low = low + 50;

            priceRangeValues.put("high", high.toString());
            priceRangeValues.put("low", low.toString());
            filters.put(priceRangeKey, priceRangeValues);

            query = new CategoryItemsQuery();
            query.setCategoryUrlName(category.getRlnm());
            query.setUserCode(user.getCd());
            query.setFilters(filters);
            ItemPage<Item> page2 = productEngineService.findItemsByCategory(query);
            assertNotNull("Page is null", page2);
            assertNotNull("Page contents is null", page2.getContent());
            assertTrue("Page size is null", page2.getTotalElements() > 0);
            assertNotNull("Page filters are null", page2.getFilters());

            log.info("Now we compare the first result set with the second result set. They should be the same as there are no user presets involved.");
            assertTrue("Result set comparison incorrect. Default result set should produce less items", page1.getTotalElements() == page2.getTotalElements());

            log.info("Price filter works. Now we add a multiple choice filter.");
            String bestForFilterKey = "best.for";
            Map<String, String> bestForValues = new HashMap<String, String>(2);
            bestForValues.put("BestForInfants", "true");
            filters.put(bestForFilterKey, bestForValues);

            query = new CategoryItemsQuery();
            query.setCategoryUrlName(category.getRlnm());
            query.setFilters(filters);
            query.setUserCode(user.getCd());
            ItemPage<Item> page3 = productEngineService.findItemsByCategory(query);
            assertNotNull("Page is null", page3);
            assertNotNull("Page contents is null", page3.getContent());
            assertTrue("Page size is null", page3.getTotalElements() > 0);
            assertNotNull("Page filters are null", page3.getFilters());

            log.info("Now we compare the second result set with the third result set. They should be the same as there are no user presets involved.");
            assertTrue("Result set comparison incorrect", page2.getTotalElements() == page3.getTotalElements());
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            fail(ex.getMessage());
        }

        log.info("Testing in-memory item retrieval complete");
    }

    @Test
    public void testCategoryFilter() {
        log.info("Testing category filter...");

        try {
            log.info("Now we're going to filter on category");
            Map<String, Map<String, String>> filters = new HashMap<String, Map<String, String>>();

            String filterKey = "category";
            Map<String, String> categoryValues = new HashMap<String, String>(2);

            categoryValues.put(CATEGORY_URL_NAME, CATEGORY_URL_NAME);
            filters.put(filterKey, categoryValues);

            CategoryItemsQuery query = new CategoryItemsQuery();
            query.setCategoryUrlName(category.getRlnm());
            query.setUserCode(user.getCd());
            query.setFilters(filters);
            ItemPage<Item> page2 = productEngineService.findItemsByCategory(query);
            assertNotNull("Page is null", page2);
            assertNotNull("Page contents is null", page2.getContent());
            assertTrue("Page size is null", page2.getTotalElements() > 0);
            assertNotNull("Page filters are null", page2.getFilters());

        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            fail(ex.getMessage());
        }

        log.info("Testing category filter complete");
    }

    @Test
    public void testFindHighestMatchingItemPerCategory() {
        Map<String, Integer> userMotivators = new HashMap<String, Integer>();
        userMotivators.put("A", 8);
        userMotivators.put("B", 9);
        userMotivators.put("C", 3);
        userMotivators.put("D", 8);
        userMotivators.put("E", 9);
        userMotivators.put("F", 8);
        userMotivators.put("G", 8);

        Motivator motivator = new Motivator(MotivatorSource.QUIZ, userMotivators);
        motivator = userService.saveMotivator(user.getCd(), motivator);
        assertNotNull("Motivator is null", motivator);

        Category category = categoryService.findCategoryByUrlName(CATEGORY_URL_NAME);
        List<Category> categories = new ArrayList<Category>();
        categories.add(category);

        Map<String, List<RelevantItem>> map = productEngineService.findHighestMatchingItemPerCategory(categories, user.getCd(), 1, 1);
        assertNotNull("Map of highest marching item per category", map);
        assertEquals("Maz size is incorrect", map.size(), 1, 0);

        Collection<List<RelevantItem>> collection = map.values();

        for (List<RelevantItem> list : collection) {
            assertEquals("Size not equal to 1", list.size(), 1);
        }
    }
}
