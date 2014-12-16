package com.lela.web.test.controller;

import com.lela.commons.service.CategoryService;
import com.lela.commons.service.ItemService;
import com.lela.commons.service.OwnerService;
import com.lela.commons.service.SearchException;
import com.lela.commons.service.SearchService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.domain.document.Motivator;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.enums.MotivatorSource;
import com.lela.util.test.integration.search.SolrServerRunning;
import com.lela.domain.document.Category;
import com.lela.domain.document.Item;
import com.lela.domain.document.Owner;
import com.lela.domain.document.User;
import com.lela.domain.dto.search.GroupedSearchResult;
import com.lela.domain.dto.search.SearchResult;
import com.lela.web.test.AbstractFunctionalTest;
import com.lela.web.web.controller.OwnerController;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

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

public class OwnerControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(OwnerControllerFunctionalTest.class);
    private static final String CATEGORY_URL_NAME = "ownercontrollertest";
    private static final String CATEGORY_NAME = "ownercontrollertest";
    private static final String ITEM1_URL_NAME = "ownerurlname";
    private static final String ITEM1_NAME = "ownername";
    private static final String OWNER_URL_NAME = "ownerowner";
    private static final String OWNER_URL_NAME_2 = "ownerowner2";
    private static final String OWNER_NAME = "ownerowner";
    private static final String OWNER_NAME_2 = "ownerowner2";

    @Rule
    public SolrServerRunning serverRunning = SolrServerRunning.isRunning();

    @Autowired
    private OwnerController ownerController;

    @Autowired
    private SearchService searchService;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private CategoryService categoryService;

    private Owner nonSeoFriendlyOwner;
    private Owner seoFriendlyOwner;
    private Item item;
    private Category cat;

    @Before
    public void setUp() {
        log.info("Creating test data for owner controller functional test...");

        SpringSecurityHelper.secureChannel();
        try {
            searchService.deleteItemsByQuery("ctgry:" + CATEGORY_URL_NAME);

            nonSeoFriendlyOwner = new Owner();
            nonSeoFriendlyOwner.setRlnm(OWNER_URL_NAME);
            nonSeoFriendlyOwner.setNm(OWNER_NAME);
            nonSeoFriendlyOwner = ownerService.saveOwner(nonSeoFriendlyOwner);

            seoFriendlyOwner = new Owner();
            seoFriendlyOwner.setRlnm(OWNER_URL_NAME_2);
            seoFriendlyOwner.setSrlnm(OWNER_URL_NAME_2);
            seoFriendlyOwner.setNm(OWNER_NAME_2);
            seoFriendlyOwner = ownerService.saveOwner(seoFriendlyOwner);

            item = new Item();
            item.setId(ObjectId.get());
            item.setPc(item.getId().toString());
            item.setLlpc(item.getId().toString());
            item.setRlnm(ITEM1_URL_NAME);
            item.setNm(ITEM1_NAME);

            cat = new Category();
            cat.setRlnm(CATEGORY_URL_NAME);
            cat.setNm(CATEGORY_NAME);

            cat = categoryService.saveCategory(cat);

            item.setCtgry(cat);

            item.setWnr(nonSeoFriendlyOwner);

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

        log.info("Creating test data for owner controller functional test complete");
    }

    @After
    public void tearDown() {
        SpringSecurityHelper.secureChannel();

        try {
            searchService.deleteItemsByQuery("ctgry:" + CATEGORY_URL_NAME);

            cat = categoryService.removeCategory(cat.getRlnm());
            nonSeoFriendlyOwner = ownerService.removeOwner(nonSeoFriendlyOwner.getRlnm());
            seoFriendlyOwner = ownerService.removeOwner(seoFriendlyOwner.getRlnm());
            item = itemService.removeItem(item.getRlnm());

        } catch (SearchException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }

        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testItemsForOwnerSearch() {
        SpringSecurityHelper.secureChannel();
        log.info("Testing items for nonSeoFriendlyOwner search...");

        Model model = new BindingAwareModelMap();
        HttpSession session = new MockHttpSession();

        try {
            List<String> ids = new ArrayList<String>(1);
            ids.add(item.getId().toString());

            log.info("First we want to search for items without motivators");
            ModelAndView view = ownerController.showOwner(OWNER_URL_NAME, session, model, Locale.US);
            assertEquals("view name is incorrect", "owner.index", view.getViewName());
            assertNotNull("items is null", model.asMap().get(WebConstants.SEARCH_RESULTS));
            GroupedSearchResult searchResult = (GroupedSearchResult) model.asMap().get(WebConstants.SEARCH_RESULTS);
            assertTrue("Results missing", searchResult.getItems().containsKey(CATEGORY_URL_NAME));
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

            log.info("Saving user motivators first");
            UserSupplement us = new UserSupplement(user.getCd());
            us.setMtvtrmp(new HashMap<MotivatorSource, Motivator>(1));
            us.getMtvtrmp().put(MotivatorSource.QUIZ, new Motivator(MotivatorSource.QUIZ, userMotivators));
            us = userService.saveUserSupplement(us);

            session.setAttribute(WebConstants.USER, user);
            log.info("Then we want to test retrieving an item with motivators");
            view = ownerController.showOwner(OWNER_URL_NAME, session, model, Locale.US);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "owner.index", view.getViewName());
            assertNotNull("items is null", model.asMap().get(WebConstants.SEARCH_RESULTS));
            searchResult = (GroupedSearchResult) model.asMap().get(WebConstants.SEARCH_RESULTS);
            assertTrue("Results missing", searchResult.getRelevantItems().containsKey(CATEGORY_URL_NAME));
            long resultSize2 = searchResult.getRelevantItems().get(CATEGORY_URL_NAME).getTotalElements();
            assertTrue("Regular search should be equal relevant search results", resultSize1 == resultSize2);

            model = new BindingAwareModelMap();
            view = ownerController.showOwnerCategory(OWNER_URL_NAME, CATEGORY_URL_NAME, 0, session, model, Locale.US);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "owner.category", view.getViewName());
            assertNotNull("items is null", model.asMap().get(WebConstants.SEARCH_RESULTS));
            SearchResult searchResult1 = (SearchResult) model.asMap().get(WebConstants.SEARCH_RESULTS);
            assertTrue("Results missing", searchResult1.getRelevantItems().hasContent());
            long resultSize3 = searchResult1.getRelevantItems().getTotalElements();
            assertTrue("Regular search should be equal relevant search results", resultSize2 == resultSize3);

            log.info("Nothing left to do so we delete all the Solr documents");
            searchService.deleteItemsById(ids);

            model = new BindingAwareModelMap();
            log.info("Verify that all the specified documents have been deleted");
            view = ownerController.showOwner(OWNER_URL_NAME, session, model, Locale.US);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "owner.index", view.getViewName());
            assertNull("items is not null", model.asMap().get(WebConstants.SEARCH_RESULTS));

        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        SpringSecurityHelper.unsecureChannel();
        log.info("Testing items for nonSeoFriendlyOwner search complete");
    }

    @Test
    public void testSeoFriendlyOwner() {
        log.info("Testing an owner object that has srlnm populated...");

        Model model = new BindingAwareModelMap();
        HttpSession session = new MockHttpSession();

        try {
            ModelAndView view = ownerController.showOwner(OWNER_URL_NAME_2, session, model, Locale.US);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "/" + OWNER_URL_NAME_2 + "/b?rlnm=" + OWNER_URL_NAME_2, ((RedirectView)view.getView()).getUrl());

            model = new BindingAwareModelMap();
            session = new MockHttpSession();
            String theView = ownerController.showOwnerSEO(OWNER_URL_NAME_2, OWNER_URL_NAME_2, session, model, Locale.US);
            assertNotNull("theView is null", theView);
            assertEquals("View name is incorrect", "owner.index", theView);

            view = ownerController.showOwnerCategory(OWNER_URL_NAME_2, CATEGORY_URL_NAME, 0, session, model, Locale.US);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "/" + OWNER_URL_NAME_2 + "/null/b?rlnm=" + OWNER_URL_NAME_2 + "&crlnm=" + CATEGORY_URL_NAME, ((RedirectView)view.getView()).getUrl());

            model = new BindingAwareModelMap();
            session = new MockHttpSession();
            theView = ownerController.showOwnerCategorySEO(OWNER_URL_NAME_2, CATEGORY_URL_NAME, OWNER_URL_NAME_2, CATEGORY_URL_NAME, 0, session, model, Locale.US);
            assertNotNull("theView is null", theView);
            assertEquals("View name is incorrect", "owner.category", theView);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing an owner object that has srlnm populated complete");
    }
}
