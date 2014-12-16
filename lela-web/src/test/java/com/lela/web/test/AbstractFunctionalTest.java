/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.web.test;

import com.icegreen.greenmail.util.GreenMail;
import com.lela.commons.service.UserService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.domain.document.UserSupplement;
import com.lela.util.test.AbstractTest;
import com.lela.util.utilities.GreenMailHelper;
import com.lela.domain.document.User;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Bjorn Harvold
 * Date: 6/23/11
 * Time: 11:12 AM
 * Responsibility:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/META-INF/spring/applicationContext.xml", "/META-INF/spring/test.xml",
        "/META-INF/spring/webmvc-component-scan.xml",
        "/META-INF/spring/webmvc-config.xml",
        "/META-INF/spring/webmvc-security.xml",
        "/META-INF/spring/webmvc-interceptors.xml"
})
public abstract class AbstractFunctionalTest extends AbstractTest {

    @Autowired
    protected UserService userService;

    protected static GreenMail greenMail;

    @BeforeClass
    public static void beforeClass() {
        greenMail = GreenMailHelper.getGreenMail();
    }

    @AfterClass
    public static void afterClass() {
        GreenMailHelper.stopGreenMail();
    }

    protected User createRandomUser(boolean enabled) {
        return createRandomUser(enabled, null);
    }

    protected UserSupplement createRandomUserSupplement(User user) {
        UserSupplement us = new UserSupplement(user.getCd());
        us.setMl(user.getMl());
        us.setLcl(Locale.US);
        us.setFnm("First name");
        us.setLnm("Last name");

        return us;
    }

    protected User createRandomUser(boolean enabled, String password) {
        User user = new User();
        user.setMl((RandomStringUtils.randomAlphabetic(8) + "@" + RandomStringUtils.randomAlphabetic(8) + ".fake").toLowerCase());
        List<String> roles = new ArrayList<String>();
        roles.add("appuser");
        user.setRrlnms(roles);

        if (enabled) {
            if (StringUtils.isBlank(password)) {
                user.setPsswrd(RandomStringUtils.randomAlphabetic(8));
            } else {
                user.setPsswrd(password);
            }
        }

        return user;
    }

    protected User createRandomAdminUser(boolean enabled) {
        User user = new User();
        user.setMl(RandomStringUtils.randomAlphabetic(8) + "@" + RandomStringUtils.randomAlphabetic(8) + ".fake");
        List<String> roles = new ArrayList<String>();
        roles.add("appuser");
        roles.add("superadmin");
        roles.add("developer");
        roles.add("contentproducer");
        user.setRrlnms(roles);

        if (enabled) {
            user.setPsswrd(RandomStringUtils.randomAlphabetic(8));
        }

        return user;
    }
}
