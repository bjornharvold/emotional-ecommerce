/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.data.web.controller.administration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Locale;

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
import com.lela.data.web.controller.administration.quiz.QuizManagerController;
import com.lela.domain.document.Quiz;
import com.lela.domain.document.QuizStep;
import com.lela.domain.document.QuizStepEntry;
import com.lela.domain.enums.QuizStepType;

/**
 * Created by Bjorn Harvold
 * Date: 7/4/12
 * Time: 7:08 PM
 * Responsibility:
 */
public class QuizManagerControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(QuizManagerControllerFunctionalTest.class);

    @Autowired
    private QuizManagerController quizManagerController;

    @Autowired
    private QuizService quizService;

    @Test
    public void testQuizManagerController() {
        Quiz quiz = null;
        QuizStep quizStep = null;
        QuizStepEntry quizStepEntry = null;
        String view = null;
        Model model = null;
        BindingResult errors = null;
        RedirectAttributes redirectAttributes;
        Locale locale = Locale.US;
        log.info("Testing quiz functionality in quizManagerController...");

        log.info("First we need to secure the context because these are admin-only methods");
        SpringSecurityHelper.secureChannel();

        try {
            log.info("Listing all existing quizzes...");
            model = new BindingAwareModelMap();
            view = quizManagerController.showQuizzes(0, model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.quiz.list", view);
            log.info("Listing all existing quizzes complete. We don't really care if this methods contains a list or not. We will be creating a quiz shortly.");

            log.info("Go to the quiz form page and get ready to create a quiz...");
            model = new BindingAwareModelMap();
            view = quizManagerController.showQuizForm(null, model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.quiz.form", view);
            assertNotNull("quiz object is null", model.asMap().get(WebConstants.QUIZ));
            quiz = (Quiz) model.asMap().get(WebConstants.QUIZ);
            log.info("Go to the quiz form page and get ready to create a quiz complete");

            log.info("Fill out quiz form object and submit form...");
            quiz.setDflt(false);
            quiz.setLng(locale.getLanguage());
            quiz.setNm(QuizManagerControllerFunctionalTest.class.getSimpleName());
            quiz.setRlnm(QuizManagerControllerFunctionalTest.class.getSimpleName());
            quiz.setSrlnm(QuizManagerControllerFunctionalTest.class.getSimpleName());
            quiz.setPblshd(true);

            errors = new BindException(quiz, WebConstants.QUIZ);
            redirectAttributes = new RedirectAttributesModelMap();
            model = new BindingAwareModelMap();
            view = quizManagerController.submitQuizForm(quiz, errors, redirectAttributes, locale, model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "redirect:/administration/quiz/" + quiz.getRlnm(), view);
            assertTrue("Redirect attributes is empty", redirectAttributes.getFlashAttributes().containsKey(WebConstants.MESSAGE));
            log.info("Fill out quiz form object and submit form complete");

            log.info("Show the quiz page...");
            model = new BindingAwareModelMap();
            view = quizManagerController.showQuiz(quiz.getRlnm(), model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.quiz", view);
            assertNotNull("quiz object is null", model.asMap().get(WebConstants.QUIZ));
            quiz = (Quiz) model.asMap().get(WebConstants.QUIZ);
            log.info("Show the quiz page complete");

            log.info("Now that we have a persisted quiz, time to add a quiz step to it...");
            log.info("Show quiz step form page");
            model = new BindingAwareModelMap();
            view = quizManagerController.showQuizStepForm(quiz.getRlnm(), null, model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.quiz.step.form", view);
            assertNotNull("quiz step object is null", model.asMap().get(WebConstants.QUIZ_STEP));
            quizStep = (QuizStep) model.asMap().get(WebConstants.QUIZ_STEP);
            log.info("Quiz step form loaded successfully");

            log.info("Fill out the quiz step and submit");
            quizStep.setNm("Radical kuiz ztep");
            quizStep.setRlnm("radical-kuiz-ztep");
            quizStep.setSrlnm("Radical-Kuiz-Ztep");
            quizStep.setQrlnm(quiz.getRlnm());
            quizStep.setRdr(1);
            quizStep.setTp(QuizStepType.QUESTION);

            errors = new BindException(quiz, WebConstants.QUIZ_STEP);
            redirectAttributes = new RedirectAttributesModelMap();
            model = new BindingAwareModelMap();
            view = quizManagerController.submitQuizStepForm(quiz.getRlnm(), quizStep, errors, redirectAttributes, locale, model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "redirect:/administration/quiz/" + quiz.getRlnm() + "/step/" + quizStep.getD(), view);
            assertTrue("Redirect attributes is empty", redirectAttributes.getFlashAttributes().containsKey(WebConstants.MESSAGE));
            log.info("Added quiz step successfully to quiz");

            log.info("Show quiz step page...");
            model = new BindingAwareModelMap();
            view = quizManagerController.showQuizStep(quiz.getRlnm(), quizStep.getD(), model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.quiz.step", view);
            assertNotNull("quiz step object is null", model.asMap().get(WebConstants.QUIZ_STEP));
            quizStep = (QuizStep) model.asMap().get(WebConstants.QUIZ_STEP);
            log.info("Show quiz step page completed");

            log.info("Time to add a question step entry to this quiz step...");
            model = new BindingAwareModelMap();
            view = quizManagerController.showQuizStepEntryForm(quiz.getRlnm(), quizStep.getD(), null, model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.quiz.step.entry.form", view);
            assertNotNull("quiz step entry object is null", model.asMap().get(WebConstants.QUIZ_STEP_ENTRY));
            quizStepEntry = (QuizStepEntry) model.asMap().get(WebConstants.QUIZ_STEP_ENTRY);

            log.info("Fill out quiz step entry form and submit...");
            quizStepEntry.setQrlnm(quiz.getRlnm());
            quizStepEntry.setQd(quizStep.getD());
            quizStepEntry.setRdr(1);
            // this question id does not exist but the back-end doesn't validate on this as it's a dropdown which has to be answered
            quizStepEntry.setRlnm("kuiz-schtep-entree");

            errors = new BindException(quiz, WebConstants.QUIZ_STEP_ENTRY);
            redirectAttributes = new RedirectAttributesModelMap();
            model = new BindingAwareModelMap();
            view = quizManagerController.submitQuizStepEntryForm(quiz.getRlnm(), quizStep.getD(), quizStepEntry, errors, redirectAttributes, locale, model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "redirect:/administration/quiz/" + quiz.getRlnm() + "/step/" + quizStep.getD(), view);
            assertTrue("Redirect attributes is empty", redirectAttributes.getFlashAttributes().containsKey(WebConstants.MESSAGE));
            log.info("Fill out quiz step entry form and submit complete");
            log.info("Time to add a question step entry to this quiz step complete");

            log.info("Verify that the quiz now contains a quiz step with a quiz entry");
            log.info("Show the quiz page...");
            model = new BindingAwareModelMap();
            view = quizManagerController.showQuiz(quiz.getRlnm(), model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.quiz", view);
            assertNotNull("quiz object is null", model.asMap().get(WebConstants.QUIZ));
            quiz = (Quiz) model.asMap().get(WebConstants.QUIZ);
            assertEquals("Quiz object is missing quiz step", 1, quiz.getStps().size(), 0);
            assertEquals("Quiz step is missing quiz step entry", 1, quiz.getStps().get(0).getNtrs().size(), 0);
            log.info("Show the quiz page complete");

            log.info("Let's remove the entries in reverse order starting with the quiz step entry");
            redirectAttributes = new RedirectAttributesModelMap();
            view = quizManagerController.deleteQuizStepEntry(quiz.getRlnm(), quizStep.getD(), quizStepEntry.getD(), redirectAttributes, locale);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "redirect:/administration/quiz/" + quiz.getRlnm() + "/step/" + quizStep.getD(), view);
            assertTrue("Redirect attributes is empty", redirectAttributes.getFlashAttributes().containsKey(WebConstants.MESSAGE));

            log.info("Verify that the quiz now contains a quiz step with no quiz entry");
            log.info("Show the quiz page...");
            model = new BindingAwareModelMap();
            view = quizManagerController.showQuiz(quiz.getRlnm(), model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.quiz", view);
            assertNotNull("quiz object is null", model.asMap().get(WebConstants.QUIZ));
            quiz = (Quiz) model.asMap().get(WebConstants.QUIZ);
            assertEquals("Quiz object is missing quiz step", 1, quiz.getStps().size(), 0);
            assertTrue("Quiz step still has quiz step entry", quiz.getStps().get(0).getNtrs().isEmpty());
            log.info("Show the quiz page complete");

            log.info("Removing quiz step");
            redirectAttributes = new RedirectAttributesModelMap();
            view = quizManagerController.deleteQuizStep(quiz.getRlnm(), quizStep.getD(), redirectAttributes, locale);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "redirect:/administration/quiz/" + quiz.getRlnm(), view);
            assertTrue("Redirect attributes is empty", redirectAttributes.getFlashAttributes().containsKey(WebConstants.MESSAGE));

            log.info("Verify that the quiz now contains no quiz step");
            log.info("Show the quiz page...");
            model = new BindingAwareModelMap();
            view = quizManagerController.showQuiz(quiz.getRlnm(), model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.quiz", view);
            assertNotNull("quiz object is null", model.asMap().get(WebConstants.QUIZ));
            quiz = (Quiz) model.asMap().get(WebConstants.QUIZ);
            assertTrue("Quiz object still has quiz step", quiz.getStps().isEmpty());
            log.info("Show the quiz page complete");

            log.info("Delete the quiz object...");
            redirectAttributes = new RedirectAttributesModelMap();
            view = quizManagerController.deleteQuiz(quiz.getRlnm(), redirectAttributes, locale);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "redirect:/administration/quiz/list", view);
            assertTrue("Redirect attributes is empty", redirectAttributes.getFlashAttributes().containsKey(WebConstants.MESSAGE));
            log.info("Delete the quiz object complete");

            log.info("Verify that the quiz has been removed");
            log.info("Show the quiz page...");
            model = new BindingAwareModelMap();
            view = quizManagerController.showQuiz(quiz.getRlnm(), model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.quiz", view);
            assertNull("quiz object is not null", model.asMap().get(WebConstants.QUIZ));
            log.info("Show the quiz page complete");
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            fail(ex.getMessage());
        } finally {
            if (quiz != null) {
                quizService.removeQuiz(quiz.getRlnm());
            }
        }

        SpringSecurityHelper.unsecureChannel();
        log.info("Testing quiz functionality in quizManagerController complete");
    }
}
