/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.test.service;

import com.lela.commons.service.ProfileService;
import com.lela.commons.service.UserService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.test.AbstractFunctionalTest;
import com.lela.domain.document.User;
import junit.framework.Assert;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Bjorn Harvold
 * Date: 6/23/11
 * Time: 11:16 AM
 * Responsibility:
 */
public class UserServiceFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(UserServiceFunctionalTest.class);
    private static final String INVALID_EMAIL = "testuser-1kijmkhj@yopmail.com";
    
    private final String SPECIFIC_EMAIL = "popeye@gmail.fake";
    private final String SPECIFIC_EMAIL_PARTIAL_1 = "popeye@gmail.fak";
    private final String SPECIFIC_EMAIL_PARTIAL_2 = "opeye@gmail.fak";
    private final String SPECIFIC_EMAIL_PARTIAL_3 = "pope@gmail.fake";
    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    private User user;
    private User specificUser;

    @Before
    public void setUp() {
        user = createRandomUser(true);
        user = profileService.registerTestUser(user);
        specificUser = createSpecificUser(true);
        specificUser.setMl(SPECIFIC_EMAIL);
        user = profileService.registerTestUser(specificUser);
    }

    @After
    public void teardown() {
        SpringSecurityHelper.secureChannel();

        if (user != null) {
            userService.removeUser(user);
        }
        if (specificUser != null){
        	userService.removeUser(specificUser);
        }
        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testUserService() {
        SpringSecurityHelper.secureChannel();

        log.info("Retrieving user...");
        user = userService.findUser(user.getId());
        assertNotNull("User is missing", user);
        assertNotNull("User is missing an id", user.getId());

        user = userService.findUser(new ObjectId(user.getId().toString()));
        assertNotNull("User is missing", user);
        assertNotNull("User is missing an id", user.getId());

        user = userService.findUserByEmail(user.getMl());
        assertNotNull("User is missing", user);
        assertNotNull("User is missing an id", user.getId());

        log.info("User retrieved successfully");

        log.info("Deleting user...");
        userService.removeUser(user);

        user = userService.findUser(user.getId());
        assertNull("User still exists", user);

        log.info("Deleting user successful");
        log.info("Test complete!");
        SpringSecurityHelper.unsecureChannel();
    }
    
    @Test
    public void testFindUserByEmailBasedOnEmailLength() {
        log.info("Testing find user by email on user service based on the length of the email address");

        User user = userService.findUserByEmail(SPECIFIC_EMAIL);
        Assert.assertNotNull(user);
        
        log.info("Using partial email addresses and ensure that the user is not found...");
        user = userService.findUserByEmail(SPECIFIC_EMAIL_PARTIAL_1);
        Assert.assertNull(user);
        user = userService.findUserByEmail(SPECIFIC_EMAIL_PARTIAL_2);
        Assert.assertNull(user);
        user = userService.findUserByEmail(SPECIFIC_EMAIL_PARTIAL_3);
        Assert.assertNull(user);        
        

        log.info("Testing find user by email based on email length complete");
    }

    @Test
    public void testLoadUserByUsername() {
        log.info("Testing load user by username on user service");
        UserDetails userDetails = userService.loadUserByUsername(user.getMl());
        assertNotNull("UserDetails is null", userDetails);
        assertEquals("UserDetails is null", userDetails.getUsername(), user.getMl());
        log.info("Testing find user ids by social network on user service complete");

        try {
            userDetails = userService.loadUserByUsername(INVALID_EMAIL);
            fail("Invalid email didn't throw exception");
        } catch(UsernameNotFoundException e) {
            assertTrue(true);
        }
        log.info("Testing load user by username on user service complete");
    }

    @Test
    public void testEncryptPassword() {
        log.info("Test encrypt password...");
        String rawPassword = "password";
        String encryptPassword = userService.encryptPassword(rawPassword);
        assertFalse("Encryption did not occur", rawPassword.equals(encryptPassword));
        log.info("Test encrypt password complete");
    }

    @Test
    public void testSaveUsers() {
        log.info("Testing load user by username on user service");
        User user1 = userService.findUserByEmail(user.getMl());
        List<User> users = new ArrayList<User>();
        users.add(user1);
        userService.saveUsers(users);
        log.info("Testing load user by username on user service complete");
    }

}
