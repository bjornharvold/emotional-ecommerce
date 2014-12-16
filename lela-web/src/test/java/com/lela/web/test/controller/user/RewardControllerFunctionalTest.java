/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.test.controller.user;

import com.lela.commons.service.ProfileService;
import com.lela.domain.document.Reward;
import com.lela.domain.document.User;
import com.lela.domain.dto.Principal;
import com.lela.domain.dto.Rewards;
import com.lela.domain.enums.ClaimStatus;
import com.lela.commons.repository.UserRepository;
import com.lela.commons.service.UserService;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.web.test.AbstractFunctionalTest;
import com.lela.commons.web.utils.WebConstants;
import com.lela.web.web.controller.user.RewardController;
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
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class RewardControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(RewardControllerFunctionalTest.class);

    private static final String PRODUCT_NAME = "ProductName";
    private static final Date PURCHASE_DATE = new Date();
    private static final Double SALE_PRICE = 300.25;
    private static final Double REWARD_AMOUNT = 30D;
    private static final Date CLAIM_DATE = new Date();
    private static final ClaimStatus CLAIM_STATUS = ClaimStatus.NOT_CLAIMED;

    @Autowired
    private UserService userService;

    @Autowired
    private RewardController rewardController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileService profileService;

    private User user;

    @Before
    public void setUp() {
        user = createRandomUser(true);
        user = profileService.registerTestUser(user);
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
    public void testShowAndUpdateRewards() {
        HttpSession session = null;
        Model model = null;
        String view = null;

        SpringSecurityHelper.secureChannel(new Principal(user));

        Reward reward = new Reward();
        reward.setPrdctnm(PRODUCT_NAME);
        reward.setPrchsdt(PURCHASE_DATE);
        reward.setSlprc(SALE_PRICE);
        reward.setRwrdamnt(REWARD_AMOUNT);
        reward.setClmdt(CLAIM_DATE);
        reward.setClmstts(CLAIM_STATUS);

        try {
            session = new MockHttpSession();
            view = rewardController.updateRewards(reward, session);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Update rewards method threw unexpected exception: " + e.getMessage());
        }

        assertEquals("Tile view is incorrect", "redirect:/user/dashboard/rewards", view);


        try {
            model = new BindingAwareModelMap();
            session = new MockHttpSession();
            view = rewardController.showRewards(session, model);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        assertEquals("Tile view is incorrect", "user.dashboard.rewards", view);
        assertNotNull("Rewards DTO is null", model.asMap().get(WebConstants.REWARDS));
        Rewards rewardsDto = (Rewards)model.asMap().get(WebConstants.REWARDS);
        List<Reward> rewards = userService.findRewards(user.getCd());
        assertNotNull("User rewards list is null", rewards);
        assertEquals("User rewards list not updated correctly", rewardsDto.getRwrds().size(), 1);
        reward = rewardsDto.getRwrds().get(0);
        assertEquals("Product name not correct", reward.getPrdctnm(), PRODUCT_NAME);
        assertEquals("Purchase name not correct", reward.getPrchsdt(), PURCHASE_DATE);
        assertEquals("Sale price not correct", reward.getSlprc(), SALE_PRICE);
        assertEquals("Reward amount not correct", reward.getRwrdamnt(), REWARD_AMOUNT);
        assertEquals("Claim date not correct", reward.getClmdt(), CLAIM_DATE);
        assertEquals("Claim status not correct", reward.getClmstts(), CLAIM_STATUS);
    }
}
