/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.test.controller;

import com.lela.domain.document.User;
import com.lela.domain.dto.SubscribeToEmailList;
import com.lela.domain.dto.UnsubscribeFromEmailList;
import com.lela.web.test.AbstractFunctionalTest;
import com.lela.commons.web.utils.WebConstants;
import com.lela.web.web.controller.ListSubscriberController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Created by Bjorn Harvold
 * Date: 8/3/11
 * Time: 10:05 AM
 * Responsibility:
 */
@SuppressWarnings("unchecked")
public class ListSubscriberControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(ListSubscriberControllerFunctionalTest.class);

    /** Default is development */
    @Value("${mailchimp.list.id:937b0a100a}")
    private String listId;

    //private static final String EMAIL = "testuser-1kijmkhj@yopmail.com";
    private static final String BAD_EMAIL = "bjorn";
    private static final String BAD_LIST_ID = "BAD_LIST_ID";

    private static User user;

    @Autowired
    private ListSubscriberController listSubscriberController;

    @Before
    public void setup() {

        user = createRandomUser(true);
        userService.saveUser(user);
    }

    @After
    public void tearDown() {
        log.info("Finally we want to unsubscribe the user");
        UnsubscribeFromEmailList dto = new UnsubscribeFromEmailList(user.getMl(), listId);
        try {
            Future<String> result = userService.unsubscribeFromEmailList(dto);            
            assertNotNull("Future result is null", result);

            String message = result.get();
            assertEquals("Dto message is incorrect", WebConstants.SUCCESS, message);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testListSubscriber() {
        log.info("Testing list subscriber controller...");

        try {
            log.info("First we're going to try to sign up to a mailing list with a bad email");
            SubscribeToEmailList dto = new SubscribeToEmailList(BAD_EMAIL, listId, null);
            Future<String> result = userService.subscribeToEmailList(dto);
            assertNotNull("Future result is null", result);

            String message = result.get();
            assertEquals("Dto message is incorrect", WebConstants.FAILURE, message);

            dto = listSubscriberController.subscribeToEmailList(dto);
            assertEquals("Dto message is incorrect", WebConstants.FAILURE, dto.getMessage());

            log.info("First we're going to try to sign up to a mailing list with a bad list id");
            dto = new SubscribeToEmailList(user.getMl(), BAD_LIST_ID, null);
            result = userService.subscribeToEmailList(dto);
            assertNotNull("Future result is null", result);

            message = result.get();
            assertEquals("Dto message is incorrect", WebConstants.FAILURE, message);

            log.info("Signing up for an email list");
            dto = new SubscribeToEmailList(user.getMl(), listId, null);
            result = userService.subscribeToEmailList(dto);
            assertNotNull("Future result is null", result);

            message = result.get();
            assertEquals("Dto message is incorrect", WebConstants.SUCCESS, message);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        log.info("Testing list subscriber controller complete");
    }
}
