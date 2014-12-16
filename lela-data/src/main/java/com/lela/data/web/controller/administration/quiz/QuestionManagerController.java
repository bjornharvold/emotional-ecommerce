/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.data.web.controller.administration.quiz;

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
import com.lela.commons.web.utils.WebConstants;
import com.lela.data.web.validator.QuestionValidator;
import com.lela.domain.document.Answer;
import com.lela.domain.document.Question;
import com.lela.domain.dto.quiz.AnswerEntry;
import com.lela.domain.enums.QuestionType;

/**
 * Created by Bjorn Harvold
 * Date: 6/29/12
 * Time: 3:38 PM
 * Responsibility:
 */
@Controller
@SessionAttributes(types = {Question.class, AnswerEntry.class})
public class QuestionManagerController {

    private final QuizService quizService;
    private final MessageSource messageSource;
    private final QuestionValidator questionValidator;
    private static final Integer MAX_RESULTS = 12;

    @Autowired
    public QuestionManagerController(QuizService quizService,
                                     MessageSource messageSource,
                                     QuestionValidator questionValidator) {
        this.quizService = quizService;
        this.messageSource = messageSource;
        this.questionValidator = questionValidator;
    }

    @RequestMapping(value = "/administration/question/list", method = RequestMethod.GET)
    public String showQuestions(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                Model model) throws Exception {
        model.addAttribute(WebConstants.QUESTIONS, quizService.findQuestions(page, MAX_RESULTS));
        return "admin.question.list";
    }

    @RequestMapping(value = "/administration/question/{urlName}", method = RequestMethod.GET)
    public String showQuestion(@PathVariable("urlName") String urlName,
                               Model model) throws Exception {
        model.addAttribute(WebConstants.QUESTION, quizService.findQuestionByUrlName(urlName));
        return "admin.question";
    }

    /**
     * Deletes the question
     *
     * @param urlName            urlName
     * @param redirectAttributes redirectAttributes
     * @param locale             locale
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/question/{urlName}", method = RequestMethod.DELETE)
    public String deleteQuestion(@PathVariable("urlName") String urlName,
                             RedirectAttributes redirectAttributes, Locale locale) throws Exception {
        String view = "redirect:/administration/question/list";

        quizService.removeQuestion(urlName);

        // add a success message that can be seen on the redirected page
        String message = messageSource.getMessage("question.deleted.successfully", new String[]{urlName}, locale);
        redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);

        return view;
    }

    @RequestMapping(value = "/administration/question/form", method = RequestMethod.GET)
    public String showQuestionForm(@RequestParam(value = "urlName", required = false) String urlName,
                                   Model model) throws Exception {

        if (StringUtils.isBlank(urlName)) {
            model.addAttribute(WebConstants.QUESTION, new Question());
        } else {
            model.addAttribute(WebConstants.QUESTION, quizService.findQuestionByUrlName(urlName));
        }

        model.addAttribute(WebConstants.QUESTION_TYPES, QuestionType.values());

        return "admin.question.form";
    }

    @RequestMapping(value = "/administration/question/form", method = RequestMethod.POST)
    public String submitQuestionForm(@Valid Question question, BindingResult errors,
                                     RedirectAttributes redirectAttributes, Locale locale,
                                     Model model) throws Exception {
        String view = null;

        // check for errors
        questionValidator.validate(question, errors);

        if (errors.hasErrors()) {
            view = "admin.question.form";
            model.addAttribute(WebConstants.QUESTION, question);

        } else {
            question = quizService.saveQuestion(question);
            view = "redirect:/administration/question/" + question.getRlnm();

            // add a success message that can be seen on the redirected page
            String message = messageSource.getMessage("question.saved.successfully", new String[]{question.getRlnm()}, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        }

        return view;
    }

    /**
     * Form for creating answers to the question
     *
     * @param urlName urlName
     * @param model   model
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/question/{urlName}/answer", method = RequestMethod.GET)
    public String showAnswerForm(@PathVariable("urlName") String urlName,
                                 @RequestParam(value = "d", required = false) String answerId,
                                 Model model) throws Exception {
        Answer answer = quizService.findAnswer(urlName, answerId);

        if (answer == null) {
            model.addAttribute(WebConstants.ANSWER_ENTRY, new AnswerEntry(urlName));
        } else {
            model.addAttribute(WebConstants.ANSWER_ENTRY, new AnswerEntry(answer));
        }

        return "admin.question.answer.form";
    }

    /**
     * Deletes the answer
     *
     * @param urlName     urlName
     * @param answerId answerId
     * @return Tile def
     * @throws Exception
     */
    @RequestMapping(value = "/administration/question/{urlName}/answer/{answerId}", method = RequestMethod.DELETE)
    public String deleteAnswer(@PathVariable(value = "urlName") String urlName,
                                 @PathVariable(value = "answerId") String answerId,
                                 RedirectAttributes redirectAttributes, Locale locale) throws Exception {
        String view = "redirect:/administration/question/" + urlName;

        quizService.removeAnswer(urlName, answerId);

        // add a success message that can be seen on the redirected page
        String message = messageSource.getMessage("answer.deleted.successfully", new String[]{answerId}, locale);
        redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);

        return view;
    }

    @RequestMapping(value = "/administration/question/{urlName}/answer", method = RequestMethod.POST)
    public String submitAnswerForm(@PathVariable("urlName") String urlName,
                                   @Valid AnswerEntry answerEntry, BindingResult errors,
                                     RedirectAttributes redirectAttributes, Locale locale,
                                     Model model) throws Exception {
        String view = null;

        if (errors.hasErrors()) {
            view = "admin.question.answer.form";
            model.addAttribute(WebConstants.ANSWER_ENTRY, answerEntry);

        } else {
            quizService.saveAnswerEntry(answerEntry);
            view = "redirect:/administration/question/" + urlName;

            // add a success message that can be seen on the redirected page
            String message = messageSource.getMessage("answer.saved.successfully", new String[]{answerEntry.getNm()}, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        }

        return view;
    }
}
