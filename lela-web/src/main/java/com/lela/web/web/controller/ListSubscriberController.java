/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */


package com.lela.web.web.controller;

//~--- non-JDK imports --------------------------------------------------------

import com.lela.commons.service.UserService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.dto.SubscribeToEmailList;
import com.lela.domain.dto.UnsubscribeFromEmailList;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * <p>Title: PageController</p>
 * <p>Description: Dispatches pages.</p>
 *
 * @author Bjorn Harvold
 */
@Controller
public class ListSubscriberController {
    private final UserService userService;

    @Autowired
    public ListSubscriberController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/subscribe", method = RequestMethod.POST,
            consumes = "application/json", produces = "application/json")
    @ResponseBody
    public SubscribeToEmailList subscribeToEmailList(@RequestBody SubscribeToEmailList dto) throws Exception {

        // some dirty validation
        EmailValidator validator = EmailValidator.getInstance(false);

        if (validator.isValid(dto.getEmail())) {
            userService.subscribeToEmailList(dto);

            // if we really want to validate the success of this we should be catching the Future
            // instead we're going to assume everything is hunky dorey if the email is ok
            dto.setMessage(WebConstants.SUCCESS);
        } else {
            dto.setMessage(WebConstants.FAILURE);
        }

        return dto;
    }

    @RequestMapping(value = "/unsubscribe", method = RequestMethod.POST,
            consumes = "application/json", produces = "application/json")
    @ResponseBody
    public UnsubscribeFromEmailList unsubscribeFromEmailList(@RequestBody UnsubscribeFromEmailList dto) throws Exception {
        userService.unsubscribeFromEmailList(dto);

        return dto;
    }
}
