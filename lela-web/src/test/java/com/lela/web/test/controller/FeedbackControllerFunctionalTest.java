/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.test.controller;

import com.lela.domain.document.User;
import com.lela.domain.dto.Principal;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.web.test.AbstractFunctionalTest;
import com.lela.web.web.controller.FeedbackController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.handler.DispatcherServletWebRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Bjorn Harvold
 * Date: 8/3/11
 * Time: 10:05 AM
 * Responsibility:
 */
@SuppressWarnings("unchecked")
public class FeedbackControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(FeedbackControllerFunctionalTest.class);

    @Autowired
    private FeedbackController feedbackController;

    private User user = null;


    @Before
    public void setUp() {
        user = createRandomUser(true);
        user = userService.saveUser(user);
        assertNotNull("User id is null", user.getId());

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
    public void testFeedbackForm() {
        log.info("Testing feedback form...");

        try {
            MockHttpServletRequest request = new MockHttpServletRequest();
            MockHttpServletResponse response = new MockHttpServletResponse();
            WebRequest webRequest = new DispatcherServletWebRequest(request, response);
            Model model = new BindingAwareModelMap();

            log.info("Now we set the user in the spring security context");
            SpringSecurityHelper.secureChannel(new Principal(user));
            String view = feedbackController.showFeedback(model);
            assertNotNull("View is null");
            assertEquals("View name is incorrect", "feedback", view);

            SpringSecurityHelper.unsecureChannel();
        } catch (Exception e) {
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        assertTrue(true);
        log.info("Testing feedback form complete");
    }

    /*
    @Test
    public void testSubmitFeedbackForm() {
        log.info("Testing feedback form submission...");
        Model model = new BindingAwareModelMap();

        try {
            log.info("Filling out the feedback dto so we can run registration");
            Feedback dto = new Feedback();
            BindingResult results = new BindException(dto, WebConstants.FEEDBACK);

            log.info("First we submit with missing a user");
            String view = feedbackController.submitFeedbackForm(dto, results, model);
            assertNotNull("View is null");
            assertEquals("Tile view is incorrect", "feedback.error", view);

            log.info("Now we set the user in the spring security context");
            SpringSecurityHelper.secureChannel(new Principal(user));

            log.info("Now we submit with correct data");
            dto.setAgree(true);
            dto.setChange("Nothing");
            dto.setComparison(Comparison.BETTER);
            dto.setLike("Yes indeed");
            dto.setPaypalEmail(EMAIL);
            dto.setRecommendToFriend(RecommendToFriend.YES);

            view = feedbackController.submitFeedbackForm(dto, results, model);
            assertNotNull("View is null");
            assertEquals("Tile view is incorrect", "feedback.success", view);

            SpringSecurityHelper.unsecureChannel();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        assertTrue(true);
        log.info("Testing feedback form submission complete");
    }
    */
}
