package com.lela.data.web;

import com.lela.commons.service.UserService;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import java.util.Locale;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 6/19/12
 * Time: 10:48 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration( locations={"/META-INF/spring-test/applicationContext-test.xml"
//        ,"/META-INF/spring/webmvc-component-scan.xml"
//        ,"/META-INF/spring/webmvc-config.xml"
//})
@ContextConfiguration(locations = {"/META-INF/spring-test/applicationContext-test.xml", "/META-INF/spring/test.xml",
        "/META-INF/spring/webmvc-component-scan.xml",
        "/META-INF/spring/webmvc-config.xml",
        "/META-INF/spring/webmvc-security.xml"
})

public abstract class AbstractFunctionalTest extends CommonControllerTest{
    @Autowired
    protected UserService userService;

    protected User createRandomUser(boolean enabled) {
        return createRandomUser(enabled, null);
    }

    protected UserSupplement createRandomUserSupplement(User user) {
        UserSupplement us = new UserSupplement(user.getCd());
        us.setMl(user.getMl().toLowerCase());
        us.setLcl(Locale.US);
        us.setFnm("First name");
        us.setLnm("Last name");

        return us;
    }

    protected User createRandomUser(boolean enabled, String password) {
        User user = new User();
        user.setMl(RandomStringUtils.randomAlphabetic(8) + "@" + RandomStringUtils.randomAlphabetic(8) + ".fake");
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
