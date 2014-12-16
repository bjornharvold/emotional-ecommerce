
/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.web.utils;

import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.Principal;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * User: Bjorn Harvold
 * Date: Sep 17, 2010
 * Time: 3:25:43 PM
 * Responsibility: This is our custom locale resolver for TXLA. Once the user logs 
 * in we will have access to UserProfileData and therefore the locale. Prior to logging in we will default to en_US.
 * The toggle on the webpage can change the locale at runtime. 
 */
public class CustomLocaleResolver extends CookieLocaleResolver implements LocaleResolver {
    private final static Logger log = LoggerFactory.getLogger(CustomLocaleResolver.class);
    private final UserService userService;

    public CustomLocaleResolver(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        Locale result = null;
        
        Principal principal = SpringSecurityHelper.getSecurityContextPrincipal();

        // this is the prioritized locale
        if (principal != null && principal.getUser() != null) {
            UserSupplement us = userService.findUserSupplement(principal.getUser().getCd());

            if (us.getLcl() != null) {
                result = us.getLcl();
            }
        } else {
            result = super.resolveLocale(request);
        }
        
        return result;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        super.setLocale(request, response, locale);
        
        // update the locale in the db
        Principal principal = SpringSecurityHelper.getSecurityContextPrincipal();
        
        if (principal != null) {
            // grab a fresh copy
            User dbUser = principal.getUser();
            UserSupplement us = userService.findUserSupplement(dbUser.getCd());
            // set new locale
            us.setLcl(locale);
            // update
            userService.saveUserSupplement(us);
        }
    }
}
