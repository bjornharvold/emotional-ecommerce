/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.test.controller.user;

import com.lela.commons.service.ProfileService;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.Principal;
import com.lela.domain.dto.UserAccountDto;
import com.lela.commons.repository.UserRepository;
import com.lela.commons.service.UserService;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.web.test.AbstractFunctionalTest;
import com.lela.commons.web.utils.WebConstants;
import com.lela.web.web.controller.user.UserController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class UserControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(UserControllerFunctionalTest.class);
    private static final String QUESTION1 = "What is your favorite color?";
    private static final String ANSWER1= "Blue";
    private static final String QUESTION2 = "What was the year of your first car?";
    private static final String ANSWER2= "1980";

    @Autowired
    private UserController userController;

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
    public void testShowAndUpdateUserAccount() {
        Model model = new BindingAwareModelMap();
        String view = null;

        SpringSecurityHelper.secureChannel(new Principal(user));
        HttpSession session = new MockHttpSession();

        try {
            view = userController.showUserAccount(session, model);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        assertEquals("Tile view is incorrect", "user.dashboard.overview", view);
        assertNotNull("User Account DTO is null", model.asMap().get(WebConstants.USER_ACCOUNT_DTO));
        UserAccountDto userAccountDto = (UserAccountDto)model.asMap().get(WebConstants.USER_ACCOUNT_DTO);
        assertEquals("Email was not retrieved", userAccountDto.getMl().toLowerCase(), user.getMl());

        userAccountDto.setMl(user.getMl());

        try {
            view = userController.updateUserAccount(userAccountDto, session);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        assertEquals("Tile view is incorrect", "redirect:/user/dashboard/overview", view);

        model = new BindingAwareModelMap();
        try {
            view = userController.showUserCoupons(session, model);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        assertNotNull("Tile view is null", view);
        assertEquals("Tile view is incorrect", "user.dashboard.coupons", view);
        String page = (String) model.asMap().get(WebConstants.ACCOUNT);
        assertEquals("Account page is incorrect", "account-coupons", page);
        assertNotNull("User object is null", model.asMap().get(WebConstants.USER));
    }

}
