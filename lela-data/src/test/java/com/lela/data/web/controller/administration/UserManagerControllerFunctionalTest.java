/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.data.web.controller.administration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.lela.commons.service.ProfileService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.data.web.AbstractFunctionalTest;
import com.lela.domain.document.Motivator;
import com.lela.domain.document.Role;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.CustomPage;
import com.lela.domain.dto.report.UserUserSupplementEntry;
import com.lela.domain.dto.user.UserSearchQuery;
import com.lela.domain.enums.MotivatorSource;

/**
 * Created by Bjorn Harvold
 * Date: 8/3/11
 * Time: 10:05 AM
 * Responsibility:
 */
@SuppressWarnings("unchecked")
public class UserManagerControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(UserManagerControllerFunctionalTest.class);

    @Autowired
    private UserManagerController userManagerController;

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

        assertNotNull("User is null", user);
        assertNotNull("User ID is null", user.getId());

        SpringSecurityHelper.unsecureChannel();
    }

    @After
    public void tearDown() {
        SpringSecurityHelper.secureChannel();

        if (user != null) {
            userService.removeUser(user);

            user = userService.findUserByEmail(user.getMl());

            assertNull("User was not removed", user);
        }

        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testAdministrationDashboard() {
        log.info("Testing admin user controller...");
        SpringSecurityHelper.secureChannel();

        Model model = new BindingAwareModelMap();

        try {
            String view = userManagerController.showUsers(0, "10",  model);
            assertNotNull("view is null", view);
            assertEquals("Tile view is incorrect", "admin.user.list", view);
            log.info("Testing testing");
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        SpringSecurityHelper.unsecureChannel();
        log.info("Testing admin user controller complete");
    }

    @Test
    public void testSearchForUsersByQuery() {
        log.info("Testing search for user by query controller...");
        SpringSecurityHelper.secureChannel();

        Model model = new BindingAwareModelMap();

        try {
            UserSearchQuery query = new UserSearchQuery();
            query.setMl(user.getMl());
            String view = userManagerController.searchForUserByQuery(query, model);
            assertEquals("Tile view is incorrect", "admin.user.list", view);
            assertNotNull("User list is null", model.asMap().get(WebConstants.USERS));
            CustomPage<UserUserSupplementEntry> page = (CustomPage<UserUserSupplementEntry>) model.asMap().get(WebConstants.USERS);

            assertEquals("Result size is incorrect", 1l, page.getContent().size(), 0);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        SpringSecurityHelper.unsecureChannel();
        log.info("Testing search for user by query controller complete");
    }

    @Test
    public void testShowUser() {
        log.info("Testing show user...");
        SpringSecurityHelper.secureChannel();

        Model model = new BindingAwareModelMap();

        try {
            log.info("Retrieving user by user id");
            String view = userManagerController.showUser(user.getId(), model);
            assertEquals("Tile view is incorrect", "admin.user.data", view);
            assertNotNull("User is null", model.asMap().get(WebConstants.USER));
            assertNotNull("Roles is null", model.asMap().get(WebConstants.ROLES));
            User tempUser = (User) model.asMap().get(WebConstants.USER);
            int userRoles1 = tempUser.getRrlnms().size();

            List<Role> roles = (List<Role>) model.asMap().get(WebConstants.ROLES);
            assertFalse("Roles is empty", roles.isEmpty());

            String roleUrlName = roles.get(0).getRlnm();
            log.info("Adding a new role to user");
            view = userManagerController.addUserRole(user.getId(), roleUrlName);
            assertEquals("Tile view is incorrect", "redirect:/administration/user/" + user.getIdString(), view);

            log.info("Verifying that the role got added to the user");
            view = userManagerController.showUser(user.getId(), model);
            tempUser = (User) model.asMap().get(WebConstants.USER);
            int userRoles2 = tempUser.getRrlnms().size();
            assertTrue("User did not get the new role", userRoles2 > userRoles1);

            log.info("Removing the role again from user");
            view = userManagerController.removeUserRole(user.getId(), roleUrlName);
            assertEquals("Tile view is incorrect", "redirect:/administration/user/" + user.getIdString(), view);

            log.info("Verifying that the role got removed from the user");
            view = userManagerController.showUser(user.getId(), model);
            tempUser = (User) model.asMap().get(WebConstants.USER);
            int userRoles3 = tempUser.getRrlnms().size();
            assertTrue("User did not have the new role removed", userRoles1 == userRoles3);

        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        SpringSecurityHelper.unsecureChannel();
        log.info("Testing show user complete");
    }

    @Test
    public void testGenerateUserMotivatorReport() {
        log.info("Testing generating user motivator report...");
        SpringSecurityHelper.secureChannel();
        User user = null;
        HttpServletResponse response = new MockHttpServletResponse();

        try {
            log.info("First we need to create at least one users that has motivators");

            user = createRandomUser(true);
            user = profileService.registerTestUser(user);

            Map<String, Integer> userMotivators = new HashMap<String, Integer>();
            userMotivators.put("A", 8);
            userMotivators.put("B", 9);
            userMotivators.put("C", 3);
            userMotivators.put("D", 8);
            userMotivators.put("E", 9);
            userMotivators.put("F", 8);
            userMotivators.put("G", 8);

            Motivator motivator = new Motivator(MotivatorSource.QUIZ, userMotivators);
            motivator = userService.saveMotivator(user.getCd(), motivator);
            assertNotNull("Motivator is null", motivator);

            userManagerController.generateUserMotivatorReport(response);
            String contentType = response.getContentType();
            String header = ((MockHttpServletResponse)response).getHeader("Content-Disposition");
            assertEquals("Content type incorrect", "application/vnd.ms-excel.12", contentType);
            assertEquals("Content header incorrect", "attachment; filename=user-motivator-report.xlsx", header);

        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        } finally {
            userService.removeUser(user);
        }

        SpringSecurityHelper.unsecureChannel();
        log.info("Testing generating user motivator report complete");
    }

}
