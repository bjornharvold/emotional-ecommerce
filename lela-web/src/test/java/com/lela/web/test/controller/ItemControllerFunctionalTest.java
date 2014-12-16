/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.test.controller;

import com.lela.commons.event.*;
import com.lela.commons.service.LocalCacheEvictionService;
import com.lela.commons.service.ProductReviewService;
import com.lela.commons.service.ProfileService;
import com.lela.commons.spring.mobile.MockDevice;
import com.lela.commons.spring.mobile.WurflDevice;
import com.lela.domain.document.Category;
import com.lela.domain.document.Item;
import com.lela.domain.document.Motivator;
import com.lela.domain.document.ProductReview;
import com.lela.domain.document.User;
import com.lela.domain.document.ItemDetails;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.LocationQuery;
import com.lela.domain.dto.Principal;
import com.lela.commons.service.UserService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.domain.enums.MotivatorSource;
import com.lela.web.test.AbstractFunctionalTest;
import com.lela.web.test.controller.event.MockEventHandler;
import com.lela.web.web.controller.ItemController;
import com.lela.commons.web.utils.WebConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Bjorn Harvold
 * Date: 8/3/11
 * Time: 10:05 AM
 * Responsibility:
 */
@SuppressWarnings("unchecked")
public class ItemControllerFunctionalTest extends AbstractFunctionalTest {

    private static final Logger log = LoggerFactory.getLogger(ItemControllerFunctionalTest.class);
    private static final String EMAIL = "testuser1kijmkhj@yopmail.com";

    private static final float LONGITUDE = -106.938716f;
    private static final float LATITUDE = 36.121582f;
    private static final float RADIUS = 20.0f;
    private static final String ZIPCODE = "87018";
    private static final String IP_ADDRESS = "127.0.0.1";
    private static final String IPV6_ADDRESS = "2002:4a8b:32e1:0:2d73:11fb:8b44:da47";

    private static final String ITEM_URL_NAME1 = "teststroller";
    private static final String ITEM_URL_NAME2 = "teststroller2";
    private static final String[] ITEM_URL_NAMES = {"teststroller","teststroller2"};
    private static final String ITEM_CATEGORY = "ItemControllerFunctionalTest";
    
    private final String TEST_ITEM_URL = "pr1";

    private Item item;
    private Item item2;
    private ProductReview pr1;
    private ProductReview pr2;
    private ProductReview pr3;
    private ProductReview pr4;
    
    private Category category;
    private MockDevice mockDevice = new MockDevice(MockDevice.DEVICE_TYPE.NORMAL);
    private Device device = new WurflDevice(mockDevice);

    @Autowired
    private ItemController itemController;

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProductReviewService productReviewService;
    
    @Autowired
    private LocalCacheEvictionService localCacheEvictionService;

    private MockEventHandler eventHandler = new MockEventHandler();

    @Before
    public void setup() {
        SpringSecurityHelper.secureChannel();

        eventHandler.clear();
        new EventHelper(eventHandler);

        // Flush category cache
        localCacheEvictionService.evictItem(ITEM_URL_NAME1);
        localCacheEvictionService.evictItem(ITEM_URL_NAME2);
        localCacheEvictionService.evictItemCategory(ITEM_CATEGORY);

        // Load the item to be tested from a known state for each test
        item = loadToDatabase(Item.class, ITEM_URL_NAME1);

        // Load the item to be tested from a known state for each test
        item2 = loadToDatabase(Item.class, ITEM_URL_NAME2);

        // Load the item to be tested from a known state for each test
        category = loadToDatabase(Category.class, ITEM_CATEGORY);
        
        //Delete all ProductReviews for TEST_ITEM_URL, if any
        ProductReview productReview = productReviewService.findProductReviewsByItemUrlName(TEST_ITEM_URL);
        if (productReview != null){
        	productReviewService.removeProductReview(productReview.getTmrlnm());
        }
        //Load them from file
        pr1 = loadToDatabase(ProductReview.class, "pr1");
        pr2 = loadToDatabase(ProductReview.class, "pr2");
        pr3 = loadToDatabase(ProductReview.class, "pr3");
        pr4 = loadToDatabase(ProductReview.class, "pr4");
        
        
        SpringSecurityHelper.unsecureChannel();
    }
    
    @After
    public void teardown() {
        
        if (item != null) {
            localCacheEvictionService.evictItem(item.getRlnm());
        }

        if (item2 != null) {
            localCacheEvictionService.evictItem(item2.getRlnm());
        }

        if (category != null) {
            localCacheEvictionService.evictCategory(category.getRlnm());
        }

        eventHandler.clear();
    }

    @Test
    public void testItemDetails() {
        log.info("Testing item details...");
        User user = new User();
        Model model = new BindingAwareModelMap();
        MockHttpServletRequest request = new MockHttpServletRequest();
        HttpSession session = new MockHttpSession();
        session.setAttribute(WebConstants.USER, user);

        try {
            log.info("First we want to test retrieving just an item with the old non-seo friendly request mapping");
            ModelAndView view = itemController.showItemDetails(ITEM_URL_NAME2, request, session, model);
            assertEquals("There should be 1 events fired", 1, eventHandler.getEvents().size());
            assertNotNull("There should be a ViewedItemEvent", eventHandler.find(ViewedItemEvent.class));
            assertNotNull("view is null", view);
            assertTrue("View type is incorrect", view.getView() instanceof RedirectView);
            RedirectView rv = (RedirectView) view.getView();
            assertEquals("view name is incorrect", "/" + ITEM_URL_NAME2 + "/p?rlnm=" + ITEM_URL_NAME2, rv.getUrl());

            log.info("Let's now try to use the new SEO friendly request mapping");
            model = new BindingAwareModelMap();
            session = new MockHttpSession();
            session.setAttribute(WebConstants.USER, user);
            eventHandler.clear();
            String view1 = itemController.showItemDetailsSEO(ITEM_URL_NAME2, ITEM_URL_NAME2, null, request, session, model, device);
            assertEquals("There should be 1 events fired", 1, eventHandler.getEvents().size());
            assertNotNull("There should be a ViewedItemEvent", eventHandler.find(ViewedItemEvent.class));
            assertNotNull("view is null", view1);
            assertEquals("view name is incorrect", "item.details", view1);
            assertNotNull("item is null", model.asMap().get(WebConstants.ITEM_DETAILS));

            model = new BindingAwareModelMap();

            Map<String, Integer> userMotivators = new HashMap<String, Integer>();
            userMotivators.put("A", 8);
            userMotivators.put("B", 9);
            userMotivators.put("C", 3);
            userMotivators.put("D", 8);
            userMotivators.put("E", 9);
            userMotivators.put("F", 8);
            userMotivators.put("G", 8);

            Motivator motivator = userService.saveMotivator(user.getCd(), new Motivator(MotivatorSource.QUIZ, userMotivators));

            session = new MockHttpSession();
            session.setAttribute(WebConstants.USER, user);

            log.info("Then we want to test retrieving an item with motivators");
            eventHandler.clear();
            view1 = itemController.showItemDetailsSEO(ITEM_URL_NAME2, ITEM_URL_NAME2, null, request, session, model, device);
            assertEquals("There should be 1 events fired", 1, eventHandler.getEvents().size());
            assertNotNull("There should be a ViewedItemEvent", eventHandler.find(ViewedItemEvent.class));

            assertNotNull("view is null", view1);
            assertEquals("view name is incorrect", "item.details", view1);
            assertNotNull("item is null", model.asMap().get(WebConstants.ITEM_DETAILS));
            assertTrue("Item class incorrect", model.asMap().get(WebConstants.ITEM_DETAILS) instanceof ItemDetails);
            assertNotNull("supplementary items are null", model.asMap().get(WebConstants.SUPPLEMENTARY_ITEMS));


        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        } finally {
            SpringSecurityHelper.secureChannel();
            userService.removeUser(user);
            SpringSecurityHelper.unsecureChannel();
        }

        log.info("Testing item details complete");
    }

    @Test
    public void testItemComparison() {
        log.info("Testing item comparison details...");
        User user = new User();
        Model model = new BindingAwareModelMap();
        HttpSession session = new MockHttpSession();
        session.setAttribute(WebConstants.USER, user);

        try {
            log.info("First we want to test retrieving just an item without motivators");
            String view = itemController.showItemComparison(ITEM_URL_NAMES, session, model);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "item.comparison", view);
            assertNotNull("items is null", model.asMap().get(WebConstants.ITEMS));

            List<Item> items = (List<Item>)model.asMap().get(WebConstants.ITEMS);
            assertEquals("Items count not correct", 2, items.size());

            boolean found = false;
            for (Item item : items) {
                if (item.getRlnm().equals(ITEM_URL_NAME1)) {
                    found = true;
                    break;
                }
            }
            assertTrue("Item missing from list", found);

            found = false;
            for (Item item : items) {
                if (item.getRlnm().equals(ITEM_URL_NAME2)) {
                    found = true;
                    break;
                }
            }
            assertTrue("Item missing from list", found);

            assertEquals("There should be 1 events fired", 1, eventHandler.getEvents().size());
            assertNotNull("There should be a CompareItemsEvent", eventHandler.find(CompareItemsEvent.class));

        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        } finally {
            SpringSecurityHelper.secureChannel();
            userService.removeUser(user);
            SpringSecurityHelper.unsecureChannel();
        }

        log.info("Testing item comparison details complete");
    }

    /**
     * IMPORTANT!  This method cannot test that search by lat long is returning
     * accurate results since there are no online stores and thus the search always
     * returns an empty result.  See tests in MerchantServiceFunctionTest that
     * correctly test search functionality
     */
    @Test
    public void testSearchByLatLong() {

        /*
        {
        "longitude": "-73.986171",
        "latitude": "40.748716",
        "radius": "10.0"
        }
        */
        LocationQuery query = new LocationQuery();
        query.setLongitude(LONGITUDE);
        query.setLatitude(LATITUDE);
        query.setRadius(RADIUS);

        HttpSession session = new MockHttpSession();
        HttpServletRequest request = new MockHttpServletRequest();
        Model model = new BindingAwareModelMap();

        User user = new User();
        session.setAttribute(WebConstants.USER, user);

        try {
            String view = itemController.showLocalStores(ITEM_URL_NAME1, query, request, session, model);

            assertNotNull("View is null", view);
            assertNotNull("Branches not provided", model.asMap().get(WebConstants.BRANCHES));
            assertNotNull("Item not provided", model.asMap().get(WebConstants.ITEM));

        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        } finally {
            SpringSecurityHelper.secureChannel();
            userService.removeUser(user);
            SpringSecurityHelper.unsecureChannel();
        }
    }

    /**
     * IMPORTANT!  This method cannot test that search by lat long is returning
     * accurate results since there are no online stores and thus the search always
     * returns an empty result.  See tests in MerchantServiceFunctionTest that
     * correctly test search functionality
     */
    @Test
    public void testSearchByZipcode() {

        /*
        {
        "zipcode": "10001",
        "radius": "10.0"
        }
        */
        LocationQuery query = new LocationQuery();
        query.setZipcode(ZIPCODE);
        query.setRadius(RADIUS);

        User user = new User();
        HttpSession session = new MockHttpSession();
        HttpServletRequest request = new MockHttpServletRequest();
        Model model = new BindingAwareModelMap();
        session.setAttribute(WebConstants.USER, user);

        try {
            String view = itemController.showLocalStores(ITEM_URL_NAME1,
                    query,
                    request,
                    session,
                    model);

            assertNotNull("View is null", view);
            assertNotNull("Branches not provided", model.asMap().get(WebConstants.BRANCHES));
            assertNotNull("Item not provided", model.asMap().get(WebConstants.ITEM));

        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        } finally {
            SpringSecurityHelper.secureChannel();
            userService.removeUser(user);
            SpringSecurityHelper.unsecureChannel();
        }
    }

    /**
     * IMPORTANT!  This method cannot test that search by lat long is returning
     * accurate results since there are no online stores and thus the search always
     * returns an empty result.  See tests in MerchantServiceFunctionTest that
     * correctly test search functionality
     */
    @Test
    public void testSearchByIpAddress() {

        /*
        {
        "ipAddress": "10001",
        "radius": "10.0"
        }
        */
        LocationQuery query = new LocationQuery();
        query.setIpAddress(IP_ADDRESS);
        query.setRadius(RADIUS);

        HttpSession session = new MockHttpSession();
        HttpServletRequest request = new MockHttpServletRequest();
        Model model = new BindingAwareModelMap();
        User user = new User();
        session.setAttribute(WebConstants.USER, user);

        try {
            String view = itemController.showLocalStores(ITEM_URL_NAME1,
                    query,
                    request,
                    session,
                    model);

            assertNotNull("View is null", view);
            assertNotNull("Branches not provided", model.asMap().get(WebConstants.BRANCHES));
            assertNotNull("Item not provided", model.asMap().get(WebConstants.ITEM));

        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        } finally {
            SpringSecurityHelper.secureChannel();
            userService.removeUser(user);
            SpringSecurityHelper.unsecureChannel();
        }
    }

    /**
     * IMPORTANT!  This method cannot test that search by lat long is returning
     * accurate results since there are no online stores and thus the search always
     * returns an empty result.  See tests in MerchantServiceFunctionTest that
     * correctly test search functionality
     */
    @Test
    public void testSearchByIpv6Address() {

        /*
        {
        "ipAddress": "10001",
        "radius": "10.0"
        }
        */
        LocationQuery query = new LocationQuery();
        query.setIpAddress(IPV6_ADDRESS);
        query.setRadius(RADIUS);

        User user = new User();
        HttpSession session = new MockHttpSession();
        HttpServletRequest request = new MockHttpServletRequest();
        Model model = new BindingAwareModelMap();

        session.setAttribute(WebConstants.USER, user);

        try {
            String view = itemController.showLocalStores(ITEM_URL_NAME1,
                    query,
                    request,
                    session,
                    model);

            assertNotNull("View is null", view);
            assertNotNull("Branches not provided", model.asMap().get(WebConstants.BRANCHES));
            assertNotNull("Item not provided", model.asMap().get(WebConstants.ITEM));

        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        } finally {
            SpringSecurityHelper.secureChannel();
            userService.removeUser(user);
            SpringSecurityHelper.unsecureChannel();
        }
    }

    /**
     * IMPORTANT!  This method cannot test that search by lat long is returning
     * accurate results since there are no online stores and thus the search always
     * returns an empty result.  See tests in MerchantServiceFunctionTest that
     * correctly test search functionality
     */
    @Test
    public void testSearchWithNoArguments() {

        /*
        {
        }
        */
        LocationQuery query = new LocationQuery();

        HttpSession session = new MockHttpSession();
        HttpServletRequest request = new MockHttpServletRequest();
        Model model = new BindingAwareModelMap();
        User user = new User();
        session.setAttribute(WebConstants.USER, user);

        try {
            String view = itemController.showLocalStores(ITEM_URL_NAME1,
                    query,
                    request,
                    session,
                    model);

            assertNotNull("View is null", view);
            assertNotNull("Branches not provided", model.asMap().get(WebConstants.BRANCHES));
            assertNotNull("Item not provided", model.asMap().get(WebConstants.ITEM));

        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        } finally {
            SpringSecurityHelper.secureChannel();
            userService.removeUser(user);
            SpringSecurityHelper.unsecureChannel();
        }
    }

    @Test
    public void testSearchByZipcodeChangesUserCurrentZip() {

        User user = createRandomUser(true);

        user = profileService.registerTestUser(user);

        try {
            SpringSecurityHelper.secureChannel(new Principal(user));

            /*
            {
            "zipcode": "10001",
            "radius": "10.0"
            }
            */
            LocationQuery query = new LocationQuery();
            query.setZipcode(ZIPCODE);
            query.setRadius(RADIUS);

            HttpSession session = new MockHttpSession();
            HttpServletRequest request = new MockHttpServletRequest();
            Model model = new BindingAwareModelMap();

            try {
                itemController.showLocalStores(ITEM_URL_NAME1, query, request, session, model);

                UserSupplement us = userService.findUserSupplement(user.getCd());
                assertEquals("Current zipcode is not correct", ZIPCODE, us.getCzp());

            } catch (Exception e) {
                log.info(e.getMessage(), e);
                fail("Method threw unexpected exception: " + e.getMessage());
            }
        } finally {
            SpringSecurityHelper.secureChannel();
            userService.removeUser(user);
            SpringSecurityHelper.unsecureChannel();
        }
    }  
    @Test
	public void testProductReviewsByItemUrl() {

		try {
			Model model = new BindingAwareModelMap();
			itemController.showReviewsForItem(TEST_ITEM_URL, model);
			Assert.assertTrue(model.containsAttribute(WebConstants.ITEM_PRODUCT_REVIEW));
			ProductReview productReview = (ProductReview)model.asMap().get(WebConstants.ITEM_PRODUCT_REVIEW);
			Assert.assertNotNull(productReview.getRdlst());
			Assert.assertTrue(productReview.getRdlst().size() == 20);
			
		} catch (Exception e) {
			log.info(e.getMessage(), e);
			fail("Method threw unexpected exception: " + e.getMessage());
		} finally {
			SpringSecurityHelper.unsecureChannel();
		}
	}
}
