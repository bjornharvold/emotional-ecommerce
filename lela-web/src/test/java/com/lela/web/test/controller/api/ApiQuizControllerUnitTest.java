package com.lela.web.test.controller.api;

import com.google.common.collect.Lists;
import com.lela.commons.event.EventHelper;
import com.lela.commons.event.QuizFinishEvent;
import com.lela.commons.event.QuizStartEvent;
import com.lela.commons.event.QuizStepFinishEvent;
import com.lela.commons.service.*;
import com.lela.commons.utilities.UserSessionTrackingHelper;
import com.lela.domain.document.*;
import com.lela.domain.dto.quiz.QuizAnswer;
import com.lela.domain.dto.quiz.QuizAnswers;
import com.lela.domain.dto.quiz.QuizDto;
import com.lela.domain.enums.ApplicationType;
import com.lela.web.web.controller.api.ApiQuizController;
import org.dozer.Mapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import java.util.HashSet;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 11/9/12
 * Time: 8:26 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(EventHelper.class)
public class ApiQuizControllerUnitTest {

    @Mock
    AffiliateService affiliateService;

    @Mock
    ApplicationService applicationService;

    @Mock
    QuizService quizService;

    @Mock
    Validator validator;

    @Mock
    Mapper mapper;

    @Mock
    ProductEngineService productEngineService;

    @Mock
    UserTrackerService userTrackerService;

    @Mock
    UserService userService;

    @Mock
    UserSessionTrackingHelper userSessionTrackingHelper;

    @InjectMocks
    ApiQuizController apiQuizController;

    String userCode = "user-code";
    String applicationUrlName = "applicationUrlName";
    String affiliateUrlName = "affiliateUrlName";
    String testUrlName = "testUrlName";
    String email = "aaa@bbb.com";

    @Before
    public void setup() {
        apiQuizController.setUserSessionTrackingHelper(userSessionTrackingHelper);
    }

    @Test
    public void testRetrieveQuiz() throws Exception
    {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpSession session = new MockHttpSession();

        mockCommonMethodCalls(applicationUrlName, affiliateUrlName, testUrlName, email);
        Quiz quiz = new Quiz();
        QuizDto quizDto = new QuizDto();


        when(quizService.findPublishedQuizByUrlName(testUrlName)).thenReturn(quiz);
        when(mapper.map(quiz, QuizDto.class)).thenReturn(quizDto);


        ArgumentCaptor<Object> trackEvent = (ArgumentCaptor)ArgumentCaptor.forClass(Object.class);
        mockStatic(EventHelper.class);

        apiQuizController.retrieveQuiz(applicationUrlName,
                affiliateUrlName,
                email,
                null,
                request,
                response,
                session);

        verifyStatic(times(1));
        EventHelper.post(trackEvent.capture());

        assertTrue("QuizStartEvent not posted", trackEvent.getAllValues().get(0) instanceof QuizStartEvent);

    }

    @Test
    public void testAnswerQuiz() throws Exception
    {
        QuizAnswers quizAnswers = new QuizAnswers();
        quizAnswers.setAffiliateId(affiliateUrlName);
        quizAnswers.setApplicationId(applicationUrlName);
        quizAnswers.setEmail(email);

        QuizAnswer quizAnswer = new QuizAnswer();
        quizAnswers.setList(Lists.newArrayList(quizAnswer));

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpSession session = new MockHttpSession();

        mockCommonMethodCalls(applicationUrlName, affiliateUrlName, testUrlName, email);
        when(validator.validate(quizAnswers)).thenReturn(new HashSet<ConstraintViolation<QuizAnswers>>());

        ArgumentCaptor<Object> trackEvent = (ArgumentCaptor)ArgumentCaptor.forClass(Object.class);
        mockStatic(EventHelper.class);

        apiQuizController.answerQuiz(quizAnswers, request, response, session);

        verifyStatic(times(1));
        EventHelper.post(trackEvent.capture());

        assertTrue("QuizFinish not posted", trackEvent.getAllValues().get(0) instanceof QuizFinishEvent);


    }

    @Test
    public void testTrackAnswer() throws Exception
    {
        String affiliateUrlName = "";
        String applicationUrlName = "";
        String campaignId = "";
        String stepUrlName = "";

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpSession session = new MockHttpSession();

        mockCommonMethodCalls(applicationUrlName, affiliateUrlName, testUrlName, email);

        ArgumentCaptor<Object> trackEvent = (ArgumentCaptor)ArgumentCaptor.forClass(Object.class);
        mockStatic(EventHelper.class);

        apiQuizController.trackAnswer(userCode, affiliateUrlName, applicationUrlName, campaignId, stepUrlName, request, response, session);

        verifyStatic(times(1));
        EventHelper.post(trackEvent.capture());

        assertTrue("QuizStepFinishEvent not posted", trackEvent.getAllValues().get(0) instanceof QuizStepFinishEvent);
    }

    private void mockCommonMethodCalls(String applicationUrlName, String affiliateUrlName, String testUrlName, String email) {
        AffiliateAccount affiliateAccount = new AffiliateAccount();
        AffiliateAccountApplication affiliateAccountApplication = new AffiliateAccountApplication();
        affiliateAccountApplication.setRlnm(applicationUrlName);
        Application application = new Application();
        application.setTp(ApplicationType.QUIZ);
        application.setPblshd(Boolean.TRUE);
        application.setTrlnm(testUrlName);
        affiliateAccount.setPps(Lists.newArrayList(affiliateAccountApplication));
        User user = new User();
        user.setCd(userCode);

        when(affiliateService.findAffiliateAccountByUrlName(affiliateUrlName)).thenReturn(affiliateAccount);
        when(applicationService.findApplicationByUrlName(applicationUrlName)).thenReturn(application);
        when(userService.findUserByEmail(email)).thenReturn(user);
    }

}
