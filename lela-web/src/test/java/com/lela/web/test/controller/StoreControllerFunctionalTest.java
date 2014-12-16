/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.web.test.controller;

import com.lela.commons.service.CategoryService;
import com.lela.commons.service.ItemService;
import com.lela.commons.service.OwnerService;
import com.lela.commons.service.SearchException;
import com.lela.commons.service.SearchService;
import com.lela.commons.service.StoreService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.domain.document.Motivator;
import com.lela.domain.dto.LocationQuery;
import com.lela.domain.enums.MotivatorSource;
import com.lela.util.test.integration.search.SolrServerRunning;
import com.lela.domain.document.AvailableInStore;
import com.lela.domain.document.Category;
import com.lela.domain.document.Item;
import com.lela.domain.document.Owner;
import com.lela.domain.document.Store;
import com.lela.domain.document.User;
import com.lela.domain.dto.search.GroupedSearchResult;
import com.lela.domain.dto.search.SearchResult;
import com.lela.domain.enums.StoreType;
import com.lela.web.test.AbstractFunctionalTest;
import com.lela.web.web.controller.StoreController;
import com.lela.commons.web.utils.WebConstants;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Bjorn Harvold
 * Date: 1/18/12
 * Time: 2:04 AM
 * Responsibility:
 */
public class StoreControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(StoreControllerFunctionalTest.class);
    private static final String CATEGORY_URL_NAME = "storecontrollertest";
    private static final String CATEGORY_NAME = "storecontrollertest";
    private static final String ITEM1_URL_NAME = "storeurlname";
    private static final String ITEM1_NAME = "storename";
    private static final String STORE_URL_NAME = "storestore";
    private static final String STORE_NAME = "storestore";
    private static final String STORE_URL_NAME_2 = "storestore2";
    private static final String STORE_NAME_2 = "storestore2";
    
    @Rule
    public SolrServerRunning serverRunning = SolrServerRunning.isRunning();

    @Autowired
    private StoreController storeController;

    @Autowired
    private SearchService searchService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private CategoryService categoryService;

    private Store theStore;
    private Store theStore2;
    private AvailableInStore store;
    private Item item;
    private Category cat;
    private Owner owner;
    private static final String OWNER_URL_NAME = "storeownerstore";
    private static final String OWNER_NAME = "storeownerstore";

    @Before
    public void setUp() {
        SpringSecurityHelper.secureChannel();
        try {
            searchService.deleteItemsByQuery("ctgry:" + CATEGORY_URL_NAME);

            owner = new Owner();
            owner.setRlnm(OWNER_URL_NAME);
            owner.setNm(OWNER_NAME);
            owner = ownerService.saveOwner(owner);
            
            theStore = new Store();
            theStore.setRlnm(STORE_URL_NAME);
            theStore.setNm(STORE_NAME);

            theStore = storeService.saveStore(theStore);

            theStore2 = new Store();
            theStore2.setRlnm(STORE_URL_NAME_2);
            theStore2.setSrlnm(STORE_URL_NAME_2);
            theStore2.setNm(STORE_NAME_2);

            theStore2 = storeService.saveStore(theStore2);
            
            store = new AvailableInStore(theStore.getNm(), theStore.getRlnm(), StoreType.ONLINE);
            List<AvailableInStore> stores = new ArrayList<AvailableInStore>(1);
            stores.add(store);

            cat = new Category();
            cat.setRlnm(CATEGORY_URL_NAME);
            cat.setNm(CATEGORY_NAME);

            cat = categoryService.saveCategory(cat);

            item = new Item();
            item.setId(ObjectId.get());
            item.setPc(item.getId().toString());
            item.setLlpc(item.getId().toString());
            item.setRlnm(ITEM1_URL_NAME);
            item.setNm(ITEM1_NAME);

            item.setStrs(stores);
            item.setCtgry(cat);
            item.setWnr(owner);

            item = itemService.saveItem(item);

            List<Item> items = new ArrayList<Item>(1);
            items.add(item);
            searchService.indexItems(items);

            Thread.sleep(1000);

        } catch (Exception e) {
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

            owner = ownerService.removeOwner(owner.getRlnm());
            theStore = storeService.removeStore(theStore.getRlnm());
            theStore2 = storeService.removeStore(theStore2.getRlnm());
            cat = categoryService.removeCategory(cat.getRlnm());
            item = itemService.removeItem(item.getRlnm());

        } catch (SearchException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }

        SpringSecurityHelper.unsecureChannel();
    }
    
    @Test
    public void testItemsInStoreSearch() {
        SpringSecurityHelper.secureChannel();
        log.info("Testing items in store search...");

        Model model = new BindingAwareModelMap();
        HttpSession session = new MockHttpSession();

        try {
            List<String> ids = new ArrayList<String>(1);
            ids.add(item.getId().toString());

            log.info("First we want to search for items without motivators");
            ModelAndView view = storeController.showStore(STORE_URL_NAME, session, model, Locale.US);
            assertNotNull("View is null", view);
            assertEquals("view name is incorrect", "store.index", view.getViewName());
            assertNotNull("items is null", model.asMap().get(WebConstants.SEARCH_RESULTS));
            GroupedSearchResult searchResult = (GroupedSearchResult)  model.asMap().get(WebConstants.SEARCH_RESULTS);
            assertTrue("Stroller results missing", searchResult.getItems().containsKey(CATEGORY_URL_NAME));
            long resultSize1 = searchResult.getItems().get(CATEGORY_URL_NAME).getTotalElements();
            assertTrue("Result size incorrect", resultSize1 > 0);

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
            view = storeController.showStore(STORE_URL_NAME, session, model, Locale.US);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "store.index", view.getViewName());
            assertNotNull("items is null", model.asMap().get(WebConstants.SEARCH_RESULTS));
            searchResult = (GroupedSearchResult) model.asMap().get(WebConstants.SEARCH_RESULTS);
            assertTrue("Results missing", searchResult.getRelevantItems().containsKey(CATEGORY_URL_NAME));
            long resultSize2 = searchResult.getRelevantItems().get(CATEGORY_URL_NAME).getTotalElements();
            assertTrue("Regular search should be equal relevant search results", resultSize1 == resultSize2);

            model = new BindingAwareModelMap();
            view = storeController.showStoreCategory(STORE_URL_NAME, CATEGORY_URL_NAME, 0, session, model, Locale.US);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "store.category", view.getViewName());
            assertNotNull("items is null", model.asMap().get(WebConstants.SEARCH_RESULTS));
            SearchResult searchResult1 = (SearchResult) model.asMap().get(WebConstants.SEARCH_RESULTS);
            assertTrue("Results missing", searchResult1.getRelevantItems().hasContent());
            long resultSize3 = searchResult1.getRelevantItems().getTotalElements();
            assertTrue("Regular search should be equal relevant search results", resultSize2 == resultSize3);

            log.info("Nothing left to do so we delete all the Solr documents");
            searchService.deleteItemsById(ids);
            userService.removeUser(user);

            model = new BindingAwareModelMap();
            log.info("Verify that all the specified documents have been deleted");
            view = storeController.showStore(STORE_URL_NAME, session, model, Locale.US);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "store.index", view.getViewName());
            assertNull("items is not null", model.asMap().get(WebConstants.SEARCH_RESULTS));
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        SpringSecurityHelper.unsecureChannel();
        log.info("Testing items in store search complete");
    }

    @Test
    public void testSeoFriendlyStore() {
        log.info("Testing an store object that has srlnm populated...");

        Model model = new BindingAwareModelMap();
        HttpSession session = new MockHttpSession();

        try {
            ModelAndView view = storeController.showStore(STORE_URL_NAME_2, session, model, Locale.US);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "/" + STORE_URL_NAME_2 + "/s?rlnm=" + STORE_URL_NAME_2, ((RedirectView)view.getView()).getUrl());

            model = new BindingAwareModelMap();
            session = new MockHttpSession();
            String theView = storeController.showStoreSEO(STORE_URL_NAME_2, STORE_URL_NAME_2, session, model, Locale.US);
            assertNotNull("theView is null", theView);
            assertEquals("View name is incorrect", "store.index", theView);

            view = storeController.showStoreCategory(STORE_URL_NAME_2, CATEGORY_URL_NAME, 0, session, model, Locale.US);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "/" + STORE_URL_NAME_2 + "/null/s?rlnm=" + STORE_URL_NAME_2 + "&crlnm=" + CATEGORY_URL_NAME, ((RedirectView)view.getView()).getUrl());

            model = new BindingAwareModelMap();
            session = new MockHttpSession();
            theView = storeController.showStoreCategorySEO(STORE_URL_NAME_2, CATEGORY_URL_NAME, STORE_URL_NAME_2, CATEGORY_URL_NAME, 0, session, model, Locale.US);
            assertNotNull("theView is null", theView);
            assertEquals("View name is incorrect", "store.category", theView);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing an store object that has srlnm populated complete");
    }

    @Test
    public void testShowSeoStores() {
        log.info("Testing seo stores...");

        Model model = new BindingAwareModelMap();

        try {
            String view = storeController.showStores(model);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "stores.index", view);
            assertNotNull("Stores are null", model.asMap().get(WebConstants.STORES));

            model = new BindingAwareModelMap();
            String theView = storeController.showStoresByName(STORE_URL_NAME_2.substring(0, 1), model);
            assertNotNull("theView is null", theView);
            assertEquals("View name is incorrect", "stores.index", theView);

        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing seo stores complete");
    }

    @Test
    public void testShowSeoLocalStores() {
        log.info("Testing seo local stores...");

        Model model = new BindingAwareModelMap();
        HttpSession session = new MockHttpSession();
        HttpServletRequest request = new MockHttpServletRequest();
        LocationQuery query = new LocationQuery();

        try {
            query.setZipcode("10024");
            String view = storeController.showLocalStores(query, request, session, model);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "local.stores.index", view);
            assertNotNull("Branches are null", model.asMap().get(WebConstants.BRANCHES));
            assertNotNull("Zipcode is null", model.asMap().get(WebConstants.ZIPCODE));

            model = new BindingAwareModelMap();
            session = new MockHttpSession();
            request = new MockHttpServletRequest();
            view = storeController.showLocalStores("10014", request, session, model);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "local.stores.index", view);
            assertNotNull("Branches are null", model.asMap().get(WebConstants.BRANCHES));
            assertNotNull("Zipcode is null", model.asMap().get(WebConstants.ZIPCODE));

        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing seo local stores complete");
    }
}
