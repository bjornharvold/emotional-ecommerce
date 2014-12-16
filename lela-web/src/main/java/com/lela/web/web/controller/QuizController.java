/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.web.controller;

//~--- non-JDK imports --------------------------------------------------------

import com.lela.commons.service.TaskService;
import com.lela.commons.service.UserService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.Motivator;
import com.lela.domain.document.Task;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.quiz.ProfileStatusResponse;
import com.lela.domain.enums.MotivatorSource;
import com.lela.domain.enums.ProfileStatus;
import com.lela.domain.enums.TaskType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 7/19/11
 * Time: 3:01 PM
 * Responsibility:
 */
@Controller
public class QuizController extends AbstractController {
    private final static Logger log = LoggerFactory.getLogger(QuizController.class);

    private final UserService userService;
    private final TaskService taskService;

    @Autowired
    public QuizController(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    /**
     * Shows the embedable quiz
     *
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/quiz", method = RequestMethod.GET)
    public String showQuiz(Model model, Device device, HttpSession session) throws Exception {

        User user = this.retrieveUserFromPrincipalOrSession(session);
        UserSupplement us = userService.findUserSupplement(user.getCd());

        model.addAttribute(WebConstants.QUIZ_TYPE, "full");

        if (us != null) {
            Motivator motivator = us.getMotivator();

            // if motivator != null && motivator type is QUIZ || (motivator is FACEBOOK and you have taken the styles question)
            //    redirect to /categories
            // if motivator != null && motivators are Facebook and styles questions not answer AND B > 1
            //    view = quiz.partial
            // if there is a running FB task then
            //    view = quiz.waiting
            // (Else scenario) if  motivator == null OR (motivator != null && motivators are Facebook and B <= 1)
            //    view = quiz (full)
            if (motivator != null) {
                if (MotivatorSource.QUIZ.equals(motivator.getTp()) || ((MotivatorSource.FACEBOOK.equals(motivator.getTp()) && motivator.hasStyleMotivators()))) {
                    return "redirect:/categories";
                } else if (MotivatorSource.FACEBOOK.equals(motivator.getTp()) && !motivator.hasStyleMotivators()) {
                    model.addAttribute(WebConstants.QUIZ_TYPE, "partial");
                }
            }

            // Determine if there is a running Facebook task
            List<Task> tasks = taskService.findTasksByRecipient(user.getCd());
            if (tasks != null) {
                for (Task task : tasks) {
                    if (TaskType.FACEBOOK_DATA_AGGREGATION.equals(task.getTp())) {
                        return returnMobileViewIfNecessary(model, device, session, "quiz.waiting");
                    }
                }
            }
        }

        return returnMobileViewIfNecessary(model, device, session, "quiz");
    }

    @RequestMapping(value = "/quiz/profilestatus", method = RequestMethod.GET)
    @ResponseBody
    public ProfileStatusResponse getProfileStatus(HttpSession session) {
        ProfileStatusResponse result = new ProfileStatusResponse();
        result.setProfileStatus(ProfileStatus.MISSING);

        User user = this.retrieveUserFromPrincipalOrSession(session);
        UserSupplement us = userService.findUserSupplement(user.getCd());

        if (us != null) {
            result.setProfileStatus(us.getProfileStatus());
        }

        return result;
    }

    /**
     * Shows the animation before quiz
     *
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/quiz/profile", method = RequestMethod.GET)
    public String createProfile(Model model, Device device, HttpSession session) throws Exception {
        return returnMobileViewIfNecessary(model, device, session, "quiz.waiting");
    }


}
