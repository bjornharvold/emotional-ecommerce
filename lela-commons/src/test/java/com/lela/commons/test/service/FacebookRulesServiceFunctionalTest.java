package com.lela.commons.test.service;

import com.lela.commons.service.FacebookUserService;
import com.lela.commons.service.UserService;
import com.lela.commons.test.AbstractFunctionalTest;
import com.lela.domain.document.User;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: Cruiser1
 * Date: 2/13/12
 * Time: 8:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class FacebookRulesServiceFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(FacebookRulesServiceFunctionalTest.class);

    private static final String USER_EMAIL = "testuser1kijmkhj@yopmail.com";
    private static final String IP_ADDRESS = "74.139.104.16";

    @Autowired
    private FacebookUserService facebookUserService;

    @Autowired
    private UserService userService;

//    @Test
//    public void testFacebookSnapshot() {
//        User user = userService.findUserByEmail("chris@tallented.net");
//        FacebookSnapshot snapshot = facebookUserService.generateSnapshot(user, IP_ADDRESS);
//
//        System.out.println(snapshot.getGender());
//    }
    
    @Test
    public void testFacebookRules() {
        User user = userService.findUserByEmail(USER_EMAIL);
        //facebookRulesService.processMotivators(user);
        assertTrue("Error processing motivators", true);
    }
}
