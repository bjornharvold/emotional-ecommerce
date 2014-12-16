/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */


package com.lela.web.web.controller;

//~--- non-JDK imports --------------------------------------------------------

import com.lela.commons.service.UserService;
import com.lela.domain.document.User;
import com.lela.domain.dto.UserAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * <p>Title: ApiController</p>
 * <p>Description: Entry point for external apps</p>
 *
 * @author Bjorn Harvold
 */
@Controller
public class UserAttributeController extends AbstractController {

    private final UserService userService;

    @Autowired
    public UserAttributeController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Shows the quiz page iframe content
     *
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/attrs", method = RequestMethod.GET)
    public String saveUserAttributes(UserAttributes userAttributes, HttpSession session) throws Exception {
        User user = retrieveUserFromPrincipalOrSession(session);

        userService.processUserAttributes(user.getCd(), userAttributes);
        
        return "attributes.quiz";
    }

}
