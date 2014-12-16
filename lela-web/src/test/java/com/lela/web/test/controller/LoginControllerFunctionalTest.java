/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.test.controller;

import com.lela.commons.service.ProfileService;
import com.lela.commons.service.UserService;
import com.lela.commons.spring.mobile.MockDevice;
import com.lela.commons.spring.mobile.WurflDevice;
import com.lela.domain.document.User;
import com.lela.domain.dto.Login;
import com.lela.domain.dto.LoginStatus;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.domain.dto.Principal;
import com.lela.web.test.AbstractFunctionalTest;
import com.lela.web.web.controller.LoginController;
import com.lela.commons.web.utils.WebConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.ui.Model;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.handler.DispatcherServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Bjorn Harvold
 * Date: 8/3/11
 * Time: 10:05 AM
 * Responsibility:
 */
@SuppressWarnings("unchecked")
public class LoginControllerFunctionalTest extends AbstractFunctionalTest {

    private MockDevice mockDevice = new MockDevice(MockDevice.DEVICE_TYPE.NORMAL);
    private Device device = new WurflDevice(mockDevice);

    private static final Logger log = LoggerFactory.getLogger(LoginControllerFunctionalTest.class);
    private static final String REDIRECT_URL = "redirect/url";

    @Autowired
    private LoginController loginController;

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    private User user;
    private User adminUser;

    @Before
    public void setUp() {
        SpringSecurityHelper.unsecureChannel();

        user = createRandomUser(true);
        user = profileService.registerTestUser(user);
        user = userService.findUserByEmail(user.getMl());

        adminUser = createRandomAdminUser(true);
        adminUser = profileService.registerTestUser(adminUser);
        adminUser = userService.findUserByEmail(adminUser.getMl());
    }

    @After
    public void tearDown() {
        SpringSecurityHelper.secureChannel();

        if (user != null) {
            userService.removeUser(user);
        }

        if (adminUser != null) {
            userService.removeUser(adminUser);
        }

        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testLoginRedirect() {
        log.info("Testing login redirect...");
        MockHttpSession session = new MockHttpSession();
        MockHttpServletRequest request = new MockHttpServletRequest();

        try {
            log.info("First we expect the redirect to redirect us to the login page because there is no SecurityContext");
            String view = loginController.redirect(session, request);
            assertEquals("Tile view is incorrect", "redirect:/login?success=false", view);

            log.info("Now we're going to set a security context for an admin. That should change the redirect.");
            SpringSecurityHelper.secureChannel(new Principal(adminUser));

            view = loginController.redirect(session, request);
            assertEquals("Tile view is incorrect", "redirect:/administration/dashboard", view);

            log.info("Now we're going to set a security context for a user. That should change the redirect.");

            SpringSecurityHelper.secureChannel(new Principal(user));

            log.info("Testing without desired redirect");
            view = loginController.redirect(session, request);
            assertEquals("Tile view is incorrect", "redirect:/", view);

            log.info("Testing with desired redirect");
            session.setAttribute(WebConstants.REDIRECT, REDIRECT_URL);
            view = loginController.redirect(session, request);
            assertEquals("Tile view is incorrect", "redirect:" + REDIRECT_URL, view);

            log.info("End it with unsecuring the spring security context");
            SpringSecurityHelper.unsecureChannel();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        log.info("Testing login redirect complete");
    }

    @Test
    public void testLogin() {
        log.info("Testing login...");
        HttpSession session = new MockHttpSession();
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        Model model = new BindingAwareModelMap();
        WebRequest webRequest = new DispatcherServletWebRequest(request, response);

        try {
            String view = loginController.login(null, webRequest, model, device, session);
            assertEquals("Tile view is incorrect", "login", view);

            view = loginController.login(REDIRECT_URL, webRequest, model, device, session);
            assertEquals("Tile view is incorrect", "login", view);
            assertNotNull("Redirect url is missing", session.getAttribute(WebConstants.REDIRECT));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing login complete");
    }
}
