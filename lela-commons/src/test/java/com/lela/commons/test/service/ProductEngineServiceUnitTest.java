package com.lela.commons.test.service;

import com.lela.commons.event.EventHelper;
import com.lela.commons.event.QuizAnswersEvent;
import com.lela.commons.event.RegistrationEvent;
import com.lela.commons.service.ComputedCategoryService;
import com.lela.commons.service.UserService;
import com.lela.commons.service.impl.ProductEngineServiceImpl;
import com.lela.domain.dto.quiz.QuizAnswers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 11/9/12
 * Time: 8:09 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(EventHelper.class)
public class ProductEngineServiceUnitTest {

    @Mock
    ComputedCategoryService computedCategoryService;

    @Mock
    UserService userService;

    @InjectMocks
    ProductEngineServiceImpl productEngineServiceImpl;

    String userCode = "user-code";

    @Test
    public void testAnswerQuestions()
    {
        QuizAnswers answers = new QuizAnswers();

        ArgumentCaptor<Object> trackEvent = (ArgumentCaptor)ArgumentCaptor.forClass(Object.class);
        mockStatic(EventHelper.class);

        productEngineServiceImpl.answerQuestions(userCode, answers);

        verifyStatic(times(1));
        EventHelper.post(trackEvent.capture());

        assertTrue("QuizAnswersEvent not posted", trackEvent.getAllValues().get(0) instanceof QuizAnswersEvent);
    }

}
