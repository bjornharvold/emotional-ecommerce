/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.web.test.controller;

import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.User;
import com.lela.domain.dto.UserAttributes;
import com.lela.web.test.AbstractFunctionalTest;
import com.lela.web.web.controller.UserAttributeController;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Created by Bjorn Harvold
 * Date: 1/18/12
 * Time: 2:04 AM
 * Responsibility:
 */
public class UserAttributeControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(UserAttributeControllerFunctionalTest.class);

    @Autowired
    private UserAttributeController userAttributeController;

    @Before
    public void setUp() {
        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testUserAttributes() {
        log.info("Testing adding an attribute to the user...");
        User user = null;

        try {
            HttpSession session = new MockHttpSession();
            UserAttributes ua = new UserAttributes();

            Map<String, List<String>> map = new HashMap<String, List<String>>(1);
            List<String> values = new ArrayList<String>(1);
            values.add("20120512");
            values.add("20128956");

            // invalid value
            map.put("ddt", values);
            ua.setMap(map);

            log.info("First we test with invalid values");
            String view = userAttributeController.saveUserAttributes(ua, session);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "attributes.quiz", view);

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

            view = userAttributeController.saveUserAttributes(ua, session);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "attributes.quiz", view);

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

            view = userAttributeController.saveUserAttributes(ua, session);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "attributes.quiz", view);

            log.info("There should not be any attributes on the user at this point");
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
