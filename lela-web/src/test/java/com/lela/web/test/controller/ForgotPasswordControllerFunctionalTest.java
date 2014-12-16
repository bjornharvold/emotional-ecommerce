/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.test.controller;

import com.lela.commons.service.ProfileService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.domain.document.User;
import com.lela.commons.service.UserService;
import com.lela.domain.document.UserSupplement;
import com.lela.web.test.AbstractFunctionalTest;
import com.lela.web.web.controller.ForgotPasswordController;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Created by Bjorn Harvold
 * Date: 8/3/11
 * Time: 10:05 AM
 * Responsibility:
 */
@SuppressWarnings("unchecked")
public class ForgotPasswordControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(ForgotPasswordControllerFunctionalTest.class);

    @Autowired
    private ForgotPasswordController forgotPasswordController;

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    private User user;
    private UserSupplement us;

    @Before
    public void setUp() {
        SpringSecurityHelper.secureChannel();
        user = createRandomUser(true);
        us = createRandomUserSupplement(user);

        // this needs to save before registerTestUser()
        us = userService.saveUserSupplement(us);

        user = profileService.registerTestUser(user);
        SpringSecurityHelper.unsecureChannel();
    }

    @After
    public void tearDown() {
        SpringSecurityHelper.secureChannel();
        if (user != null) {
            userService.removeUser(user);
        }
        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testForgotPassword() {
        log.info("Testing forgot password controller...");

        Model model = new BindingAwareModelMap();

        try {
            String oldPassword = user.getPsswrd();

            log.info("First we want to see the forgot password tile");
            String view = forgotPasswordController.showForgotPassword();
            assertEquals("Tile view is incorrect", "forgot.password", view);

            log.info("Then we want to reset the password");
            String redirect = forgotPasswordController.forgotPassword(user.getMl(), model, Locale.US);
            assertEquals("Status message incorrect", "forgot.password", redirect);

            log.info("Retrieve the user again to verify the password changed");
            user = userService.findUserByEmail(user.getMl());
            String newPassword = user.getPsswrd();
            assertNotNull("User: " + user.getMl() + " does not exist", user);
            assertFalse("Password was not successful reset", StringUtils.equals(oldPassword, newPassword));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing forgot password controller complete");
    }
}
