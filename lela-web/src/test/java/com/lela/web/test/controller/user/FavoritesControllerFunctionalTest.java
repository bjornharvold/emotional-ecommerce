/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.test.controller.user;

import com.lela.domain.document.User;
import com.lela.domain.dto.Favorite;
import com.lela.domain.dto.Principal;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.web.test.AbstractFunctionalTest;
import com.lela.commons.web.utils.WebConstants;
import com.lela.web.web.controller.user.FavoritesController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.handler.DispatcherServletWebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.servlet.http.HttpSession;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Created by Bjorn Harvold
 * Date: 8/3/11
 * Time: 10:05 AM
 * Responsibility: Handles all user functionality around favorites
 */
@SuppressWarnings("unchecked")
public class FavoritesControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(FavoritesControllerFunctionalTest.class);
    private User user = null;

    @Autowired
    private FavoritesController favoritesController;

    @Before
    public void setUp() {
        log.info("Creating user");
        user = createRandomUser(true);
        user = userService.saveUser(user);
        assertNotNull("User id is null", user.getId());

        Principal principal = new Principal(user);
        SpringSecurityHelper.secureChannel(principal);
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
    public void testFavoritesController() {
        log.info("Testing favorites controller...");
        String view;
        Model map = new BindingAwareModelMap();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        WebRequest webRequest = new DispatcherServletWebRequest(request, response);
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
        HttpSession session = new MockHttpSession();

        try {
            log.info("Try to retrieve the user profile page again");
            view = favoritesController.showFavorites(session, map);
            assertEquals("Tile view is incorrect", "user.favorites", view);

            // todo once we have data
//            assertNotNull("Car makers are null", map.asMap().get(WebConstants.CAR_MAKER));
//            assertNotNull("Shampoos are null", map.asMap().get(WebConstants.SHAMPOO));
//            assertNotNull("Fashion brands are null", map.asMap().get(WebConstants.FASHION_BRAND));
//            assertNotNull("Beers are null", map.asMap().get(WebConstants.BEER));
//            assertNotNull("Online stores are null", map.asMap().get(WebConstants.ONLINE_STORE));
//            assertNotNull("Grocery stores are null", map.asMap().get(WebConstants.GROCERY_STORE));

            assertNotNull("Favorite is null", map.asMap().get(WebConstants.FAVORITE));

            Favorite favorite = (Favorite) map.asMap().get(WebConstants.FAVORITE);

            log.info("Adding some profile metrics");
            favorite.setBr("Clausthaler");

            log.info("Updating favorites...");
            map = new BindingAwareModelMap();
            BindingResult results = new BindException(favorite, WebConstants.FAVORITE);
            view = favoritesController.updateFavorites(favorite, results, webRequest, redirectAttributes, session, map, Locale.ENGLISH);
            assertEquals("Tile view is incorrect", "redirect:/user/favorites", view);
            assertNotNull("Redirect attributes missing entry", redirectAttributes.getFlashAttributes().get(WebConstants.MESSAGE));
        } catch (Exception e) {
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing favorites controller complete");
    }

}
