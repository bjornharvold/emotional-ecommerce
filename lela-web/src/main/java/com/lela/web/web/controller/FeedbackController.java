/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.web.controller;

import com.lela.domain.document.Feedback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * Created by Bjorn Harvold
 * Date: 10/27/11
 * Time: 12:54 PM
 * Responsibility:
 */
@Controller
@RequestMapping(value = "/user/feedback")
@SessionAttributes(types = Feedback.class)
public class FeedbackController {
    private final static Logger log = LoggerFactory.getLogger(FeedbackController.class);
/*
    private final UserService userService;

    @Autowired
    public FeedbackController(UserService userService) {
        this.userService = userService;
    }
*/
    @RequestMapping(method = RequestMethod.GET)
    public String showFeedback(Model model) {
        String view = "feedback";

        /*
        // user is expected to already be logged in at this point as the feedback is appended to the user
        Principal principal = SpringSecurityHelper.getSecurityContextPrincipal();

        if (principal != null && principal.getUser() != null) {
            model.addAttribute(WebConstants.COMPARISON, Comparison.values());
            model.addAttribute(WebConstants.RECOMMEND_TO_FRIEND, RecommendToFriend.values());
            if (principal.getUser().getFdbck() != null) {
                model.addAttribute(WebConstants.FEEDBACK, principal.getUser().getFdbck());
            } else {
                model.addAttribute(WebConstants.FEEDBACK, new Feedback());
            }
        } else {
            log.warn("Could not find a logged in user to submit the feedback for");
            view = "feedback.error";
        }
        */

        return view;
    }

    /*
    @RequestMapping(method = RequestMethod.POST)
    public String submitFeedbackForm(@Valid Feedback feedback, BindingResult errors, Model model) {
        String view = "feedback.success";

        // user is expected to already be logged in at this point as the feedback is appended to the user
        Principal principal = SpringSecurityHelper.getSecurityContextPrincipal();

        if (errors.hasErrors()) {
            model.addAttribute(WebConstants.FEEDBACK, feedback);
            view = "feedback";
        } else {
            if (principal != null && principal.getUser() != null) {
                User user = principal.getUser();
                user.setFdbck(feedback);
                userService.saveUser(user);
            } else {
                log.warn("Could not find a logged in user to submit the feedback for");
                view = "feedback.error";
            }
        }

        return view;
    }
    */
}
