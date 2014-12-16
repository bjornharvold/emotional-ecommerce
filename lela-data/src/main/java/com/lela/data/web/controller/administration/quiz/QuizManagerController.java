/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.data.web.controller.administration.quiz;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lela.commons.service.QuizService;
import com.lela.commons.service.StaticContentService;
import com.lela.commons.utilities.LocaleHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.data.web.validator.QuizValidator;
import com.lela.domain.document.Quiz;
import com.lela.domain.document.QuizStep;
import com.lela.domain.document.QuizStepEntry;
import com.lela.domain.enums.QuizStepType;

/**
 * Created by Bjorn Harvold
 * Date: 6/27/12
 * Time: 5:54 PM
 * Responsibility:
 */
@Controller
@SessionAttributes(types = {Quiz.class, QuizStep.class, QuizStepEntry.class})
public class QuizManagerController  {

    private final QuizService quizService;
    private final StaticContentService staticContentService;
    private final QuizValidator quizValidator;
    private final MessageSource messageSource;
    private static final Integer MAX_RESULTS = 12;
    private static String CHANGE_ME = "-change-me";

    @Autowired
    public QuizManagerController(QuizService quizService,
                                 StaticContentService staticContentService,
                                 QuizValidator quizValidator,
                                 MessageSource messageSource) {
        this.quizService = quizService;
        this.staticContentService = staticContentService;
        this.quizValidator = quizValidator;
        this.messageSource = messageSource;
    }

    /**
     * Show quiz list page
     *
     * @param page  page
     * @param model model
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/quiz/list", method = RequestMethod.GET)
    public String showQuizzes(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                              Model model) throws Exception {
        String view = "admin.quiz.list";

        model.addAttribute(WebConstants.QUIZZES, quizService.findQuizzes(page, MAX_RESULTS));

        return view;
    }

    /**
     * Shows quiz page
     *
     * @param urlName urlName
     * @param model   model
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/quiz/{urlName}", method = RequestMethod.GET)
    public String showQuiz(@PathVariable("urlName") String urlName,
                           Model model) throws Exception {
        String view = "admin.quiz";

        model.addAttribute(WebConstants.QUIZ, quizService.findQuizByUrlName(urlName));

        return view;
    }

    /**
     * Deletes the quiz
     *
     * @param urlName            urlName
     * @param redirectAttributes redirectAttributes
     * @param locale             locale
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/quiz/{quizUrlName}", method = RequestMethod.DELETE)
    public String deleteQuiz(@PathVariable("quizUrlName") String urlName,
                             RedirectAttributes redirectAttributes, Locale locale) throws Exception {
        String view = "redirect:/administration/quiz/list";

        quizService.removeQuiz(urlName);

        // add a success message that can be seen on the redirected page
        String message = messageSource.getMessage("quiz.deleted.successfully", new String[]{urlName}, locale);
        redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);

        return view;
    }

    /**
     * Display the quiz form
     *
     * @param urlName urlName
     * @param model   model
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/quiz/form", method = RequestMethod.GET)
    public String showQuizForm(@RequestParam(value = "urlName", required = false) String urlName,
                               Model model) throws Exception {
        String view = "admin.quiz.form";

        populateReferenceData(model);

        if (StringUtils.isNotBlank(urlName)) {
            model.addAttribute(WebConstants.QUIZ, quizService.findQuizByUrlName(urlName));
        } else {
            model.addAttribute(WebConstants.QUIZ, new Quiz(Locale.ENGLISH.getLanguage()));
        }

        return view;
    }

    /**
     * Submits the quiz object
     *
     * @param quiz               quiz
     * @param errors             errors
     * @param redirectAttributes redirectAttributes
     * @param locale             locale
     * @param model              model
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/quiz/form", method = RequestMethod.POST)
    public String submitQuizForm(@Valid Quiz quiz, BindingResult errors,
                                 RedirectAttributes redirectAttributes, Locale locale,
                                 Model model) throws Exception {
        String view = null;

        // check for errors
        quizValidator.validate(quiz, errors);

        if (errors.hasErrors()) {
            view = "admin.quiz.form";
            populateReferenceData(model);
            model.addAttribute(WebConstants.QUIZ, quiz);
        } else {
            if (quiz.getCmpltcntnt() != null && quiz.getCmpltcntnt().length() == 0) {
                quiz.setCmpltcntnt(null);
            }

            if (quiz.getRtrncntnt() == null || (quiz.getRtrncntnt() != null && quiz.getRtrncntnt().length() == 0)) {
                quiz.setRtrncntnt(quiz.getCmpltcntnt());
            }

            if (quiz.getCmpltcntnt() == null) {
                quiz.setCmpltcntnt(quiz.getRtrncntnt());
            }

            quiz = quizService.saveQuiz(quiz);
            view = "redirect:/administration/quiz/" + quiz.getRlnm();

            // add a success message that can be seen on the redirected page
            String message = messageSource.getMessage("quiz.saved.successfully", new String[]{quiz.getRlnm()}, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        }

        return view;
    }

    /**
     * Shows the quiz step view page
     *
     * @param urlName    urlName
     * @param quizStepId quizStepId
     * @param model      model
     * @return Tile def
     * @throws Exception //
     */
    @RequestMapping(value = "/administration/quiz/{urlName}/step/{quizStepId}", method = RequestMethod.GET)
    public String showQuizStep(@PathVariable("urlName") String urlName,
                               @PathVariable("quizStepId") String quizStepId,
                               Model model) throws Exception {
        String view = "admin.quiz.step";

        model.addAttribute(WebConstants.QUIZ_STEP, quizService.findQuizStep(urlName, quizStepId));

        return view;
    }

    /**
     * Deletes the quiz step
     *
     * @param urlName    urlName
     * @param quizStepId quizStepId
     * @return Tile def
     * @throws Exception
     */
    @RequestMapping(value = "/administration/quiz/{urlName}/step/{quizStepId}", method = RequestMethod.DELETE)
    public String deleteQuizStep(@PathVariable(value = "urlName") String urlName,
                                 @PathVariable(value = "quizStepId") String quizStepId,
                                 RedirectAttributes redirectAttributes, Locale locale) throws Exception {
        String view = "redirect:/administration/quiz/" + urlName;

        quizService.removeQuizStep(urlName, quizStepId);

        // add a success message that can be seen on the redirected page
        String message = messageSource.getMessage("quiz.step.deleted.successfully", new String[]{quizStepId}, locale);
        redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);

        return view;
    }

    @RequestMapping(value = "/administration/quiz/{urlName}/step/form", method = RequestMethod.GET)
    public String showQuizStepForm(@PathVariable(value = "urlName") String urlName,
                                   @RequestParam(value = "quizStepId", required = false) String quizStepId,
                                   Model model) throws Exception {
        String view = "admin.quiz.step.form";

        QuizStep step = quizService.findQuizStep(urlName, quizStepId);

        if (step != null) {
            model.addAttribute(WebConstants.QUIZ_STEP, step);
        } else {
            model.addAttribute(WebConstants.QUIZ_STEP, new QuizStep(urlName));
        }

        model.addAttribute(WebConstants.QUIZ_STEP_TYPES, QuizStepType.values());
        return view;
    }

    /**
     * Shows quiz step form
     *
     * @param urlName            urlName
     * @param step               step
     * @param errors             errors
     * @param redirectAttributes redirectAttributes
     * @param locale             locale
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/quiz/{urlName}/step/form", method = RequestMethod.POST)
    public String submitQuizStepForm(@PathVariable("urlName") String urlName,
                                     @Valid QuizStep step, BindingResult errors,
                                     RedirectAttributes redirectAttributes,
                                     Locale locale, Model model) throws Exception {
        String view = null;

        if (errors.hasErrors()) {
            model.addAttribute(WebConstants.QUIZ_STEP, step);
            model.addAttribute(WebConstants.QUIZ_STEP_TYPES, QuizStepType.values());
            view = "admin.quiz.step.form";
        } else {
            quizService.saveQuizStep(step);
            view = "redirect:/administration/quiz/" + urlName + "/step/" + step.getD();

            // add a success message that can be seen on the redirected page
            String message = messageSource.getMessage("quiz.step.saved.successfully", new String[]{step.getRlnm()}, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        }

        return view;
    }

    /**
     * Show quiz step question form
     *
     * @param urlName    urlName
     * @param quizStepId quizStepId
     * @param entryId questionId
     * @param model      model
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/quiz/{urlName}/step/{quizStepId}/entry", method = RequestMethod.GET)
    public String showQuizStepEntryForm(@PathVariable(value = "urlName") String urlName,
                                        @PathVariable(value = "quizStepId") String quizStepId,
                                        @RequestParam(value = "entryId", required = false) String entryId,
                                        Model model) throws Exception {
        String view = "admin.quiz.step.entry.form";

        QuizStep step = quizService.findQuizStep(urlName, quizStepId);

        QuizStepEntry entry = quizService.findQuizStepQuestion(urlName, quizStepId, entryId);

        if (entry != null) {
            model.addAttribute(WebConstants.QUIZ_STEP_ENTRY, entry);
        } else {
            model.addAttribute(WebConstants.QUIZ_STEP_ENTRY, new QuizStepEntry(urlName, quizStepId));
        }

        switch (step.getTp()) {
            case QUESTION:
                model.addAttribute(WebConstants.QUIZ_STEP_ENTRY_VALUES, quizService.findAllQuestions());
                break;
            case SPLASH:
                List<String> fields = new ArrayList<String>(1);
                fields.add("rlnm");
                fields.add("nm");
                model.addAttribute(WebConstants.QUIZ_STEP_ENTRY_VALUES, staticContentService.findStaticContents(fields));
                break;
        }


        return view;
    }

    /**
     * Shows quiz step question form
     *
     * @param urlName            urlName
     * @param question           step
     * @param errors             errors
     * @param redirectAttributes redirectAttributes
     * @param locale             locale
     * @param model              model
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/quiz/{urlName}/step/{quizStepId}/entry", method = RequestMethod.POST)
    public String submitQuizStepEntryForm(@PathVariable("urlName") String urlName,
                                          @PathVariable("quizStepId") String quizStepId,
                                          @Valid QuizStepEntry question, BindingResult errors,
                                          RedirectAttributes redirectAttributes, Locale locale,
                                          Model model) throws Exception {
        String view = null;

        if (errors.hasErrors()) {
            view = "admin.quiz.step.entry.form";
            model.addAttribute(WebConstants.QUIZ_STEP_ENTRY, question);
            model.addAttribute(WebConstants.QUESTIONS, quizService.findAllQuestions());
        } else {
            quizService.saveQuizStepQuestion(question);
            view = "redirect:/administration/quiz/" + urlName + "/step/" + quizStepId;

            // add a success message that can be seen on the redirected page
            String message = messageSource.getMessage("quiz.step.entry.saved.successfully", new String[]{question.getRlnm()}, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        }

        return view;
    }

    /**
     * Delete quiz step question form
     *
     * @param urlName            urlName
     * @param redirectAttributes redirectAttributes
     * @param locale             locale
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/quiz/{urlName}/step/{quizStepId}/entry/{entryId}", method = RequestMethod.POST)
    public String deleteQuizStepEntry(@PathVariable("urlName") String urlName,
                                      @PathVariable("quizStepId") String quizStepId,
                                      @PathVariable("entryId") String entryId,
                                      RedirectAttributes redirectAttributes, Locale locale) throws Exception {
        String view = "redirect:/administration/quiz/" + urlName + "/step/" + quizStepId;

        quizService.removeQuizStepQuestion(urlName, quizStepId, entryId);

        // add a success message that can be seen on the redirected page
        String message = messageSource.getMessage("quiz.step.entry.deleted.successfully", new String[]{quizStepId}, locale);
        redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);

        return view;
    }

    @RequestMapping(value = "/administration/quiz/{urlName}/preview", method = RequestMethod.GET)
    public String previewQuiz(@PathVariable("urlName") String urlName,
                              @RequestParam(value = "stepUrlName", required = false) String stepUrlName,
                              Model model) throws Exception {
        String view = "admin.quiz.preview";

        // retrieve the quiz
        Quiz quiz = quizService.findQuizByUrlName(urlName);
        model.addAttribute(WebConstants.QUIZ, quiz);
        QuizStep qs = null;

        // retrieve the first step if quizUrlName is not specified
        if (StringUtils.isBlank(stepUrlName)) {
            qs = quiz.findFirstQuizStep();
        } else {
            qs = quiz.findQuizStepByUrlName(stepUrlName);
        }

        model.addAttribute(WebConstants.QUIZ_STEP, qs);

        QuizStep nextQuizStep = quiz.findNextQuizStep(qs.getRlnm());

        if (nextQuizStep != null) {
            model.addAttribute(WebConstants.NEXT_QUIZ_STEP, nextQuizStep.getRlnm());
        }

        QuizStep previousQuizStep = quiz.findPreviousQuizStep(qs.getRlnm());

        if (previousQuizStep != null) {
            model.addAttribute(WebConstants.PREVIOUS_QUIZ_STEP, previousQuizStep.getRlnm());
        }

        return view;
    }

    /**
     * Duplicates the quiz
     *
     * @param urlName            urlName
     * @param redirectAttributes redirectAttributes
     * @param locale             locale
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/quiz/duplicate/{quizUrlName}", method = RequestMethod.GET)
    public String duplicateQuiz(@PathVariable("quizUrlName") String urlName,
                             RedirectAttributes redirectAttributes, Locale locale) throws Exception {
        String view = "redirect:/administration/quiz/list";
        
        Quiz quiz = quizService.findQuizByUrlName(urlName);        
        //Change the url
        String changedUrl = quiz.getRlnm() + CHANGE_ME;
        quiz.setRlnm(changedUrl);
        
        //Clear out the id, so it is saved as a new quiz
        quiz.setId(null);
        quizService.saveQuiz(quiz);

        // add a success message that can be seen on the redirected page
        String message = messageSource.getMessage("quiz.duplicated.successfully", new String[]{urlName, changedUrl}, locale);
        redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);

        return view;
    }
    
    private void populateReferenceData(Model model) {
        LocaleHelper lh = new LocaleHelper();
        model.addAttribute(WebConstants.LOCALES, lh.getUniqueLanguages());

        List<String> fields = new ArrayList<String>(1);
        fields.add("rlnm");
        fields.add("nm");
        model.addAttribute(WebConstants.STATIC_CONTENTS, staticContentService.findStaticContents(fields));
    }
}
