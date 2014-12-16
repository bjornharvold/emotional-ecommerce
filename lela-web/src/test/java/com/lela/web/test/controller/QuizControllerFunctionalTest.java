/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.test.controller;

import com.lela.commons.service.QuizService;
import com.lela.commons.spring.mobile.MockDevice;
import com.lela.commons.spring.mobile.WurflDevice;
import com.lela.domain.document.Motivator;
import com.lela.domain.document.Question;
import com.lela.domain.document.User;
import com.lela.domain.document.UserAnswer;
import com.lela.domain.dto.quiz.QuizAnswer;
import com.lela.domain.dto.quiz.QuizAnswers;
import com.lela.domain.dto.SkipQuestion;
import com.lela.domain.enums.MotivatorSource;
import com.lela.domain.enums.QuestionType;
import com.lela.util.test.integration.oauth.ServerRunning;
import com.lela.web.test.AbstractFunctionalTest;
import com.lela.commons.web.utils.WebConstants;
import com.lela.web.web.controller.QuizController;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Bjorn Harvold
 * Date: 8/3/11
 * Time: 10:05 AM
 * Responsibility:
 */
@SuppressWarnings("unchecked")
public class QuizControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(QuizControllerFunctionalTest.class);

    @Autowired
    private QuizController quizController;

    @Test
    public void testQuizController() {
        log.info("Testing quiz controller...");

        User user = new User();
        Model model = new BindingAwareModelMap();
        HttpSession session = new MockHttpSession();
        session.setAttribute(WebConstants.USER, user);
        MockDevice mockDevice = new MockDevice(MockDevice.DEVICE_TYPE.NORMAL);
        Device device = new WurflDevice(mockDevice);

        try {
            log.info("Testing the quiz controller with a normal device...");
            String view = quizController.showQuiz(model, device, session);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "quiz", view);
            log.info("Testing the quiz controller with a normal device successful");

            log.info("Testing the quiz controller with a mobile device...");
            mockDevice = new MockDevice(MockDevice.DEVICE_TYPE.MOBILE);
            device = new WurflDevice(mockDevice);
            view = quizController.showQuiz(model, device, session);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "quiz.mobile", view);
            log.info("Testing the quiz controller with a mobile device successful");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing quiz controller complete");
    }
}
