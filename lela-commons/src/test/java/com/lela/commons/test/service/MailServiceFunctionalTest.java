/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.test.service;

import com.lela.commons.service.ProfileService;
import com.lela.domain.document.User;
import com.lela.domain.enums.MailParameter;
import com.lela.commons.mail.MailServiceException;
import com.lela.commons.service.MailService;
import com.lela.commons.service.UserService;
import com.lela.commons.test.AbstractFunctionalTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Bjorn Harvold
 * Date: 6/23/11
 * Time: 11:16 AM
 * Responsibility:
 */
public class MailServiceFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(MailServiceFunctionalTest.class);
    private static final String PASSWORD = "johndoe";

    private static final Integer INVITATION_COUNT = 10;
    private static final String INVITATION_URL = "URL_TOKEN";

    @Autowired
    private MailService mailService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    private User user;
    private User relationUser1;
    private User relationUser2;

    @Before
    public void setUp() {
        user = createRandomUser(true);
        user = profileService.registerTestUser(user);

        relationUser1 = createRandomUser(true);
        relationUser1 = profileService.registerTestUser(relationUser1);

        relationUser2 = createRandomUser(true);
        relationUser2 = profileService.registerTestUser(relationUser2);
    }

    @After
    public void tearDown() {
        if (user != null) {
            userService.removeUser(user);
        }
        if (relationUser1 != null) {
            userService.removeUser(relationUser1);
        }
        if (relationUser2 != null) {
            userService.removeUser(relationUser2);
        }
    }

    @Test
    public void testMailService() {
        log.info("Testing mail service");

        try {
            Map<MailParameter, Object> params = new HashMap<MailParameter, Object>();
            params.put(MailParameter.PASSWORD, PASSWORD);

            mailService.sendRegistrationConfirmation(user.getMl(), params, Locale.ENGLISH);
        } catch (MailServiceException e) {
            log.info(e.getMessage(), e);
            fail("Something went wrong with the email process: " + e.getMessage());
        }

        log.info("Test mail service complete!");
    }

    @Test
    public void testRelationshipRequestMailService() {
        log.info("Testing relationship request mail service");

        try {
            Map<MailParameter, Object> parameters = new HashMap<MailParameter, Object>();
            parameters.put(MailParameter.USER_FIRST_NAME, "User 1");
            parameters.put(MailParameter.USER_LAST_NAME, "User 1 LN");

            mailService.sendRelationshipRequest(relationUser2.getMl(), parameters, Locale.ENGLISH);
        } catch (MailServiceException e) {
            log.info(e.getMessage(), e);
            fail("Something went wrong with the email process: " + e.getMessage());
        }

        log.info("Test relationship request mail service complete!");
    }

    @Test
    public void testRelationshipConfirmationMailService() {
        log.info("Testing relationship confirmation mail service");

        try {
            Map<MailParameter, Object> parameters = new HashMap<MailParameter, Object>();
            parameters.put(MailParameter.USER_FIRST_NAME, "User 2");
            parameters.put(MailParameter.USER_LAST_NAME, "User 2 LN");

            mailService.sendRelationshipConfirmation(relationUser1.getMl(), parameters, Locale.ENGLISH);
        } catch (MailServiceException e) {
            log.info(e.getMessage(), e);
            fail("Something went wrong with the email process: " + e.getMessage());
        }

        log.info("Test relationship confirmation mail service complete!");
    }

    @Test
    public void testSendShareWithFriends() {
        log.info("Testing share with friends mail service");

        try {
            Map<MailParameter, Object> parameters = new HashMap<MailParameter, Object>();
            parameters.put(MailParameter.INVITATION_COUNT, INVITATION_COUNT);
            parameters.put(MailParameter.INVITATION_URL, INVITATION_URL);

            mailService.sendShareWithFriends(user.getMl(), parameters, Locale.ENGLISH);
        } catch (MailServiceException e) {
            log.info(e.getMessage(), e);
            fail("Something went wrong with the email process: " + e.getMessage());
        }

        log.info("Test relationship confirmation mail service complete!");
    }

    @Test
    public void testSharedWithFriendsRegistrationConfirmation() {
        log.info("Testing share with friends registration confirmation mail service");

        try {
            Map<MailParameter, Object> params = new HashMap<MailParameter, Object>();
            params.put(MailParameter.PASSWORD, PASSWORD);
            params.put(MailParameter.USER_FIRST_NAME, "User 2");
            params.put(MailParameter.USER_LAST_NAME, "User 2 LN");

            mailService.sendRegistrationConfirmation(user.getMl(), params, Locale.ENGLISH);
        } catch (MailServiceException e) {
            log.info(e.getMessage(), e);
            fail("Something went wrong with the email process: " + e.getMessage());
        }

        log.info("Test share with friends registration confirmation mail service complete!");
    }

    @Test
    public void testAllInvitationsUsedConfirmation() {
        log.info("Testing all invitations used confirmation mail service");

        try {
            Map<MailParameter, Object> params = new HashMap<MailParameter, Object>();
            params.put(MailParameter.USER_FIRST_NAME, "Liz");
            params.put(MailParameter.BLOG, "Mom 101");

            mailService.sendAllInvitationsUsedConfirmation(user.getMl(), params, Locale.ENGLISH);
        } catch (MailServiceException e) {
            log.info(e.getMessage(), e);
            fail("Something went wrong with the email process: " + e.getMessage());
        }

        log.info("Testing all invitations used confirmation mail service complete!");
    }

    @Test
    public void testSendEmailNoLocaleMailService() {
        log.info("Testing mail service");

        try {
            mailService.sendPasswordResetEmail(user.getMl(), PASSWORD, null);
        } catch (MailServiceException e) {
            log.info(e.getMessage(), e);
            fail("Something went wrong with the email process send password reset email : " + e.getMessage());
        }

        try {
            Map<MailParameter, Object> params = new HashMap<MailParameter, Object>();
            params.put(MailParameter.PASSWORD, PASSWORD);
            params.put(MailParameter.USER_FIRST_NAME, "User");
            params.put(MailParameter.USER_LAST_NAME, "User LN");

            mailService.sendShareWithFriends(user.getMl(), params, null);
        } catch (MailServiceException e) {
            log.info(e.getMessage(), e);
            fail("Something went wrong with the email process send share with friends: " + e.getMessage());
        }

        try {
            Map<MailParameter, Object> params = new HashMap<MailParameter, Object>();
            params.put(MailParameter.PASSWORD, PASSWORD);

            mailService.sendRegistrationConfirmation(user.getMl(), params, null);
        } catch (MailServiceException e) {
            log.info(e.getMessage(), e);
            fail("Something went wrong with the email process send registration confirmation: " + e.getMessage());
        }

        try {
            Map<MailParameter, Object> parameters = new HashMap<MailParameter, Object>();
            parameters.put(MailParameter.USER_FIRST_NAME, "User 1");
            parameters.put(MailParameter.USER_LAST_NAME, "User 1 LN");

            mailService.sendRelationshipRequest(relationUser2.getMl(), parameters, null);
        } catch (MailServiceException e) {
            log.info(e.getMessage(), e);
            fail("Something went wrong with the email process send relationship request : " + e.getMessage());
        }

        try {
            Map<MailParameter, Object> parameters = new HashMap<MailParameter, Object>();
            parameters.put(MailParameter.PASSWORD, relationUser1.getPsswrd());
            parameters.put(MailParameter.USER_FIRST_NAME, "User 2");
            parameters.put(MailParameter.USER_LAST_NAME, "User 2 LN");

            mailService.sendRelationshipConfirmation(relationUser1.getMl(), parameters, null);
        } catch (MailServiceException e) {
            log.info(e.getMessage(), e);
            fail("Something went wrong with the email process send relationship confirmation: " + e.getMessage());
        }

        try {
            Map<MailParameter, Object> params = new HashMap<MailParameter, Object>();
            params.put(MailParameter.USER_FIRST_NAME, "Liz");
            params.put(MailParameter.BLOG, "Mom 101");

            mailService.sendAllInvitationsUsedConfirmation(user.getMl(), params, null);
        } catch (MailServiceException e) {
            log.info(e.getMessage(), e);
            fail("Something went wrong with the email process send all invitations used confirmation: " + e.getMessage());
        }

        log.info("Test send email no locale mail service complete!");
    }

    @Test
    public void testExceptionMailService() {
        log.info("Testing exception for merchant service...");

        MailServiceException e = new MailServiceException();

        e = new MailServiceException("test1");
        assertTrue("Error message not correct", e.getMessage().equals("test1"));

        e = new MailServiceException("test2", new Throwable("cause2"));
        assertTrue("Error message not correct", e.getMessage().equals("test2"));
        assertTrue("Error cause not correct", e.getCause().getMessage().equals("cause2"));

        e = new MailServiceException("test3", new Throwable("cause3"), "one3", "two3");
        assertTrue("Error message not correct", e.getMessage().equals("test3"));
        assertTrue("Error cause not correct", e.getCause().getMessage().equals("cause3"));
        String[] params = e.getParams();
        assertTrue("Error message param not correct", params[0].equals("one3"));
        assertTrue("Error message param not correct", params[1].equals("two3"));

        e = new MailServiceException("test4", "one4", "two4");
        assertTrue("Error message not correct", e.getMessage().equals("test4"));
        params = e.getParams();
        assertTrue("Error message param not correct", params[0].equals("one4"));
        assertTrue("Error message param not correct", params[1].equals("two4"));

        log.info("Testing exception for merchant service complete");
    }
}
