/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */


package com.lela.ingest.web.controller;

//~--- non-JDK imports --------------------------------------------------------

import com.lela.commons.web.utils.WebConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpSession;

//~--- classes ----------------------------------------------------------------

/**
 * User: Chris Tallent
 * Date: Feb 10, 2012
 * Time: 5:32:36 PM
 * Responsibility:
 */
@Controller
public class LoginController {
    //~--- methods ------------------------------------------------------------

    /**
     * Displays the login box on the index page.
     *
     * @param redirectUrl redirectUrl
     * @return Tile def
     * @throws Exception ex
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@RequestParam(value = WebConstants.REDIRECT, required = false) String redirectUrl) throws Exception {
        return "login";
    }
}
