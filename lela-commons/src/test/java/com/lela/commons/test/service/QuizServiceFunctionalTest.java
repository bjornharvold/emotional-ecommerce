/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.test.service;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.lela.commons.service.AffiliateService;
import com.lela.commons.service.ApplicationService;
import com.lela.commons.service.QuizService;
import com.lela.commons.service.StaticContentService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.test.AbstractFunctionalTest;
import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.Application;
import com.lela.domain.document.Question;
import com.lela.domain.document.Quiz;
import com.lela.domain.document.QuizStep;
import com.lela.domain.document.QuizStepEntry;
import com.lela.domain.document.StaticContent;
import com.lela.domain.enums.ApplicationType;
import com.lela.domain.enums.QuestionType;
import com.lela.domain.enums.QuizStepType;

/**
 * Created by Bjorn Harvold
 * Date: 6/4/12
 * Time: 7:53 PM
 * Responsibility:
 */
public class QuizServiceFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(QuizServiceFunctionalTest.class);
    private static final String QUESTION_NAME = QuizServiceFunctionalTest.class.getSimpleName();

    @Autowired
    private QuizService quizService;

    @Autowired
    private AffiliateService affiliateService;
    
    @Autowired
    private ApplicationService applicationService;

    
    @Autowired
    private StaticContentService staticContentService;
    
    
    private AffiliateAccount affiliateAccount = null;
    private Application application = null;
    private StaticContent staticContent = null;
    private Quiz quiz;
    
    private Question slider;

    private static final String AFFILIATE_URL_NAME = "http://google.com";
    
    private static final String APPLICATION_URL_NAME = "APPLICATION_URL_NAME";

    
    private static final String QUIZ_NAME = "QUIZ_NAME";
    private static final String QUIZ_URL_NAME = "QUIZ_URL_NAME";
    private static final String QUIZ_STEP_ENTRY_URL = "QUIZ_STEP_ENTRY_URL";
    
    private static final String LANGUAGE = "en";
    private static final String QUIZ_STEP_ONE = QUIZ_URL_NAME + "-QuizStepOne";
    
    private static final String THIS_IS_A_BIG_TEST = "This is a big test. ${o.nm}";
    @Before
    public void setUp() {
        SpringSecurityHelper.secureChannel();

        log.info("Setting up test...");
        slider = new Question();
        slider.setNm(QUESTION_NAME);
        slider.setRlnm(QUESTION_NAME);
        slider.setTp(QuestionType.SLIDER);
        slider.setGrp(2);
        slider = quizService.saveQuestion(slider);
        log.info("Setting up test complete");
        
        quiz = new Quiz();
        quiz.setDflt(false);
        quiz.setLng(LANGUAGE);
        quiz.setNm(QUIZ_NAME);
        quiz.setRlnm(QUIZ_URL_NAME);
        quiz.setSrlnm(QUIZ_URL_NAME);
        quiz.setPblshd(true);      
        
        List<QuizStep> steps = new ArrayList<QuizStep>(1);
        QuizStep step = new QuizStep();
        step.setTp(QuizStepType.SPLASH);
        step.setNm(QUIZ_STEP_ONE);
        step.setRdr(1);
        step.setRlnm(QUIZ_STEP_ONE);
        step.setSrlnm(QUIZ_STEP_ONE);

        List<QuizStepEntry> entries = new ArrayList<QuizStepEntry>(1);
        QuizStepEntry entry = new QuizStepEntry();
        entry.setRdr(1);
        entry.setRlnm(QUIZ_STEP_ENTRY_URL);
        entries.add(entry);
        step.setNtrs(entries);
        steps.add(step);
        quiz.setStps(steps);
        
        quiz = quizService.saveQuiz(quiz);
        
        application = new Application();
        application.setTrlnm(QUIZ_URL_NAME);
        application.setNm(APPLICATION_URL_NAME);
        application.setRlnm(APPLICATION_URL_NAME);
        application.setTp(ApplicationType.QUIZ);
        application.setPblshd(true);

        application = applicationService.saveApplication(application);
     
        
        staticContent = new StaticContent();
        staticContent.setNm(QuizServiceFunctionalTest.class.getSimpleName()+ "-name");
        staticContent.setRlnm(QUIZ_STEP_ENTRY_URL);
        staticContent.setSrlnm(QuizServiceFunctionalTest.class.getSimpleName() + "-Srlm");
        staticContent.setBdy(THIS_IS_A_BIG_TEST);
        staticContent.setVlctytmplt(true);
        
        staticContent = staticContentService.saveStaticContent(staticContent);
        
        
        affiliateAccount = affiliateService.findActiveAffiliateAccountByUrlName(AFFILIATE_URL_NAME);
        if (affiliateAccount == null){
        	affiliateAccount = new AffiliateAccount();
        	affiliateAccount.setCtv(true);
        	affiliateAccount.setRlnm(AFFILIATE_URL_NAME);
        	affiliateService.saveAffiliateAccount(affiliateAccount);
        }
    }

    @After
    public void tearDown() {
        SpringSecurityHelper.secureChannel();
        if (slider != null) {
            quizService.removeQuestion(slider.getRlnm());
        }
        cleanUp();
        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testFindQuestionByUrlName() {

        log.info("Retrieving question by url name...");
        Question question = quizService.findQuestionByUrlName(slider.getRlnm());
        assertNotNull("Question is missing", question);
        assertNotNull("Question is missing an id", question.getId());
        log.info("Retrieving question by url name complete");
    }
    
    @Test
    public void testQuizByUrlNameForStaticContent(){
    	try {
	    	Quiz testQuiz = quizService.findQuizByUrlName(QUIZ_URL_NAME, AFFILIATE_URL_NAME, APPLICATION_URL_NAME);
	    	Assert.notNull(testQuiz);
	    	Assert.notNull(testQuiz.getStps());
	    	Assert.notNull(testQuiz.getStps().get(0));
	    	Assert.notNull(testQuiz.getStps().get(0).getNtrs());
	    	Assert.notNull(testQuiz.getStps().get(0).getNtrs().get(0));
	    	Assert.notNull(testQuiz.getStps().get(0).getNtrs().get(0).getStaticContent());
    	} catch (Exception e){
    		e.printStackTrace();
    	} finally {
    		cleanUp();
    	}
    }

    
    private void cleanUp(){
		if (application != null){
			applicationService.removeApplication(application.getRlnm());
		}
		if (affiliateAccount != null){
			affiliateService.removeAffiliateAccount(affiliateAccount.getRlnm());
		}
		if (staticContent != null){
			staticContentService.removeStaticContent(staticContent.getRlnm());
		}
    }
}
