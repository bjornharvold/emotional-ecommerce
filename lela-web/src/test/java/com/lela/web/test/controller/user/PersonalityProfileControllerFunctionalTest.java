/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.test.controller.user;

import com.lela.commons.service.ProfileService;
import com.lela.domain.document.Motivator;
import com.lela.domain.document.User;
import com.lela.domain.dto.Principal;
import com.lela.commons.repository.UserRepository;
import com.lela.commons.service.UserService;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.domain.enums.MotivatorSource;
import com.lela.web.test.AbstractFunctionalTest;
import com.lela.commons.web.utils.WebConstants;
import com.lela.web.web.controller.user.PersonalityProfileController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: Martin Gamboa
 * Date: 10/11/11
 * Time: 3:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class PersonalityProfileControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(PersonalityProfileControllerFunctionalTest.class);

    private static final String MOTIVATOR_B = "B79";
    private static final String MOTIVATOR_D = "D79";
    private static final String MOTIVATOR_E = "E79";
    private static final String MOTIVATOR_F = "F79";

    @Autowired
    private PersonalityProfileController personalityProfileController;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileService profileService;

    private User user;

    @Before
    public void setUp() {
        SpringSecurityHelper.secureChannel();
        user = createRandomUser(true);
        user = profileService.registerTestUser(user);

        Map<String, Integer> userMotivators = new ConcurrentHashMap<String, Integer>();
        userMotivators.put("D", 9);
        userMotivators.put("E", 9);
        userMotivators.put("F", 9);
        userMotivators.put("G", 1);
        userMotivators.put("A", 9);
        userMotivators.put("B", 9);
        userMotivators.put("C", 5);

        Motivator motivator = userService.saveMotivator(user.getCd(), new Motivator(MotivatorSource.QUIZ, userMotivators));
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
    public void testPersonalityProfileGet() {
        log.info("Testing send and accept relationship on social network controller");

        SpringSecurityHelper.secureChannel(new Principal(user));

        Model model = new BindingAwareModelMap();
        HttpSession session = new MockHttpSession();
        String view = personalityProfileController.showPersonalityProfile(session, model);
        assertEquals("Tile view is incorrect", "user.dashboard.personality", view);
        List<String> motivatorLocalizedKeys = (List<String>)model.asMap().get(WebConstants.PROFILE_SUMMARY);
        assertNotNull("motivatorLocalizedKeys is null", motivatorLocalizedKeys);
        assertTrue("Motivator B does not exists", motivatorLocalizedKeys.contains(MOTIVATOR_B));
        assertTrue("Motivator D does not exists", motivatorLocalizedKeys.contains(MOTIVATOR_D));
        assertTrue("Motivator E does not exists", motivatorLocalizedKeys.contains(MOTIVATOR_E));
        assertTrue("Motivator F does not exists", motivatorLocalizedKeys.contains(MOTIVATOR_F));
    }
}
