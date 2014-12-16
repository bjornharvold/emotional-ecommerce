/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.web.test.controller;

import com.lela.commons.service.CategoryService;
import com.lela.commons.service.ItemService;
import com.lela.commons.service.SearchException;
import com.lela.commons.service.SearchService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.domain.document.AvailableInStore;
import com.lela.domain.document.Category;
import com.lela.domain.document.Color;
import com.lela.domain.document.Motivator;
import com.lela.domain.document.Owner;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.enums.MotivatorSource;
import com.lela.domain.enums.StoreType;
import com.lela.util.test.integration.search.SolrServerRunning;
import com.lela.domain.document.Item;
import com.lela.domain.document.RelevantItem;
import com.lela.domain.document.User;
import com.lela.domain.dto.ItemPage;
import com.lela.domain.dto.RelevantItemSearchQuery;
import com.lela.domain.dto.search.CategoryCount;
import com.lela.domain.dto.search.SearchResult;
import com.lela.web.test.AbstractFunctionalTest;
import com.lela.web.web.controller.SearchController;
import com.lela.commons.web.utils.WebConstants;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Bjorn Harvold
 * Date: 1/18/12
 * Time: 2:04 AM
 * Responsibility:
 */
public class SearchControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(SearchControllerFunctionalTest.class);

    private static final String ITEM2_URL_NAME = "svenskkaviar";
    private static final String ITEM2_NAME = "Svensk";
    private static final String OWNER_SVERIGE_URLNAME = "sverige";
    private static final String OWNER_SVERIGE_NAME = "Sverige";
    private static final String ITEM1_URL_NAME = "norskgeitost";
    private static final String ITEM1_NAME = "Norsk";
    private static final String CATEGORY_NAME = "blah blah";
    private static final String OWNER_UNITEDSTATES_URL_NAME = "unitedstates";
    private static final String OWNER_UNITED_STATES_NAME = "United States";
    private static final String COLOR_RED = "red";
    private static final String COLOR_BLUE = "blue";
    private static final String COLOR_BLACK = "black";
    private static final String BEST_BUY_NAME = "Best Buy";
    private static final String BEST_BUY_URL_NAME = "bestbuy";
    private static final String TARGET_NAME = "Target";
    private static final String TARGET_URL_NAME = "target";
    private static final String BUY_COM_NAME = "buy.com";
    private static final String BUY_COM_URL_NAME = "buycom";
    private static final String CATEGORY_URL_NAME = "searchcontrollertest";

    @Rule
    public SolrServerRunning serverRunning = SolrServerRunning.isRunning();

    @Autowired
    private SearchController searchController;

    @Autowired
    private SearchService searchService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private CategoryService categoryService;

    private Category category;
    private List<Item> testDataList;

    @Before
    public void setUp() {
        SpringSecurityHelper.secureChannel();
        try {
            searchService.deleteItemsByQuery("ctgry:" + CATEGORY_URL_NAME);

            category = new Category();
            category.setRlnm(CATEGORY_URL_NAME);
            category.setNm(CATEGORY_NAME);

            category = categoryService.saveCategory(category);

            testDataList = createTestData(category);

            testDataList = itemService.saveItems(testDataList);

        } catch (SearchException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
        SpringSecurityHelper.unsecureChannel();
    }

    @After
    public void tearDown() {
        SpringSecurityHelper.secureChannel();
        try {
            searchService.deleteItemsByQuery("ctgry:" + CATEGORY_URL_NAME);

            categoryService.removeCategory(category.getRlnm());

            for (Item item : testDataList) {
                itemService.removeItem(item.getRlnm());
            }

        } catch (SearchException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testItemSearch() {
        log.info("Testing item search...");

        Model model = new BindingAwareModelMap();
        HttpSession session = new MockHttpSession();
        Locale locale = Locale.US;
        
        try {


            List<String> ids = new ArrayList<String>(testDataList.size());
            for (Item doc : testDataList) {
                ids.add(doc.getId().toString());
            }

            SpringSecurityHelper.secureChannel();
            searchService.indexItems(testDataList);
            SpringSecurityHelper.unsecureChannel();

            Thread.sleep(1000);

            log.info("First we want to search for items without motivators");
            RelevantItemSearchQuery query = new RelevantItemSearchQuery();
            query.setTerms(ITEM1_NAME);
            query.setCat(CATEGORY_URL_NAME);
            String view = searchController.searchForItem(query, session, model, locale);
            assertEquals("view name is incorrect", "search.result", view);
            assertNotNull("items is null", model.asMap().get(WebConstants.SEARCH_RESULTS));
            SearchResult searchResult = (SearchResult)  model.asMap().get(WebConstants.SEARCH_RESULTS);
            long resultSize1 = getTotalCategoryCount(searchResult);
            assertTrue("Result size incorrect", resultSize1 > 0);
            
            ItemPage<Item> page = searchResult.getItems();
            assertNotNull("ItemPage is null", page);
            long pageSize = page.getTotalElements();
            assertEquals("Category count doesn't match with page total size", resultSize1, pageSize, 0);

            model = new BindingAwareModelMap();
            User user = new User();
            Map<String, Integer> userMotivators = new HashMap<String, Integer>();
            userMotivators.put("A", 8);
            userMotivators.put("B", 9);
            userMotivators.put("C", 3);
            userMotivators.put("D", 8);
            userMotivators.put("E", 9);
            userMotivators.put("F", 8);
            userMotivators.put("G", 8);

            Motivator motivator = userService.saveMotivator(user.getCd(), new Motivator(MotivatorSource.QUIZ, userMotivators));

            session.setAttribute(WebConstants.USER, user);
            log.info("Then we want to test retrieving an item with motivators");
            view = searchController.searchForItem(query, session, model, locale);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "search.result", view);
            assertNotNull("items is null", model.asMap().get(WebConstants.SEARCH_RESULTS));
            searchResult = (SearchResult)  model.asMap().get(WebConstants.SEARCH_RESULTS);
            long resultSize2 = getTotalCategoryCount(searchResult);

            ItemPage<RelevantItem> relevantItemPage = searchResult.getRelevantItems();
            assertNotNull("ItemPage is null", relevantItemPage);
            long relevantItemPageSize = relevantItemPage.getTotalElements();
            assertEquals("Category count doesn't match with page total size", resultSize2, relevantItemPageSize, 0);
            
            assertTrue("Regular search should be equal relevant search results", resultSize1 == resultSize2);

            log.info("Let's search for 2 items at the same time");
            query.setTerms(ITEM2_NAME);
            query.setCat(CATEGORY_URL_NAME);
            view = searchController.searchForItem(query, session, model, locale);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "search.result", view);
            searchResult = (SearchResult) model.asMap().get(WebConstants.SEARCH_RESULTS);
            long resultSize3 = getTotalCategoryCount(searchResult);

            assertTrue("First result size should be equal second result size", resultSize2 == resultSize3);

            log.info("Nothing left to do so we delete all the Solr documents");
            SpringSecurityHelper.secureChannel();
            searchService.deleteItemsById(ids);
            userService.removeUser(user);
            SpringSecurityHelper.unsecureChannel();
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing item search complete");
    }

    private long getTotalCategoryCount(SearchResult searchResult) {
        long result = 0;

        if (searchResult != null && searchResult.getCategories() != null) {
            for (CategoryCount count : searchResult.getCategories()) {
                result += count.getCount();
            }
        }

        return result;
    }

    @Test
    public void testSplitDashedTerms() {
        log.info("Testing item search split dashed terms ...");

        Model model = new BindingAwareModelMap();
        HttpSession session = new MockHttpSession();

        try {
            String terms = "complete-coverage";
            RelevantItemSearchQuery query = new RelevantItemSearchQuery();

            String view = searchController.searchForItemAsGetWithTerms(terms, query, session, model, Locale.US);

            assertEquals("Term was not split on dashes", "complete coverage", query.getTerms());

        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing item search split dashed terms complete");
    }

    private List<Item> createTestData(Category category) {
        List<Item> result = new ArrayList<Item>();

        List<AvailableInStore> stores = new ArrayList<AvailableInStore>(3);
        AvailableInStore store1 = new AvailableInStore(BEST_BUY_NAME, BEST_BUY_URL_NAME, StoreType.ONLINE);
        AvailableInStore store2 = new AvailableInStore(TARGET_NAME, TARGET_URL_NAME, StoreType.ONLINE);
        AvailableInStore store3 = new AvailableInStore(BUY_COM_NAME, BUY_COM_URL_NAME, StoreType.ONLINE);
        stores.add(store1);
        stores.add(store2);
        stores.add(store3);

        // Item 1
        Item item1 = new Item();
        item1.setId(ObjectId.get());
        item1.setPc(item1.getId().toString());
        item1.setLlpc(item1.getId().toString());
        item1.setRlnm(ITEM1_URL_NAME);
        item1.setNm(ITEM1_NAME);
        item1.setStrs(stores);
        item1.setCtgry(category);

        Owner owner = new Owner();
        owner.setRlnm(OWNER_UNITEDSTATES_URL_NAME);
        owner.setNm(OWNER_UNITED_STATES_NAME);
        item1.setWnr(owner);

        List<Color> clrs = new ArrayList<Color>();
        Color red = new Color();
        red.setNm(COLOR_RED);
        clrs.add(red);
        Color blue = new Color();
        blue.setNm(COLOR_BLUE);
        clrs.add(blue);
        item1.setClrs(clrs);

        result.add(item1);

        // Item 2
        Item item2 = new Item();
        item2.setId(ObjectId.get());
        item2.setPc(item2.getId().toString());
        item2.setLlpc(item2.getId().toString());
        item2.setRlnm(ITEM2_URL_NAME);
        item2.setNm(ITEM2_NAME);
        item2.setCtgry(category);
        owner = new Owner();
        owner.setRlnm(OWNER_SVERIGE_URLNAME);
        owner.setNm(OWNER_SVERIGE_NAME);
        item2.setWnr(owner);
        item2.setStrs(stores);

        clrs = new ArrayList<Color>();
        Color black = new Color();
        black.setNm(COLOR_BLACK);
        clrs.add(black);
        clrs.add(blue);
        item2.setClrs(clrs);

        result.add(item2);

        return result;
    }
}
