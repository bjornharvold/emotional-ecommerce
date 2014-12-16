/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.test.controller;

import com.lela.commons.service.AffiliateService;
import com.lela.commons.service.CampaignService;
import com.lela.commons.service.EventService;
import com.lela.commons.service.LocalCacheEvictionService;
import com.lela.commons.service.UserService;
import com.lela.commons.spring.mobile.MockDevice;
import com.lela.commons.spring.mobile.WurflDevice;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.Campaign;
import com.lela.domain.document.User;
import com.lela.domain.document.UserAssociation;
import com.lela.domain.document.UserAssociationAttribute;
import com.lela.domain.dto.UserDto;
import com.lela.domain.enums.AssociationType;
import com.lela.web.test.AbstractFunctionalTest;
import com.lela.web.web.controller.RegistrationController;
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
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.handler.DispatcherServletWebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.servlet.http.Cookie;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static com.lela.commons.web.utils.WebConstants.USER_DTO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
public class RegistrationControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(RegistrationControllerFunctionalTest.class);
    private static final String EMAIL = "testuser4kijmkhj@yopmail.com";
    private static final String REDIRECT_URL = "redirect/url";
    private static final String AFFILIATE_ACCOUNT_NM = "affiliateaccount";
    private static final String CAMPAIGN_NM = "campaign";
    private static final String CAMPAIGN_REDIRECT = "/some/redirect";
    private static final String REDIRECT_LOGIN_REDIRECT = "redirect:/login/redirect";
    private static final String REFERRER_NM = "referrer";

    private AffiliateAccount account = null;
    private Campaign campaign = null;
    private AffiliateAccount referrer = null;

    @Autowired
    private RegistrationController registrationController;

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @Autowired
    private LocalCacheEvictionService localCacheEvictionService;

    @Autowired
    private AffiliateService affiliateService;

    @Autowired
    private CampaignService campaignService;

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
        campaign.setRdrctrl(CAMPAIGN_REDIRECT);
        campaign.setCtv(true);
        campaign.setStrtdt(oneMonthAgo.getTime());
        campaign.setNddt(oneMonthFromNow.getTime());

        campaignService.saveCampaign(campaign);

        // Create an affiliate referrer
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

        if (account != null) {
            affiliateService.removeAffiliateAccount(account.getRlnm());
            localCacheEvictionService.evictAffiliateAccount(account.getRlnm());
            account = null;
        }

        if (referrer != null) {
            affiliateService.removeAffiliateAccount(referrer.getRlnm());
            localCacheEvictionService.evictAffiliateAccount(referrer.getRlnm());
            referrer = null;
        }

        if (campaign != null) {
            campaignService.removeCampaign(campaign.getRlnm());
            localCacheEvictionService.evictCampaign(campaign.getRlnm());
            campaign = null;
        }

        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testRegistration() {
        log.info("Testing registration...");

        try {
            MockHttpServletRequest request = new MockHttpServletRequest();
            MockHttpServletResponse response = new MockHttpServletResponse();
            MockHttpSession session = new MockHttpSession();
            WebRequest webRequest = new DispatcherServletWebRequest(request, response);
            Model model = new BindingAwareModelMap();

            WurflDevice device = new WurflDevice(new MockDevice(MockDevice.DEVICE_TYPE.MOBILE));
            log.info("Testing without passing in a redirect url and on a mobile device");
            String view = registrationController.showRegistrationForm(null, device, session, webRequest, model);
            assertEquals("Tile view is incorrect", "register.mobile", view);
            assertNotNull("UserDto is missing", model.asMap().get(USER_DTO));

            device = new WurflDevice(new MockDevice(MockDevice.DEVICE_TYPE.NORMAL));
            log.info("Testing without passing in a redirect url");
            view = registrationController.showRegistrationForm(null, device, session, webRequest, model);
            assertEquals("Tile view is incorrect", "register", view);
            assertNotNull("UserDto is missing", model.asMap().get(USER_DTO));

            log.info("Testing with passing in a redirect url");
            view = registrationController.showRegistrationForm(REDIRECT_URL, device, session, webRequest, model);
            assertEquals("Tile view is incorrect", "register", view);
            assertNotNull("UserDto is missing", model.asMap().get(USER_DTO));
            assertNotNull("Redirect url is missing", session.getAttribute(WebConstants.REDIRECT));

            log.info("Assigning the newly created user for the next test");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        log.info("Testing registration complete");
    }

    @Test
    public void testSubmitRegistration() {
        log.info("Testing registration submission...");

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpSession session = new MockHttpSession();
        request.setSession(session);
        NativeWebRequest webRequest = new DispatcherServletWebRequest(request, response);
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        try {
            log.info("Filling out the user dto so we can run registration");
            UserDto user = new UserDto();
            user.setPsswrd("test");
            user.setMl("testuser4kijmkhj@yopmail.com");

            BindingResult results = new BindException(user, "userDto");

            String view = registrationController.submitRegistrationForm(user, results, webRequest, request, response, session, redirectAttributes, Locale.US);
            assertEquals("Tile view is incorrect", REDIRECT_LOGIN_REDIRECT, view);
            assertNotNull("Redirect attributes missing entry", redirectAttributes.getFlashAttributes().get(WebConstants.MESSAGE));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        assertTrue(true);
        log.info("Testing registration submission complete");
    }


    private void verifyUserAssociation(Date now, UserDto user) {
        // Retrieve the user from the database
        User dbUser = userService.findUserByEmail(user.getMl());
        assertNotNull("User not saved to database", dbUser);
        List<UserAssociation> ua = userService.findUserAssociations(dbUser.getCd());
        assertNotNull("User association list is null", ua);
        assertFalse("User association list is empty", ua.isEmpty());

        // Test association fields
        UserAssociation association = ua.get(0);
        assertEquals("Affiliate association has wrong name", AFFILIATE_ACCOUNT_NM, association.getNm());
        assertEquals("Affiliate association has wrong type", AssociationType.AFFILIATE, association.getTp());
        assertNotNull("Affiliate association create date was not set", association.getCdt());
        assertTrue("Affiliate association has bad create date", association.getCdt().after(now));

        // Test association attributes
        List<UserAssociationAttribute> attrs = association.getAttrs();
        assertNotNull("Association has no attributes", attrs);
        assertFalse("Association attributes list is empty", attrs.isEmpty());

        findAndVerifyAttribute(attrs, WebConstants.USER_ASSOCIATION_ATTRIBUTE_AFFILIATE_ACCOUNT_KEY, AFFILIATE_ACCOUNT_NM);
        findAndVerifyAttribute(attrs, WebConstants.USER_ASSOCIATION_ATTRIBUTE_CAMPAIGN_KEY, CAMPAIGN_NM);
        findAndVerifyAttribute(attrs, WebConstants.USER_ASSOCIATION_ATTRIBUTE_REFERRING_AFFILIATE_KEY, REFERRER_NM);
    }

    private void findAndVerifyAttribute(List<UserAssociationAttribute> attrs, String key, String value) {
        boolean found = false;
        for (UserAssociationAttribute attr : attrs) {
            if (key.equals(attr.getKy())) {
                assertEquals("Attribute has incorrect value for key: " + key, value, attr.getVl());
                found = true;
                break;
            }
        }

        if (!found) {
            fail("Attribute not found for key: " + key);
        }
    }
}
