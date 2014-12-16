/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.web.test.controller;

import com.lela.commons.service.AffiliateService;
import com.lela.commons.service.CampaignService;
import com.lela.commons.service.LocalCacheEvictionService;
import com.lela.commons.service.ProfileService;
import com.lela.commons.service.UserService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.Campaign;
import com.lela.domain.document.User;
import com.lela.domain.dto.UserAttributes;
import com.lela.domain.dto.UserDto;
import com.lela.web.test.AbstractFunctionalTest;
import com.lela.web.web.controller.AffiliateController;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Created by IntelliJ IDEA.
 * User: chris
 * Date: 1/12/12
 * Time: 9:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class AffiliateControllerFunctionalTest extends AbstractFunctionalTest {

    private static final Logger log = LoggerFactory.getLogger(AffiliateControllerFunctionalTest.class);
    private static final String EMAIL = "testuser4kijmkhj@yopmail.com";
    private static final String AFFILIATE_ACCOUNT_NM = "affilateaccount";
    private static final String CAMPAIGN_NM = "campaign";
    private static final String CAMPAIGN_REDIRECT = "/some/redirect";
    private static final String REFERRER_NM = "referrerAffiliateNnm";

    private AffiliateAccount account = null;
    private Campaign campaign = null;
    private AffiliateAccount referrer = null;

    @Autowired
    private AffiliateController affiliateController;

    @Autowired
    private UserService userService;

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private AffiliateService affiliateService;

    @Autowired
    private LocalCacheEvictionService localCacheEvictionService;

    @Autowired
    private ProfileService profileService;

    @Before
    public void setup() {

        SpringSecurityHelper.secureChannel();

        GregorianCalendar oneMonthAgo = new GregorianCalendar();
        oneMonthAgo.add(Calendar.MONTH, -1);

        GregorianCalendar oneMonthFromNow = new GregorianCalendar();
        oneMonthFromNow.add(Calendar.MONTH, 1);

        // Create an affiliate
        account = new AffiliateAccount();
        account.setId(new ObjectId());
        account.setNm(AFFILIATE_ACCOUNT_NM);
        account.setRlnm(AFFILIATE_ACCOUNT_NM);
        account.setCtv(true);

        affiliateService.saveAffiliateAccount(account);

        // Create a campaign
        campaign = new Campaign();
        campaign.setNm(CAMPAIGN_NM);
        campaign.setRlnm(CAMPAIGN_NM);
        campaign.setFfltrlnm(AFFILIATE_ACCOUNT_NM);
        campaign.setRdrctrl(CAMPAIGN_REDIRECT);
        campaign.setCtv(true);
        campaign.setStrtdt(oneMonthAgo.getTime());
        campaign.setNddt(oneMonthFromNow.getTime());

        campaignService.saveCampaign(campaign);

        // Create referring affiliate
        referrer = new AffiliateAccount();
        referrer.setId(new ObjectId());
        referrer.setNm(REFERRER_NM);
        referrer.setRlnm(REFERRER_NM);
        referrer.setCtv(true);

        affiliateService.saveAffiliateAccount(referrer);

        SpringSecurityHelper.unsecureChannel();
    }

    @After
    public void tearDown() {
        SpringSecurityHelper.secureChannel();
        User user = userService.findUserByEmail(EMAIL);

        if (user != null) {
            userService.removeUser(user);
        }

        SpringSecurityHelper.secureChannel();
        if (account != null) {
            affiliateService.removeAffiliateAccount(account.getRlnm());
            localCacheEvictionService.evictAffiliateAccount(account.getRlnm());
            account = null;
        }

        if (campaign != null) {
            campaignService.removeCampaign(campaign.getRlnm());
            localCacheEvictionService.evictCampaign(campaign.getRlnm());
            campaign = null;
        }

        if (referrer != null) {
            affiliateService.removeAffiliateAccount(REFERRER_NM);
            localCacheEvictionService.evictAffiliateAccount(referrer.getRlnm());
            referrer = null;
        }

        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testRedirect() {

        log.info("Testing saving affiliate data to cookies and session then redirect...");
        MockHttpSession session = new MockHttpSession();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setSession(session);
        MockHttpServletResponse response = new MockHttpServletResponse();
        Model model = new BindingAwareModelMap();

        try {
            // Call the landing page
            UserAttributes attr = new UserAttributes();
            String redirect = affiliateController.landingPage(attr, CAMPAIGN_NM, REFERRER_NM, session, request, response, model, Locale.US);

            // Verify the redirect url
            assertEquals("Campaign redirect is incorrect", "redirect:" + CAMPAIGN_REDIRECT, redirect);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing registration submission complete");
    }

    @Test
    public void testNothingSavedForLoggedInUser() {

        log.info("Testing saving affiliate data to cookies and session then redirect...");
        MockHttpSession session = new MockHttpSession();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Model model = new BindingAwareModelMap();

        try {
            log.info("Filling out the user dto so we can run registration");
            UserDto userDto = new UserDto();
            userDto.setPsswrd("test");
            userDto.setMl("testuser4kijmkhj@yopmail.com");
            userDto.setLcl(Locale.US);
            userDto.setOptin(false);
            User user = profileService.registerWebUser(userDto, null, null);
            userService.autoLogin(user.getId(), null);

            // Call the landing page
            UserAttributes attr = new UserAttributes();
            String redirect = affiliateController.landingPage(attr, CAMPAIGN_NM, REFERRER_NM, session, request, response, model, Locale.US);

            // Verify the redirect url
            assertEquals("Campaign redirect is incorrect", "redirect:" + CAMPAIGN_REDIRECT, redirect);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing registration submission complete");
    }

    @Test
    public void testUserAttributes() {
        log.info("Testing adding an attribute to the user...");
        User user = null;
        MockHttpSession session = new MockHttpSession();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();
        Model model = new BindingAwareModelMap();

        try {
            UserAttributes ua = new UserAttributes();

            Map<String, List<String>> map = new HashMap<String, List<String>>(1);
            List<String> values = new ArrayList<String>(1);
            values.add("20120512");
            values.add("20128956");

            // invalid value
            map.put("ddt", values);
            ua.setMap(map);

            log.info("First we test with invalid values");
            String view = affiliateController.landingPage(ua, CAMPAIGN_NM, REFERRER_NM, session, request, response, model, Locale.US);
            assertNotNull("view is null", view);

            log.info("There should not be any attributes on the user at this point");
            user = (User) session.getAttribute(WebConstants.USER);
            assertNotNull("user is null", user);
            Map<String, List<String>> userAttributes = userService.findUserAttributes(user.getCd());
            assertNull("user attributes is not null", userAttributes);

            log.info("Now we test with incorrect key");
            map = new HashMap<String, List<String>>(1);
            values = new ArrayList<String>(1);
            values.add("20120512");

            // invalid value
            map.put("incorrectKey", values);
            ua.setMap(map);

            view = affiliateController.landingPage(ua, CAMPAIGN_NM, REFERRER_NM, session, request, response, model, Locale.US);
            assertNotNull("view is null", view);

            log.info("There should not be any attributes on the user at this point");
            user = (User) session.getAttribute(WebConstants.USER);
            assertNotNull("user is null", user);
            userAttributes = userService.findUserAttributes(user.getCd());
            assertNull("user attributes is not null", userAttributes);

            log.info("Now we test with correct key and values");
            map = new HashMap<String, List<String>>(1);
            values = new ArrayList<String>(1);
            values.add("20120512");

            // user attribute: "due date"
            map.put("ddt", values);
            ua.setMap(map);

            view = affiliateController.landingPage(ua, CAMPAIGN_NM, REFERRER_NM, session, request, response, model, Locale.US);
            assertNotNull("view is null", view);

            log.info("There should be attributes on the user at this point");
            user = (User) session.getAttribute(WebConstants.USER);
            assertNotNull("user is null", user);
            userAttributes = userService.findUserAttributes(user.getCd());
            assertNotNull("user attributes is not null", userAttributes);
            assertEquals("user attribute size is incorrect", 1, userAttributes.size(), 0);

            log.info("Testing adding an attribute to the user complete");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        } finally {
            SpringSecurityHelper.secureChannel();
            userService.removeUser(user);
            SpringSecurityHelper.unsecureChannel();
        }
    }

}
