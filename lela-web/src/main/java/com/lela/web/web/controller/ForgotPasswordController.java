/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.web.controller;

import com.lela.commons.service.UserService;
import com.lela.commons.web.utils.WebConstants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

/**
 * Created by Bjorn Harvold
 * Date: 8/30/11
 * Time: 2:52 PM
 * Responsibility:
 */
@Controller
@RequestMapping("/forgot/password")
public class ForgotPasswordController {

    private static final String FORGOT_PASSWORD_KEY = "forgot.password";
    private static final String ERROR_EMAIL_UNKNOWN_KEY = "error.email.unknown";
    private final UserService userService;

    private final MessageSource messageSource;

    @Autowired
    public ForgotPasswordController(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showForgotPassword() throws Exception {
        return "forgot.password";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String forgotPassword(@RequestParam(value = "email", required = true) String email,
                                 Model model, Locale locale) throws Exception {
        String view = null;

        if (StringUtils.isNotBlank(email)) {

            Boolean success = userService.resetPassword(email);

            if (success) {
                String message = messageSource.getMessage(FORGOT_PASSWORD_KEY, null, locale);

                model.addAttribute(WebConstants.MESSAGE, message);
                view = "forgot.password";
            } else {
                model.addAttribute(WebConstants.MESSAGE, ERROR_EMAIL_UNKNOWN_KEY);
                view = "forgot.password";
            }
        }

        return view;
    }
}
