/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.spring.security;

import com.lela.commons.web.utils.WebConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by Bjorn Harvold
 * Date: 12/3/12
 * Time: 9:23 PM
 * Responsibility:
 */
public class LelaSessionClearingLogoutHandler implements LogoutHandler {
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.removeAttribute(WebConstants.USER);
        }
    }
}
