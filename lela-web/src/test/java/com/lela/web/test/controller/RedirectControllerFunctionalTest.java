/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.test.controller;

import com.google.common.collect.Maps;
import com.lela.commons.service.LocalCacheEvictionService;
import com.lela.commons.service.ProfileService;
import com.lela.domain.document.AvailableInStore;
import com.lela.domain.document.Item;
import com.lela.domain.document.QRedirect;
import com.lela.domain.document.Redirect;
import com.lela.domain.document.User;
import com.lela.domain.dto.Principal;
import com.lela.commons.remote.impl.AmazonMerchantClient;
import com.lela.commons.service.RedirectService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.web.test.AbstractFunctionalTest;
import com.lela.web.web.controller.RedirectController;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by IntelliJ IDEA.
 * User: Martin Gamboa
 * Date: 10/4/11
 * Time: 12:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class RedirectControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(RedirectControllerFunctionalTest.class);

    private static final String POPSHOPS_SUBID_PATTERN = "^redirect:((.*)/\\d{10}-(\\w{24})-(\\w{24}))$";

    private static final String ITEM_URL_NAME = "teststroller";
    private static final String ITEM_URL_NAME_2 = "teststroller2";
    private static final String INVALID_URL = "http://x.store.com";
    
    @Value("${amazon.affiliate.id}")
    private String affiliateId;

    @Autowired
    private RedirectController redirectController;

    @Autowired
    private RedirectService redirectService;

    @Autowired
    private LocalCacheEvictionService localCacheEvictionService;

    @Autowired
    private ProfileService profileService;

    private User user = null;
    private Item item = null;

    @Before
    public void setUp() {
        SpringSecurityHelper.secureChannel();

        // Find the test user
        user = createRandomUser(true);
        user = profileService.registerTestUser(user);

        // Load the item to be tested from a known state for each test
        item = loadToDatabase(Item.class, ITEM_URL_NAME_2);
    }

    @After
    public void tearDown() {
        SpringSecurityHelper.secureChannel();

        if (item != null) {
            localCacheEvictionService.evictItem(item.getRlnm());
        }

        // Remove any redirects from the test user
        if (user != null) {
            userService.removeUser(user);
        }

        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testUrlRedirectWithAmazon() {
//        SpringSecurityHelper.secureChannel(new Principal(user));
//
//        if (item.getStrs() != null && !item.getStrs().isEmpty()) {
//            AvailableInStore store = null;
//            for (AvailableInStore inStore : item.getStrs()) {
//                if (StringUtils.equals(inStore.getMrchntd(), AmazonMerchantClient.MERCHANT_ID)) {
//                    store = inStore;
//                    break;
//                }
//            }
//            String url = (String)store.getTms().get(0).getSubAttributes().get("PurchaseUrl");
//            url = StringUtils.replace(url, "%26tag%3Dws%26","%26tag%3D" + affiliateId + "%26");
//
//            try {
//                long beforeCount = redirectRepository.count();
//                HttpServletResponse response = new MockHttpServletResponse();
//                HttpSession session = new MockHttpSession();
//                String view = redirectController.redirect(store.getMrchntd(),item.getRlnm(), response, session);
//                assertNotNull("view is null", view);
//
//                Map<String, String> paramMap = deriveParameters(view);
//
//                assertEquals("Redirect url parameter is invalid", url, paramMap.get("url"));
//                assertEquals("Redirect auth parameter is invalid", "true", paramMap.get("auth"));
//                long afterCount = redirectRepository.count();
//                assertEquals("Redirect count not incremented", afterCount, beforeCount + 1);
//            } catch (Exception e) {
//                log.info(e.getMessage(), e);
//                fail("Method threw unexpected exception: " + e.getMessage());
//            }
//        }
    }

    /*
    @Test
    public void testUrlRedirectWithPopshop() {

        assertNotNull("User1 is missing", user);
        assertNotNull("User1 is missing an id", user.getId());

        SpringSecurityHelper.secureChannel(new Principal(user));

        if (item.getStrs() != null && !item.getStrs().isEmpty()) {
            AvailableInStore store = null;
            for (AvailableInStore inStore : item.getStrs()) {
                if (!StringUtils.equals(inStore.getMrchntd(), AmazonMerchantClient.MERCHANT_ID)) {
                    store = inStore;
                    break;
                }
            }
            String purchaseUrl = (String)store.getTms().get(0).getSubAttributes().get("PurchaseUrl");

            try {
                long beforeCount = redirectRepository.count();
                HttpServletResponse response = new MockHttpServletResponse();
                HttpSession session = new MockHttpSession();
                String view = redirectController.redirect(store.getMrchntd(),item.getRlnm(), response, session);
                assertNotNull("view is null", view);
                
                // Verify that the popshops URL matches the correct pattern
                Pattern pattern = Pattern.compile(POPSHOPS_SUBID_PATTERN);
                Matcher matcher = pattern.matcher(view);
                assertTrue("Popshops redirect URL does not match pattern", matcher.matches());

                // Verify the base purchase url
                assertEquals("Popshops base purchase URL incorrect: ", purchaseUrl, matcher.group(2));

                // Verify the object id components
                assertTrue("Popshops subid item id is not valid Object ID: ", ObjectId.isValid(matcher.group(3)));
                assertTrue("Popshops subid redirect id is not valid Object ID: ", ObjectId.isValid(matcher.group(4)));

                // Verify that redirect was saved to mongo
                Redirect redirect = redirectRepository.findOne(new ObjectId(matcher.group(4)));
                assertNotNull("Popshops redirect not found in database using sub-id embedded ObjectID", redirect);

                // Verify saved properties
                assertEquals("Popshops url not saved correctly in Redirect", matcher.group(1), redirect.getRl());

                long afterCount = redirectRepository.count();
                assertEquals("Redirect count not incremented", afterCount, beforeCount + 1);
            } catch (Exception e) {
                log.info(e.getMessage(), e);
                fail("Method threw unexpected exception: " + e.getMessage());
            }
        }
    }

    @Test
    public void testUrlRedirectWithPopshopNoUserId() {

        User user = null;

        if (item.getStrs() != null && !item.getStrs().isEmpty()) {
            AvailableInStore store = null;
            for (AvailableInStore inStore : item.getStrs()) {
                if (!StringUtils.equals(inStore.getMrchntd(), AmazonMerchantClient.MERCHANT_ID)) {
                    store = inStore;
                    break;
                }
            }
            String purchaseUrl = (String)store.getTms().get(0).getSubAttributes().get("PurchaseUrl");

            try {
                long beforeCount = redirectRepository.count();
                HttpServletResponse response = new MockHttpServletResponse();
                HttpSession session = new MockHttpSession();
                String view = redirectController.redirect(store.getMrchntd(), item.getRlnm(), response, session);
                assertNotNull("view is null", view);

                // Verify that the popshops URL matches the correct pattern
                Pattern pattern = Pattern.compile(POPSHOPS_SUBID_PATTERN);
                Matcher matcher = pattern.matcher(view);
                assertTrue("Popshops redirect URL does not match pattern", matcher.matches());

                // Verify the base purchase url
                assertEquals("Popshops base purchase URL incorrect: ", purchaseUrl, matcher.group(2));

                // Verify the object id components
                assertTrue("Popshops subid item id is not valid Object ID: ", ObjectId.isValid(matcher.group(3)));
                assertTrue("Popshops subid redirect id is not valid Object ID: ", ObjectId.isValid(matcher.group(4)));

                // Verify that redirect was saved to mongo
                Redirect redirect = redirectRepository.findOne(new ObjectId(matcher.group(4)));
                assertNotNull("Popshops redirect not found in database using sub-id embedded ObjectID", redirect);

                // Verify saved properties
                assertEquals("Popshops url not saved correctly in Redirect", matcher.group(1), redirect.getRl());

                long afterCount = redirectRepository.count();
                assertEquals("Redirect count not incremented", afterCount, beforeCount + 1);
            } catch (Exception e) {
                log.info(e.getMessage(), e);
                fail("Method threw unexpected exception: " + e.getMessage());
            }
        }
    }
    */

    /*
    @Test
    public void testInvalidUrlRedirect() {
        Item item = eventService.findItemByUrlName(ITEM_URL_NAME);
        merchantService.updateAvailableStores(item, MerchantType.ALL);

        if (item.getStrs() != null && !item.getStrs().isEmpty()) {
            AvailableInStore store = item.getStrs().get(0);

            try {
                long beforeCount = redirectRepository.count();
                String view = redirectController.redirect(store.getMrchntd(),item.getRlnm(),INVALID_URL);
                assertNotNull("view is null", view);
                String redirectString = "redirect:403";
                assertEquals("Invalid redirect was incorrect", redirectString, view);
                long afterCount = redirectRepository.count();
                assertEquals("Redirect count not incremented", afterCount, beforeCount + 1);
            } catch (Exception e) {
                log.info(e.getMessage(), e);
                fail("Method threw unexpected exception: " + e.getMessage());
            }
        }
    }
    */
    private Map<String, String> deriveParameters(String view)
    {
        Map<String, String> paramMap = new HashMap<String, String>();
        String parameters = StringUtils.substringAfter(view, "?");
        String[] values = parameters.split("&");

        for(String value: values)
        {
            String[] nameValue = value.split("=");
            paramMap.put(nameValue[0], nameValue[1]);
        }
        return paramMap;
    }
}
