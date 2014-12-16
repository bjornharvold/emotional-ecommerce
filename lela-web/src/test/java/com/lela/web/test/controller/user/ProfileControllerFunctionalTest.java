/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.test.controller.user;

import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.DisplayLocale;
import com.lela.domain.dto.Principal;
import com.lela.domain.dto.Profile;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.domain.dto.user.ProfilePictureUpload;
import com.lela.web.test.AbstractFunctionalTest;
import com.lela.commons.web.utils.WebConstants;
import com.lela.web.web.controller.user.ProfileController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.handler.DispatcherServletWebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Locale;

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
public class ProfileControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(ProfileControllerFunctionalTest.class);
    private static final String PASSWORD = "password";

    private ClassPathResource profilePicture = new ClassPathResource("testdata/image/blogimage.png");

    private User user = null;
    private UserSupplement us = null;
    private Principal principal = null;

    @Autowired
    private ProfileController profileController;


    @Before
    public void setUp() {
        log.info("Creating user");
        user = createRandomUser(true, PASSWORD);
        us = createRandomUserSupplement(user);

        us = userService.saveUserSupplement(us);

        userService.saveUser(user);
        user = userService.findUserByEmail(user.getMl());

        assertNotNull("User id is null", user.getId());

        principal = new Principal(user);
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
    public void testProfileController() {
        log.info("Testing profile controller...");

        SpringSecurityHelper.secureChannel(principal);

        String view;
        Model map = new BindingAwareModelMap();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        WebRequest webRequest = new DispatcherServletWebRequest(request, response);
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
        HttpSession session = new MockHttpSession();

        try {
            log.info("Try to retrieve the user profile page again");
            view = profileController.showUser(session, map);
            assertEquals("Tile view is incorrect", "user.profile", view);
            assertNotNull("Countries are null", map.asMap().get(WebConstants.COUNTRIES));
            assertNotNull("Languages are null", map.asMap().get(WebConstants.LANGUAGES));

            // todo once we have data
//            assertNotNull("Industries are null", map.asMap().get(WebConstants.INDUSTRIES));
//            assertNotNull("Annual Household Incomes are null", map.asMap().get(WebConstants.ANNUAL_HOUSEHOLD_INCOME));
//            assertNotNull("Job titles are null", map.asMap().get(WebConstants.JOB_TITLE));
//            assertNotNull("Company sizes are null", map.asMap().get(WebConstants.COMPANY_SIZE));

            assertNotNull("Profile is null", map.asMap().get(WebConstants.PROFILE));

            Profile profile = (Profile) map.asMap().get(WebConstants.PROFILE);

            List<DisplayLocale> countries = (List<DisplayLocale>) map.asMap().get(WebConstants.COUNTRIES);
            System.out.println("Countries:");
            for (DisplayLocale locale : countries) {
                System.out.println(locale.getKey() + "=" + locale.getValue());
            }

            List<DisplayLocale> languages = (List<DisplayLocale>) map.asMap().get(WebConstants.LANGUAGES);
            System.out.println("Languages:");
            for (DisplayLocale locale : languages) {
                System.out.println(locale.getKey() + "=" + locale.getValue());
            }

            log.info("Adding some profile metrics");
            profile.setCmpnysz("1");

            log.info("Updating profile...");
            map = new BindingAwareModelMap();
            BindingResult results = new BindException(profile, WebConstants.PROFILE);
            session = new MockHttpSession();
            view = profileController.updateUserProfile(profile, results, webRequest, redirectAttributes, session, map, Locale.ENGLISH);
            assertEquals("Tile view is incorrect", "redirect:/user/profile", view);
            assertNotNull("Redirect attributes missing entry", redirectAttributes.getFlashAttributes().get(WebConstants.MESSAGE));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        SpringSecurityHelper.unsecureChannel();
        log.info("Testing profile controller complete");
    }

    @Test
    public void testDeactivateUser() {
        log.info("Testing that user deactivation works");

        SpringSecurityHelper.secureChannel(principal);

        try {
            String view = profileController.showDisableForm();
            assertNotNull("View is null", view);
            assertEquals("View is incorrect", "user.deactivate", view);

            log.info("First we test that the current user is authenticated");
            assertTrue("User is not authenticated", principal.isEnabled());

            HttpSession session = new MockHttpSession();
            view = profileController.disableUser(null, session);
            assertNotNull("View is null", view);
            assertEquals("View is incorrect", "redirect:/logout", view);

            user = userService.findUserByEmail(user.getMl());

            assertNull("User is not null", user);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        SpringSecurityHelper.unsecureChannel();
        log.info("Testing that user deactivation works. Successful!");
    }

    @Test
    public void testUploadProfilePicture() {
        log.info("Testing uploading a profile picture...");
        SpringSecurityHelper.secureChannel(principal);

        String view = null;

        try {

            view = profileController.showProfilePicture();
            assertNotNull("View is null", view);
            assertEquals("View is incorrect", "user.profile.picture", view);

            ProfilePictureUpload picture = new ProfilePictureUpload();
            MultipartFile file = new MockMultipartFile(profilePicture.getFilename(), profilePicture.getFilename(), "image/png", profilePicture.getInputStream());
            picture.setMultipartFile(file);

            Model model = new BindingAwareModelMap();
            BindingResult errors = new BindException(picture, "profilePictureUpload");
            RedirectAttributes ra = new RedirectAttributesModelMap();
            HttpSession session = new MockHttpSession();

            view = profileController.updateProfilePicture(picture, errors, ra, session, model, Locale.US);
            assertNotNull("View is null", view);
            assertEquals("View is incorrect", "redirect:/user/profile/picture", view);

            UserSupplement us = userService.findUserSupplement(user.getCd());
            assertNotNull("User is null", us);
            assertNotNull("User profile picture is null", us.getMg());
            assertEquals("User profile picture is null", 2, us.getMg().size(), 0);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        SpringSecurityHelper.unsecureChannel();
        log.info("Testing uploading a profile picture complete");
    }
}
