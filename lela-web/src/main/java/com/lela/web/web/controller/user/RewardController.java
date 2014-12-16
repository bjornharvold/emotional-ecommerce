/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.web.controller.user;

import com.lela.commons.service.UserService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.Reward;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.Principal;
import com.lela.domain.dto.Rewards;
import com.lela.web.web.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
public class RewardController extends AbstractController {

    private final UserService userService;

    @Autowired
    public RewardController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Allow user to obtain rewards information
     *
     * @param model model
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/user/dashboard/rewards", method = RequestMethod.GET)
    public String showRewards(HttpSession session, Model model) throws Exception {
        User user = retrieveUserFromPrincipalOrSession(session);

        if (user != null) {
            UserSupplement us = userService.findUserSupplement(user.getCd());
            Rewards rewardsDto = new Rewards(us);

            model.addAttribute(WebConstants.REWARDS, rewardsDto);
            model.addAttribute(WebConstants.ACCOUNT, "account-rewards");
        }
        return "user.dashboard.rewards";
    }

    /**
     * Allow user to add reward entry
     *
     * @param session session
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/user/dashboard/rewards/update", method = RequestMethod.POST)
    public String updateRewards(Reward reward, HttpSession session) throws Exception {

        User user = retrieveUserFromPrincipalOrSession(session);

        if (user != null) {
            UserSupplement us = userService.findUserSupplement(user.getCd());
            us.addReward(reward);
            userService.saveUserSupplement(us);
        }

        return "redirect:/user/dashboard/rewards";
    }
}
