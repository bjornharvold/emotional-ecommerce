/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.test;

import com.icegreen.greenmail.util.GreenMail;
import com.lela.commons.service.UserService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.domain.document.UserSupplement;
import com.lela.util.test.AbstractTest;
import com.lela.util.utilities.GreenMailHelper;
import com.lela.domain.document.Category;
import com.lela.domain.document.Color;
import com.lela.domain.document.Item;
import com.lela.domain.document.Owner;
import com.lela.domain.document.User;
import org.apache.commons.lang.RandomStringUtils;
import org.bson.types.ObjectId;
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
@ContextConfiguration(locations = {"/META-INF/spring/applicationContext.xml","/META-INF/spring/test.xml"})
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
        User user = new User();
        user.setMl(RandomStringUtils.randomAlphabetic(8) + "@" + RandomStringUtils.randomAlphabetic(8) + ".fake");
        List<String> roles = new ArrayList<String>();
        roles.add("appuser");
        user.setRrlnms(roles);

        if (enabled) {
            user.setPsswrd(RandomStringUtils.randomAlphabetic(8));
        }

        return user;
    }

    protected UserSupplement createRandomUserSupplement(User user) {
        UserSupplement us = new UserSupplement(user.getCd());
        us.setMl(user.getMl());
        us.setLcl(Locale.US);
        us.setFnm("First name");
        us.setLnm("Last name");

        return us;
    }
    
    protected User createSpecificUser(boolean enabled) {
        User user = new User();
        user.setMl("popeye@gmail.fake");
        List<String> roles = new ArrayList<String>();
        roles.add("appuser");
        user.setRrlnms(roles);

        if (enabled) {
            user.setPsswrd("aPassword");
        }

        return user;
    }

    protected void removeUser(User user) {
        SpringSecurityHelper.secureChannel();
        userService.removeUser(user);
        SpringSecurityHelper.unsecureChannel();
    }

    protected Item createRandomItem(String categoryUrlName) {
        Item result = new Item();
        String random = RandomStringUtils.randomNumeric(4);
        result.setId(ObjectId.get());
        result.setPc(result.getId().toString());
        result.setLlpc(result.getId().toString());
        result.setRlnm("triumph-" + random);
        result.setNm("Triumph-" + random);

        Category cat = new Category();
        cat.setRlnm(categoryUrlName);
        result.setCtgry(cat);

        Owner owner = new Owner();
        owner.setRlnm("maclaren-" + random);
        owner.setNm("Maclaren-" + random);
        result.setWnr(owner);

        List<Color> clrs = new ArrayList<Color>();
        Color red = new Color();
        red.setNm("red-" + random);
        clrs.add(red);
        Color blue = new Color();
        blue.setNm("blue-" + random);
        clrs.add(blue);
        result.setClrs(clrs);

        return result;
    }
}
