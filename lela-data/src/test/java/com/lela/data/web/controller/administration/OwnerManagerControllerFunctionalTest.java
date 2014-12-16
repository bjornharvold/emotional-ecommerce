/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.data.web.controller.administration;

import com.lela.domain.document.Item;
import com.lela.domain.document.RelevantItem;
import com.lela.domain.document.User;
import com.lela.domain.dto.OwnerItemsQuery;
import com.lela.domain.dto.Principal;
import com.lela.commons.service.UserService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.data.web.AbstractFunctionalTest;

import com.lela.commons.web.utils.WebConstants;
import com.lela.data.web.controller.administration.owner.OwnerManagerController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import java.util.List;

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
@SuppressWarnings("unchecked")
public class OwnerManagerControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(OwnerManagerControllerFunctionalTest.class);

    @Autowired
    private OwnerManagerController ownerManagerController;

    @Autowired
    private UserService userService;

    private User user = null;

    @Before
    public void setUp() {
        user = createRandomAdminUser(true);

        user = userService.saveUser(user);

        user = userService.findUserByEmail(user.getMl());

        assertNotNull("User is null", user);

        SpringSecurityHelper.secureChannel(new Principal(user));
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
    public void testItemsWithBrandAndMotivators() {
        log.info("Testing admin owner controller with brand and Motivators...");
        Model model = new BindingAwareModelMap();

        try {
            String view = ownerManagerController.showOwnersPage(model);
            assertEquals("Tile view is incorrect", "administration.owners", view);
            assertNotNull("Owner query is null", model.asMap().get(WebConstants.OWNER_QUERY));

            OwnerItemsQuery query = (OwnerItemsQuery) model.asMap().get(WebConstants.OWNER_QUERY);
            query.setNm("bug");

            MockHttpServletResponse response = new MockHttpServletResponse();

            view = ownerManagerController.showOwnersData(query, response, model);
            assertEquals("Tile view is incorrect", "administration.owners.data", view);
            assertNotNull("Owner query is null", model.asMap().get(WebConstants.OWNER_QUERY));
            assertNotNull("Owner search result is null", model.asMap().get(WebConstants.OWNER_SEARCH_RESULT));

            query.setA(2);
            query.setB(2);
            query.setC(2);
            query.setD(2);
            query.setE(2);
            query.setF(2);
            query.setG(2);

            view = ownerManagerController.showOwnersData(query, response, model);
            assertEquals("Tile view is incorrect", "administration.owners.data", view);
            assertNotNull("Owner query is null", model.asMap().get(WebConstants.OWNER_QUERY));
            assertNotNull("Owner search result is null", model.asMap().get(WebConstants.OWNER_SEARCH_RESULT));
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        log.info("Testing admin owner controller complete");
    }

    @Test
    public void testItemsWithBrandOnly() {
        log.info("Testing admin owner controller with brand only...");
        Model model = new BindingAwareModelMap();

        try {
            String view = ownerManagerController.showOwnersPage(model);
            assertEquals("Tile view is incorrect", "administration.owners", view);
            assertNotNull("Owner query is null", model.asMap().get(WebConstants.OWNER_QUERY));

            OwnerItemsQuery query = (OwnerItemsQuery) model.asMap().get(WebConstants.OWNER_QUERY);
            query.setNm("bug");

            MockHttpServletResponse response = new MockHttpServletResponse();

            view = ownerManagerController.showOwnersData(query, response, model);
            assertEquals("Tile view is incorrect", "administration.owners.data", view);
            assertNotNull("Owner query is null", model.asMap().get(WebConstants.OWNER_QUERY));
            assertNotNull("Owner search result is null", model.asMap().get(WebConstants.OWNER_SEARCH_RESULT));

            view = ownerManagerController.showOwnersData(query, response, model);
            assertEquals("Tile view is incorrect", "administration.owners.data", view);
            assertNotNull("Owner query is null", model.asMap().get(WebConstants.OWNER_QUERY));
            assertNotNull("Owner search result is null", model.asMap().get(WebConstants.OWNER_SEARCH_RESULT));
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        log.info("Testing admin owner controller complete");
    }

    @Test
    public void testItemsWithBandsOnly() {
        log.info("Testing admin owner controller with brand only...");
        Model model = new BindingAwareModelMap();

        try {
            String view = ownerManagerController.showOwnersPage(model);
            assertEquals("Tile view is incorrect", "administration.owners", view);
            assertNotNull("Owner query is null", model.asMap().get(WebConstants.OWNER_QUERY));

            OwnerItemsQuery query = (OwnerItemsQuery) model.asMap().get(WebConstants.OWNER_QUERY);
            query.setNm("bug or graco");

            MockHttpServletResponse response = new MockHttpServletResponse();

            view = ownerManagerController.showOwnersData(query, response, model);
            assertEquals("Tile view is incorrect", "administration.owners.data", view);
            assertNotNull("Owner query is null", model.asMap().get(WebConstants.OWNER_QUERY));
            assertNotNull("Owner search result is null", model.asMap().get(WebConstants.OWNER_SEARCH_RESULT));

            view = ownerManagerController.showOwnersData(query, response, model);
            assertEquals("Tile view is incorrect", "administration.owners.data", view);
            assertNotNull("Owner query is null", model.asMap().get(WebConstants.OWNER_QUERY));
            assertNotNull("Owner search result is null", model.asMap().get(WebConstants.OWNER_SEARCH_RESULT));

        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        log.info("Testing admin owner controller complete");
    }

    @Test
    public void testItemsWithMotivatorsOnly() {
        log.info("Testing admin owner controller...");
        Model model = new BindingAwareModelMap();

        try {
            String view = ownerManagerController.showOwnersPage(model);
            assertEquals("Tile view is incorrect", "administration.owners", view);
            assertNotNull("Owner query is null", model.asMap().get(WebConstants.OWNER_QUERY));

            OwnerItemsQuery query = (OwnerItemsQuery) model.asMap().get(WebConstants.OWNER_QUERY);

            MockHttpServletResponse response = new MockHttpServletResponse();

            view = ownerManagerController.showOwnersData(query, response, model);
            assertEquals("Tile view is incorrect", "administration.owners.data", view);
            assertNotNull("Owner query is null", model.asMap().get(WebConstants.OWNER_QUERY));
            assertNotNull("Owner search result is null", model.asMap().get(WebConstants.OWNER_SEARCH_RESULT));

            query.setA(2);
            query.setB(2);
            query.setC(2);
            query.setD(2);
            query.setE(2);
            query.setF(2);
            query.setG(2);

            view = ownerManagerController.showOwnersData(query, response, model);
            assertEquals("Tile view is incorrect", "administration.owners.data", view);
            assertNotNull("Owner query is null", model.asMap().get(WebConstants.OWNER_QUERY));
            assertNotNull("Owner search result is null", model.asMap().get(WebConstants.OWNER_SEARCH_RESULT));

        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        log.info("Testing admin owner controller complete");
    }
}
