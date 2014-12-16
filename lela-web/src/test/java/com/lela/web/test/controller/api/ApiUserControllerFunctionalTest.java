/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.test.controller.api;

import com.lela.domain.ApplicationConstants;
import com.lela.commons.service.AffiliateService;
import com.lela.commons.service.ApplicationService;
import com.lela.commons.service.UserService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.AffiliateAccountApplication;
import com.lela.domain.document.Application;
import com.lela.domain.document.User;
import com.lela.domain.dto.user.RegisterUserRequest;
import com.lela.domain.dto.user.RegisterUserResponse;
import com.lela.domain.enums.ApplicationType;
import com.lela.web.test.AbstractFunctionalTest;
import com.lela.web.web.controller.api.ApiUserController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.handler.DispatcherServletWebRequest;

import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
public class ApiUserControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(ApiUserControllerFunctionalTest.class);
    private static final String EMAIL = "bjornie@gmail.com";
    private static final String AFFILIATE_URL_NAME = "ApiUserControllerFunctionalTestAffiliate";
    private static final String APPLICATION_URL_NAME = "ApiUserControllerFunctionalTestApplication";
    private static final String QUIZ_URL_NAME = "ApiUserControllerFunctionalTestQuiz";

    @Autowired
    private ApiUserController apiUserController;

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private AffiliateService affiliateService;

    private User user = null;
    private Application application = null;
    private AffiliateAccount affiliateAccount;

    @Before
    public void setUp() {
        SpringSecurityHelper.secureChannel();
        User user = userService.findUserByEmail(EMAIL);

        if (user != null) {
            userService.removeUser(user);
        }

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

        affiliateAccount = affiliateService.findAffiliateAccountByUrlName(AFFILIATE_URL_NAME);
        if (affiliateAccount == null) {
            affiliateAccount = new AffiliateAccount();
            affiliateAccount.setCtv(true);
            affiliateAccount.setNm(AFFILIATE_URL_NAME);
            affiliateAccount.setRlnm(AFFILIATE_URL_NAME);

            List<AffiliateAccountApplication> pps = new ArrayList<AffiliateAccountApplication>(1);
            AffiliateAccountApplication app = new AffiliateAccountApplication();
            app.setTp(ApplicationType.QUIZ);
            app.setDt(new Date());
            app.setRlnm(APPLICATION_URL_NAME);
            pps.add(app);
            affiliateAccount.setPps(pps);

            affiliateAccount = affiliateService.saveAffiliateAccount(affiliateAccount);
        }

        SpringSecurityHelper.unsecureChannel();
    }

    @After
    public void tearDown() {
        SpringSecurityHelper.secureChannel();

        User user = userService.findUserByEmail(EMAIL);

        if (user != null) {
            userService.removeUser(user);
        }
        if (application != null) {
            applicationService.removeApplication(application.getRlnm());
        }
        if (affiliateAccount != null) {
            affiliateService.removeAffiliateAccount(affiliateAccount.getRlnm());
        }

        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testApiUserController() {
        RegisterUserRequest userRequest = null;
        RegisterUserResponse userResponse = null;

        log.info("Testing api user controller...");

        MockHttpServletRequest request = null;
        MockHttpServletResponse response = null;
        MockHttpSession session = null;
        NativeWebRequest webRequest = null;

        try {
            log.info("First we try to register an invalid user...");
            request = new MockHttpServletRequest("POST", "/api/user");
            session = new MockHttpSession();
            request.setSession(session);
            response = new MockHttpServletResponse();
            webRequest = new DispatcherServletWebRequest(request, response);

            userRequest = new RegisterUserRequest();
            userResponse = apiUserController.registerApiUser(userRequest, webRequest, request, response, session, Locale.US);
            assertEquals("Response code is incorrect", HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
            assertEquals("Message is incorrect", ApplicationConstants.APPLICATION_MISSING_DATA, userResponse.getMessage());

            log.info("Then we try to register a valid user");

            log.info("But first we're going to use the valid user data with a real IP");
            request = new MockHttpServletRequest("POST", "/api/user");
            session = new MockHttpSession();
            request.setSession(session);
            response = new MockHttpServletResponse();
            userRequest = new RegisterUserRequest();
            userRequest.setEmail(EMAIL);
            userRequest.setAffiliateId(AFFILIATE_URL_NAME);
            userRequest.setApplicationId(APPLICATION_URL_NAME);
            userRequest.setOptin(false);
            userRequest.setLocale(Locale.US);
            userResponse = apiUserController.registerApiUser(userRequest, webRequest, request, response, session, Locale.US);
            assertEquals("Email is incorrect", userResponse.getEmail(), userRequest.getEmail());
            assertEquals("Response code is incorrect", HttpServletResponse.SC_OK, response.getStatus());
            assertEquals("Message is incorrect", ApplicationConstants.APPLICATION_CREATED_USER, userResponse.getMessage());

            log.info("Let's try to register the same user again. This should not work.");
            request = new MockHttpServletRequest("POST", "/api/user");
            session = new MockHttpSession();
            request.setSession(session);
            response = new MockHttpServletResponse();
            userResponse = apiUserController.registerApiUser(userRequest, webRequest, request, response, session, Locale.US);
            assertEquals("Response code is incorrect", HttpServletResponse.SC_OK, response.getStatus());
            assertEquals("Email is incorrect", userRequest.getEmail(), userResponse.getEmail());
            assertEquals("Message is incorrect", ApplicationConstants.APPLICATION_EXISTING_USER, userResponse.getMessage());


            log.info("Registering user complete");
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            fail(ex.getMessage());
        }

        log.info("Testing api user controller complete");
    }

}
