/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.web.controller.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.UnconfirmedAuthorizationCodeClientToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Bjorn Harvold
 * Date: 6/23/11
 * Time: 3:47 PM
 * Responsibility:
 */
@Controller
public class AccessConfirmationController {
    private static final Logger log = LoggerFactory.getLogger(AccessConfirmationController.class);
    private final ClientDetailsService clientDetailsService;

    @Autowired
    public AccessConfirmationController(ClientDetailsService clientDetailsService) {
        this.clientDetailsService = clientDetailsService;
    }

    @RequestMapping("/oauth/confirm_access")
    public String getAccessConfirmation(UnconfirmedAuthorizationCodeClientToken token, Model model) throws Exception {
        ClientDetails client = clientDetailsService.loadClientByClientId(token.getClientId());
        model.addAttribute("auth_request", token);
        model.addAttribute("client", client);
        return "oauth2.access.confirmation";
    }
}
