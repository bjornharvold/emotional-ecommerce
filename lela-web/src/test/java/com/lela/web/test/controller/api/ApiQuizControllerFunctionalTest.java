/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.web.test.controller.api;

import com.lela.commons.service.AffiliateService;
import com.lela.commons.service.ApplicationService;
import com.lela.commons.service.ProfileService;
import com.lela.commons.service.QuizService;
import com.lela.commons.service.UserService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.AffiliateAccountApplication;
import com.lela.domain.document.Answer;
import com.lela.domain.document.Application;
import com.lela.domain.document.Question;
import com.lela.domain.document.Quiz;
import com.lela.domain.document.QuizStep;
import com.lela.domain.document.QuizStepEntry;
import com.lela.domain.document.User;
import com.lela.domain.dto.quiz.QuizAnswer;
import com.lela.domain.dto.quiz.QuizAnswers;
import com.lela.domain.dto.quiz.QuizAnswersResponse;
import com.lela.domain.dto.quiz.QuizDto;
import com.lela.domain.enums.ApplicationType;
import com.lela.domain.enums.QuestionType;
import com.lela.domain.enums.QuizStepType;
import com.lela.web.test.AbstractFunctionalTest;
import com.lela.web.web.controller.api.ApiQuizController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Created by Bjorn Harvold
 * Date: 8/3/11
 * Time: 10:05 AM
 * Responsibility:
 */
@SuppressWarnings("unchecked")
public class ApiQuizControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(ApiQuizControllerFunctionalTest.class);
    private static final String QUIZ_URL_NAME = "ApiQuizControllerFunctionalTestQuiz";
    private static final String QUESTION_URL_NAME = "ApiQuizControllerFunctionalTestQuestion";
    private static final String LANGUAGE = "en";
    private static final String QUIZ_STEP_ONE = QUIZ_URL_NAME + "QuizStepOne";
    private static final String ANSWER_NAME = QUESTION_URL_NAME + "AnswerName";
    private static final String AFFILIATE_URL_NAME = "ApiQuizControllerFunctionalTestAffiliate";
    private static final String APPLICATION_URL_NAME = "ApiQuizControllerFunctionalTestApplication";

    @Autowired
    private ApiQuizController apiQuizController;

    @Autowired
    private QuizService quizService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private AffiliateService affiliateService;

    private Quiz quiz = null;
    private Question question = null;
    private AffiliateAccount affiliateAccount;
    private Application application = null;
    private User user = null;

    @Before
    public void setUp() {
        log.info("setting up api quiz controller...");
        SpringSecurityHelper.secureChannel();

        user = createRandomUser(true);
        user = profileService.registerTestUser(user);

        question = quizService.findQuestionByUrlName(QUESTION_URL_NAME);
        if (question == null) {
            question = new Question();
            question.setTp(QuestionType.IMAGE);
            question.setNm(QUESTION_URL_NAME);
            question.setRlnm(QUESTION_URL_NAME);
            List<Answer> answers = new ArrayList<Answer>(2);

            Answer answer1 = new Answer();
            answer1.setNm(ANSWER_NAME);
            answer1.setKy("1");
            Map<String, Integer> mapA = new HashMap<String, Integer>(1);
            mapA.put("A", 5);
            answer1.setMtvtrs(mapA);
            answers.add(answer1);

            Answer answer2 = new Answer();
            answer2.setNm(ANSWER_NAME);
            answer2.setKy("2");
            Map<String, Integer> mapB = new HashMap<String, Integer>(1);
            mapB.put("B", 5);
            answer2.setMtvtrs(mapB);
            answers.add(answer2);

            question.setNswrs(answers);

            question = quizService.saveQuestion(question);
        }

        quiz = quizService.findQuizByUrlName(QUIZ_URL_NAME);
        if (quiz == null) {
            quiz = new Quiz();
            quiz.setDflt(false);
            quiz.setNm(QUIZ_URL_NAME);
            quiz.setSrlnm(QUIZ_URL_NAME);
            quiz.setRlnm(QUIZ_URL_NAME);
            quiz.setPblshd(true);
            quiz.setLng(LANGUAGE);

            List<QuizStep> steps = new ArrayList<QuizStep>(1);
            QuizStep step = new QuizStep();
            step.setTp(QuizStepType.QUESTION);
            step.setNm(QUIZ_STEP_ONE);
            step.setRdr(1);
            step.setRlnm(QUIZ_STEP_ONE);
            step.setSrlnm(QUIZ_STEP_ONE);

            List<QuizStepEntry> entries = new ArrayList<QuizStepEntry>(1);
            QuizStepEntry entry = new QuizStepEntry();
            entry.setRdr(1);
            entry.setRlnm(question.getRlnm());
            entries.add(entry);
            step.setNtrs(entries);
            steps.add(step);
            quiz.setStps(steps);

            quiz = quizService.saveQuiz(quiz);
        }

        application = applicationService.findApplicationByUrlName(APPLICATION_URL_NAME);
        if (application == null) {
            application = new Application();
            application.setTrlnm(QUIZ_URL_NAME);
            application.setNm(APPLICATION_URL_NAME);
            application.setRlnm(APPLICATION_URL_NAME);
            application.setTp(ApplicationType.QUIZ);
            application.setPblshd(true);

            application = applicationService.saveApplication(application);
        }

        affiliateAccount = affiliateService.findAffiliateAccountByUrlName(AFFILIATE_URL_NAME);
        if (affiliateAccount == null) {
            affiliateAccount = new AffiliateAccount();
            affiliateAccount.setCtv(true);
            affiliateAccount.setNm(AFFILIATE_URL_NAME);
            affiliateAccount.setRlnm(AFFILIATE_URL_NAME);

            List<AffiliateAccountApplication> pps = new ArrayList<AffiliateAccountApplication>(1);
            AffiliateAccountApplication app = new AffiliateAccountApplication();
            app.setTp(ApplicationType.QUIZ);
            app.setDt(new Date());
            app.setRlnm(APPLICATION_URL_NAME);
            pps.add(app);
            affiliateAccount.setPps(pps);

            affiliateAccount = affiliateService.saveAffiliateAccount(affiliateAccount);
        }

        SpringSecurityHelper.unsecureChannel();
        log.info("setting up api quiz controller complete");
    }

    @After
    public void tearDown() {
        SpringSecurityHelper.secureChannel();
        if (quiz != null) {
            quizService.removeQuiz(quiz.getRlnm());
        }
        if (question != null) {
            quizService.removeQuestion(question.getRlnm());
        }
        if (application != null) {
            applicationService.removeApplication(application.getRlnm());
        }
        if (affiliateAccount != null) {
            affiliateService.removeAffiliateAccount(affiliateAccount.getRlnm());
        }
        if (user != null) {
            userService.removeUser(user);
        }
        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testApiQuizController() {
        log.info("Testing api quiz controller...");
        MockHttpServletRequest request = null;
        MockHttpServletResponse response = null;
        MockHttpSession session = null;

        try {
            request = new MockHttpServletRequest();
            response = new MockHttpServletResponse();
            session = new MockHttpSession();
            request.setSession(session);

            log.info("First we want to retrieve the quiz");
            QuizDto dto = apiQuizController.retrieveQuiz(APPLICATION_URL_NAME, AFFILIATE_URL_NAME, user.getMl(), null, request, response, session);
            assertNotNull("Quiz is null", dto);
            assertEquals("quiz name is incorrect", QUIZ_URL_NAME, dto.getNm());
            assertEquals("User code is not correct", user.getCd(), dto.getSrcd());

            QuizAnswers quizAnswers = new QuizAnswers();
            quizAnswers.setAffiliateId(AFFILIATE_URL_NAME);
            quizAnswers.setApplicationId(APPLICATION_URL_NAME);
            quizAnswers.setQuizUrlName(dto.getRlnm());
            quizAnswers.setEmail(user.getMl());
            List<QuizAnswer> list = new ArrayList<QuizAnswer>(1);
            QuizAnswer quizAnswer = new QuizAnswer();
            quizAnswer.setQuestionUrlName(question.getRlnm());
            quizAnswer.setAnswerKey(question.getNswrs().get(0).getKy());
            list.add(quizAnswer);
            quizAnswers.setList(list);

            response = new MockHttpServletResponse();
            QuizAnswersResponse qar = apiQuizController.answerQuiz(quizAnswers, request, response, session);
            assertEquals("message is incorrect", WebConstants.SUCCESS, qar.getMessage());
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            fail(ex.getMessage());
        }
        log.info("Testing api quiz controller complete");
    }

}
