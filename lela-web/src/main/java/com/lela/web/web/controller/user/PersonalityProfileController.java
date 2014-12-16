/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.web.controller.user;

import com.lela.commons.service.CategoryService;
import com.lela.commons.service.UserService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.Principal;
import com.lela.web.web.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class PersonalityProfileController extends AbstractController {

    /**
     * Field description
     */
    private final UserService userService;

    private final CategoryService categoryService;

    @Autowired
    public PersonalityProfileController(UserService userService,
                                        CategoryService categoryService) {
        this.userService = userService;
        this.categoryService = categoryService;
    }

    /**
     * Method description
     *
     * @return Return value
     */
    @RequestMapping(value = "/user/dashboard/personality", method = RequestMethod.GET)
    public String showPersonalityProfile(HttpSession session, Model model) {

        User user = retrieveUserFromPrincipalOrSession(session);
        UserSupplement us = userService.findUserSupplement(user.getCd());
        model.addAttribute(WebConstants.GENDER, us.getGndr());

        // add personal profile data
        List<String> motivatorLocalizedKeys = userService.motivatorLocalizedKeys(user);
        model.addAttribute(WebConstants.PROFILE_SUMMARY, motivatorLocalizedKeys);

        model.addAttribute(WebConstants.CATEGORIES, categoryService.findCategories());

        model.addAttribute(WebConstants.ACCOUNT, "account-personality");

        return "user.dashboard.personality";
    }
}
