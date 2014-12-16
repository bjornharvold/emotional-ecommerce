/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.data.web.controller.administration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.lela.commons.service.QuizService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.data.web.AbstractFunctionalTest;
import com.lela.data.web.controller.administration.quiz.QuestionManagerController;
import com.lela.domain.document.Question;
import com.lela.domain.dto.quiz.AnswerEntry;
import com.lela.domain.enums.QuestionType;

/**
 * Created by Bjorn Harvold
 * Date: 7/5/12
 * Time: 2:42 PM
 * Responsibility:
 */
public class QuestionManagerControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(QuestionManagerControllerFunctionalTest.class);

    @Autowired
    private QuestionManagerController questionManagerController;

    @Autowired
    private QuizService quizService;

    @Test
    public void testQuestionManagerController() {
        log.info("Testing questionManagerController...");
        String view = null;
        Model model = null;
        BindingResult errors = null;
        RedirectAttributes redirectAttributes;
        Locale locale = Locale.US;
        Question question = null;
        AnswerEntry answer = null;

        log.info("First we need to secure the context because these are admin-only methods");
        SpringSecurityHelper.secureChannel();
        try {
            log.info("Listing all existing questions...");
            model = new BindingAwareModelMap();
            view = questionManagerController.showQuestions(0, model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.question.list", view);
            log.info("Listing all existing questions complete. We don't really care if this methods contains a list or not. We will be creating a question shortly.");

            log.info("Go to the question form page and get ready to create a question...");
            model = new BindingAwareModelMap();
            view = questionManagerController.showQuestionForm(null, model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.question.form", view);
            assertNotNull("question object is null", model.asMap().get(WebConstants.QUESTION));
            question = (Question) model.asMap().get(WebConstants.QUESTION);
            log.info("Go to the question form page and get ready to create a question complete");

            log.info("Fill out question form object and submit form...");
            question.setTp(QuestionType.SLIDER);
            question.setNm(QuestionManagerControllerFunctionalTest.class.getSimpleName());
            question.setRlnm(QuestionManagerControllerFunctionalTest.class.getSimpleName());

            errors = new BindException(question, WebConstants.QUESTION);
            redirectAttributes = new RedirectAttributesModelMap();
            model = new BindingAwareModelMap();
            view = questionManagerController.submitQuestionForm(question, errors, redirectAttributes, locale, model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "redirect:/administration/question/" + question.getRlnm(), view);
            assertTrue("Redirect attributes is empty", redirectAttributes.getFlashAttributes().containsKey(WebConstants.MESSAGE));
            log.info("Fill out question form object and submit form complete");

            log.info("Verify that the question exists");
            log.info("Show the question page...");
            model = new BindingAwareModelMap();
            view = questionManagerController.showQuestion(question.getRlnm(), model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.question", view);
            assertNotNull("question object is null", model.asMap().get(WebConstants.QUESTION));
            question = (Question) model.asMap().get(WebConstants.QUESTION);
            log.info("Show the question page complete");
            
            log.info("Now that we have a persisted question, time to add an answer to it...");
            log.info("Show question answer form page");
            model = new BindingAwareModelMap();
            view = questionManagerController.showAnswerForm(question.getRlnm(), null, model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.question.answer.form", view);
            assertNotNull("answer object is null", model.asMap().get(WebConstants.ANSWER_ENTRY));
            answer = (AnswerEntry) model.asMap().get(WebConstants.ANSWER_ENTRY);
            log.info("Answer form loaded successfully");

            log.info("Fill out the answer and submit");
            answer.setNm("Radical anzwer");
            answer.setRdr(1);
            answer.setQrlnm(question.getRlnm());
            Map<String, Integer> map = new HashMap<String, Integer>(1);
            map.put("A", 1);
            answer.setMtvtrs(map);
            answer.setMg("http://questionimages.lela.com/some-image.jpg");

            errors = new BindException(question, WebConstants.ANSWER_ENTRY);
            redirectAttributes = new RedirectAttributesModelMap();
            model = new BindingAwareModelMap();
            view = questionManagerController.submitAnswerForm(question.getRlnm(), answer, errors, redirectAttributes, locale, model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "redirect:/administration/question/" + question.getRlnm(), view);
            assertTrue("Redirect attributes is empty", redirectAttributes.getFlashAttributes().containsKey(WebConstants.MESSAGE));
            log.info("Added question step successfully to question");

            log.info("Verify that the question now contains an answer");
            log.info("Show the question page...");
            model = new BindingAwareModelMap();
            view = questionManagerController.showQuestion(question.getRlnm(), model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.question", view);
            assertNotNull("question object is null", model.asMap().get(WebConstants.QUESTION));
            question = (Question) model.asMap().get(WebConstants.QUESTION);
            assertEquals("Question object is missing answer", 1, question.getNswrs().size(), 0);
            log.info("Show the question page complete");

            log.info("Let's remove the entries in reverse order starting with the answer entry");
            redirectAttributes = new RedirectAttributesModelMap();
            view = questionManagerController.deleteAnswer(question.getRlnm(), answer.getD(), redirectAttributes, locale);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "redirect:/administration/question/" + question.getRlnm(), view);
            assertTrue("Redirect attributes is empty", redirectAttributes.getFlashAttributes().containsKey(WebConstants.MESSAGE));
            
            log.info("Delete the question object...");
            redirectAttributes = new RedirectAttributesModelMap();
            view = questionManagerController.deleteQuestion(question.getRlnm(), redirectAttributes, locale);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "redirect:/administration/question/list", view);
            assertTrue("Redirect attributes is empty", redirectAttributes.getFlashAttributes().containsKey(WebConstants.MESSAGE));
            log.info("Delete the question object complete");

            log.info("Verify that the question has been deleted");
            log.info("Show the question page...");
            model = new BindingAwareModelMap();
            view = questionManagerController.showQuestion(question.getRlnm(), model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.question", view);
            assertNull("question object is not null", model.asMap().get(WebConstants.QUESTION));
            log.info("Show the question page complete");
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            fail(ex.getMessage());
        } finally {
            if (question != null) {
                quizService.removeQuestion(question.getRlnm());
            }
        }

        SpringSecurityHelper.unsecureChannel();
        log.info("Testing questionManagerController...");
    }
}
