/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.test.service;

import com.lela.commons.service.RoleService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.test.AbstractFunctionalTest;
import com.lela.domain.document.Role;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Created by Bjorn Harvold
 * Date: 6/4/12
 * Time: 7:53 PM
 * Responsibility:
 */
public class RoleServiceFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(RoleServiceFunctionalTest.class);

    @Autowired
    private RoleService roleService;

    @Test
    public void testRole() {
        SpringSecurityHelper.unsecureChannel();

        log.info("Create a role and save it");
        Role r = new Role();
        r.setNm("roleTest");
        r.setRlnm("roletest");
        String[] rights = {"RIGHT_TAKE_BREAK"};
        r.setRghts(Arrays.asList(rights));

        try {
            roleService.saveRole(r);
            fail("Role should not be able to be saved here. Missing credentials.");
        } catch (Exception ex) {
            log.info("Tried to save role without credentials. An exception was expected: " + ex.getMessage());
        }

        log.info("Securing channel...");
        SpringSecurityHelper.secureChannel();
        log.info("Channel secured");

        log.info("Saving a role. This time with a secure channel");
        try {
            r = roleService.saveRole(r);
            assertNotNull("Role is missing an id", r.getId());
            log.info("Role persisted successfully");
        } catch (Exception ex) {
            fail("Did not expect an exception here: " + ex.getMessage());
            log.info("Was not able to persist a role within secure context", ex);
        }

        log.info("Retrieving role...");
        r = roleService.findRoleByUrlName(r.getRlnm());
        assertNotNull("Role is missing", r);
        assertNotNull("Role is missing an id", r.getId());
        log.info("Role retrieved successfully");

        log.info("Deleting role...");
        roleService.removeRole(r.getRlnm());

        r = roleService.findRoleByUrlName(r.getRlnm());
        assertNull("Role still exists", r);

        log.info("Deleted role successfully");
        log.info("Test complete!");
    }
}
