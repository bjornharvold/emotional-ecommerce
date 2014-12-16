/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.data.web.controller.administration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Locale;

import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.lela.commons.service.AffiliateService;
import com.lela.commons.service.ApplicationService;
import com.lela.commons.service.LocalCacheEvictionService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.data.web.AbstractFunctionalTest;
import com.lela.data.web.controller.administration.affiliate.AffiliateManagerController;
import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.AffiliateAccountApplication;
import com.lela.domain.document.Application;
import com.lela.domain.dto.AffiliateAccountAndImage;
import com.lela.domain.enums.ApplicationType;

/**
 * User: Bjorn Harvold
 * Date: 1/12/12
 * Time: 9:11 AM
 */
public class AffiliateManagerControllerFunctionalTest extends AbstractFunctionalTest {

    private static final Logger log = LoggerFactory.getLogger(AffiliateManagerControllerFunctionalTest.class);
    private static final String AFFILIATE_NM = AffiliateManagerControllerFunctionalTest.class.getSimpleName();
    private static final String AFFILIATE_RL = "http://google.com";
    private static final String AFFILIATE_ACCOUNT_NM = AffiliateManagerControllerFunctionalTest.class.getSimpleName();
    private static final String APPLICATION_URL_NAME = AffiliateManagerControllerFunctionalTest.class.getSimpleName() + "Application";
    private static final String QUIZ_URL_NAME = AffiliateManagerControllerFunctionalTest.class.getSimpleName() + "Quiz";

    @Autowired
    private AffiliateManagerController affiliateManagerController;

    @Autowired
    private AffiliateService affiliateService;

    @Autowired
    private LocalCacheEvictionService localCacheEvictionService;

    @Autowired
    private ApplicationService applicationService;

    private AffiliateAccount affiliateAccount = null;
    private AffiliateAccountAndImage affiliateAccountAndImage = null;
    private AffiliateAccountApplication affiliateAccountApplication = null;
    private Application application = null;

    @Before
    public void setUp() {
        SpringSecurityHelper.secureChannel();
        application = applicationService.findApplicationByUrlName(APPLICATION_URL_NAME);
        if (application == null) {
            application = new Application();
            application.setTrlnm(QUIZ_URL_NAME);
            application.setNm(APPLICATION_URL_NAME);
            application.setRlnm(APPLICATION_URL_NAME);
            application.setTp(ApplicationType.QUIZ);
            application.setPblshd(true);

            application = applicationService.saveApplication(application);
        }

        SpringSecurityHelper.unsecureChannel();
    }

    @After
    public void tearDown() {
        SpringSecurityHelper.secureChannel();
        if (application != null) {
            applicationService.removeApplication(application.getRlnm());
        }
        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testAffiliateManagerController() {

        log.info("Testing affiliate manager controller...");
        SpringSecurityHelper.secureChannel();
        Model model = null;
        String view = null;
        BindingResult errors = null;
        RedirectAttributes redirectAttributes;
        Locale locale = Locale.US;

        try {
            log.info("Listing all existing affiliate accounts...");
            model = new BindingAwareModelMap();
            view = affiliateManagerController.showAffiliateAccounts(0, true, model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.affiliate.account.list", view);
            log.info("Listing all existing affiliate accounts complete. We don't really care if this methods contains a list or not. We will be creating a record shortly.");

            log.info("Go to the affiliate account form page and get ready to create an affiliate account...");
            model = new BindingAwareModelMap();
            view = affiliateManagerController.showAffiliateAccountForm(null, model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.affiliate.account.form", view);
            assertNotNull("affiliate account object is null", model.asMap().get(WebConstants.AFFILIATE_ACCOUNT));
            affiliateAccountAndImage = (AffiliateAccountAndImage) model.asMap().get(WebConstants.AFFILIATE_ACCOUNT);
            affiliateAccount = affiliateAccountAndImage.getAffiliate();
            log.info("Go to the affiliate account form page and get ready to create a affiliate account complete");

            // Create an affiliate account
            log.info("Fill out affiliate account form object and submit form...");
            affiliateAccount = new AffiliateAccount();
            affiliateAccount.setId(new ObjectId());
            affiliateAccount.setNm(AFFILIATE_ACCOUNT_NM);
            affiliateAccount.setRlnm(AFFILIATE_ACCOUNT_NM);
            affiliateAccount.setCtv(true);

            affiliateAccountAndImage = new AffiliateAccountAndImage(affiliateAccount);

            errors = new BindException(affiliateAccount, WebConstants.AFFILIATE_ACCOUNT);
            redirectAttributes = new RedirectAttributesModelMap();
            model = new BindingAwareModelMap();
            view = affiliateManagerController.submitAffiliateAccount(affiliateAccountAndImage, errors, redirectAttributes, locale, model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "redirect:/administration/affiliateaccount/" + affiliateAccount.getRlnm(), view);
            assertTrue("Redirect attributes is empty", redirectAttributes.getFlashAttributes().containsKey(WebConstants.MESSAGE));
            log.info("Fill out affiliate account form object and submit form complete");

            log.info("Show the affiliate account page...");
            model = new BindingAwareModelMap();
            view = affiliateManagerController.showAffiliateAccount(affiliateAccount.getRlnm(), model); 
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.affiliate.account", view);
            assertNotNull("affiliate account object is null", model.asMap().get(WebConstants.AFFILIATE_ACCOUNT));
            affiliateAccountAndImage = (AffiliateAccountAndImage) model.asMap().get(WebConstants.AFFILIATE_ACCOUNT);
            affiliateAccount = affiliateAccountAndImage.getAffiliate();
            log.info("Show the affiliate account page complete");

            log.info("Now that we have a persisted affiliate account, time to add an application to it...");
            log.info("Show application form page");
            model = new BindingAwareModelMap();
            view = affiliateManagerController.showAffiliateAccountApplicationForm(affiliateAccount.getRlnm(), model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.affiliate.account.application.form", view);
            assertNotNull("application object is null", model.asMap().get(WebConstants.AFFILIATE_ACCOUNT_APPLICATION));
            affiliateAccountApplication = (AffiliateAccountApplication) model.asMap().get(WebConstants.AFFILIATE_ACCOUNT_APPLICATION);
            log.info("Application form loaded successfully");

            log.info("Time to fill in the application form and submit");
            affiliateAccountApplication = new AffiliateAccountApplication(affiliateAccount.getRlnm());
            affiliateAccountApplication.setRlnm(APPLICATION_URL_NAME);

            errors = new BindException(affiliateAccountApplication, WebConstants.AFFILIATE_ACCOUNT_APPLICATION);
            redirectAttributes = new RedirectAttributesModelMap();
            model = new BindingAwareModelMap();
            view = affiliateManagerController.submitAffiliateAccountApplication(affiliateAccount.getRlnm(), affiliateAccountApplication, errors, redirectAttributes, locale, model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "redirect:/administration/affiliateaccount/" + affiliateAccount.getRlnm(), view);
            assertTrue("Redirect attributes is empty", redirectAttributes.getFlashAttributes().containsKey(WebConstants.MESSAGE));
            log.info("Added affiliate application successfully to affiliate account");

            log.info("Show the affiliate account page again and verify the affiliate application was added...");
            model = new BindingAwareModelMap();
            view = affiliateManagerController.showAffiliateAccount(affiliateAccount.getRlnm(), model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.affiliate.account", view);
            assertNotNull("affiliate account object is null", model.asMap().get(WebConstants.AFFILIATE_ACCOUNT));
            affiliateAccountAndImage = (AffiliateAccountAndImage) model.asMap().get(WebConstants.AFFILIATE_ACCOUNT);
            affiliateAccount = affiliateAccountAndImage.getAffiliate();
            assertEquals("Application size incorrect", 1, affiliateAccount.getPps().size(), 0);
            log.info("Show the affiliate account page complete");

            log.info("Now we want to start removing entries in reverse order, starting with application");
            redirectAttributes = new RedirectAttributesModelMap();
            view = affiliateManagerController.deleteAffiliateAccountApplication(affiliateAccount.getRlnm(), affiliateAccountApplication.getRlnm(), redirectAttributes, locale);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "redirect:/administration/affiliateaccount/" + affiliateAccount.getRlnm(), view);
            assertTrue("Redirect attributes is empty", redirectAttributes.getFlashAttributes().containsKey(WebConstants.MESSAGE));
            log.info("Application removed");

            log.info("Displaying affiliate account page again to verify that both affiliates and applications have been removed.");
            model = new BindingAwareModelMap();
            view = affiliateManagerController.showAffiliateAccount(affiliateAccount.getRlnm(), model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.affiliate.account", view);
            assertNotNull("affiliate account object is null", model.asMap().get(WebConstants.AFFILIATE_ACCOUNT));
            affiliateAccountAndImage = (AffiliateAccountAndImage) model.asMap().get(WebConstants.AFFILIATE_ACCOUNT);
            affiliateAccount = affiliateAccountAndImage.getAffiliate();
            assertEquals("Application size incorrect", 0, affiliateAccount.getPps().size(), 0);
            log.info("Show the affiliate account page complete");

            log.info("Deleting affiliate account last");
            redirectAttributes = new RedirectAttributesModelMap();
            view = affiliateManagerController.deleteAffiliateAccount(affiliateAccount.getRlnm(), redirectAttributes, locale);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "redirect:/administration/affiliateaccount/list", view);
            assertTrue("Redirect attributes is empty", redirectAttributes.getFlashAttributes().containsKey(WebConstants.MESSAGE));

            log.info("Displaying affiliate account page again to verify affiliate account has been deleted.");
            model = new BindingAwareModelMap();
            view = affiliateManagerController.showAffiliateAccount(affiliateAccount.getRlnm(), model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.affiliate.account", view);

            affiliateAccountAndImage = (AffiliateAccountAndImage) model.asMap().get(WebConstants.AFFILIATE_ACCOUNT);
            affiliateAccount = affiliateAccountAndImage.getAffiliate();
            assertNull("affiliate account object is not null", affiliateAccount);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Caught exception in test: " + e.getMessage());
        } finally {
            if (affiliateAccount != null) {
                affiliateService.removeAffiliateAccount(affiliateAccount.getRlnm());
                localCacheEvictionService.evictAffiliateAccount(affiliateAccount.getRlnm());
            }
        }
        SpringSecurityHelper.unsecureChannel();

        log.info("Testing affiliate manager controller complete");
    }

    /*
    private void verifyCookieValue(MockHttpServletResponse response, String expectedCookieName, String expectedCookieValue) {
        assertNotNull("HttpServletResponse is null", response);
        assertNotNull("No cookie in response for cookie name: " + expectedCookieName, response.getCookie(expectedCookieName));
        assertEquals("Wrong cookie value for for cookie name: " + expectedCookieName, expectedCookieValue, response.getCookie(expectedCookieName).getValue());
    }
    */
}
