/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.test.controller;

import com.lela.commons.repository.VerificationRepository;
import com.lela.commons.service.ProfileService;
import com.lela.commons.service.UserService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.domain.document.*;
import com.lela.web.test.AbstractFunctionalTest;
import com.lela.web.web.controller.RegistrationController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import static org.junit.Assert.*;

/**
 * Created by Michael Ball
 * Date: 5/15/12
 * Time: 7:53 AM
 * Responsibility:
 */
@SuppressWarnings("unchecked")
public class RegistrationControllerVerifyFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(RegistrationControllerVerifyFunctionalTest.class);

    @Autowired
    private RegistrationController registrationController;

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationRepository verificationRepository;

    @Autowired
    private ProfileService profileService;

    private User user;
    private UserSupplement us;

    @Before
    public void setup() {
        SpringSecurityHelper.secureChannel();

        user = this.createRandomUser(true);
        us = createRandomUserSupplement(user);

        // need to save this before registerTestUser()
        userService.saveUserSupplement(us);

        profileService.registerTestUser(user);
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
    public void testVerifyEmailAddress() {
        log.info("Testing email verification");


        Model model = new BindingAwareModelMap();

        UserVerification verification = new UserVerification(user.getId(), user.getMl());
        verificationRepository.save(verification);

        try {
            String view = registrationController.verifyEmail(verification.getTkn(), model);
            assertNotNull("Verification Date not set", verificationRepository.findByToken(verification.getTkn()).getVrfdt());
            assertTrue("Email Verification Flag not set to true on User", userService.findUserSupplement(user.getCd()).getVrfd());
            assertEquals("Tile view is incorrect", "register.valid.verify", view);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        assertTrue(true);
        log.info("Testing email verification complete");
    }

    @Test
    public void testVerifyChangedEmailAddressRejected() {
        log.info("Testing rejected email verification");


        Model model = new BindingAwareModelMap();

        //Create a token
        UserVerification verification = new UserVerification(user.getId(), user.getMl());
        verificationRepository.save(verification);

        //Change the email address
        user.setMl("abc@123.com");
        userService.saveUser(user);
        us.setMl("abc@123.com");
        userService.saveUserSupplement(us);

        try {
            String view = registrationController.verifyEmail(verification.getTkn(), model);
            UserVerification v = verificationRepository.findByToken(verification.getTkn());
            assertNotNull("Token is null", v);
            assertNull("Verification Date set", v.getVrfdt());
            assertFalse("Email Verification Flag not set to true on User", userService.findUserSupplement(user.getCd()).getVrfd());
            assertEquals("Tile view is incorrect", "register.invalid.verify", view);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        assertTrue(true);
        log.info("Testing rejected email verification complete");
    }

    @Test
    public void testVerifyChangedEmailAddressMissingToken() {
        log.info("Testing rejected email verification");


        Model model = new BindingAwareModelMap();

        //Create a token
        UserVerification verification = new UserVerification(user.getId(), user.getMl());

        //There won't any verification in the db
        //verificationRepository.save(verification);

        //Change the email address
        user.setMl("abc@123.com");
        userService.saveUser(user);

        try {
            String view = registrationController.verifyEmail(verification.getTkn(), model);
            assertFalse("Email Verification Flag not set to true on User", userService.findUserSupplement(user.getCd()).getVrfd());
            assertEquals("Tile view is incorrect", "register.invalid.verify", view);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        assertTrue(true);
        log.info("Testing rejected email verification complete");
    }
}
