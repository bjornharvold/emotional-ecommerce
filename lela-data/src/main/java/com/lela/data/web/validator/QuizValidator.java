/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.data.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.lela.commons.service.QuizService;
import com.lela.domain.document.Quiz;

/**
 * Validates that a blog submission is correct
 */
@Component
public class QuizValidator implements Validator {

    private final QuizService quizService;

    @Autowired
    public QuizValidator(QuizService quizService) {
        this.quizService = quizService;
    }

    public boolean supports(Class<?> clazz) {
        return Quiz.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        Quiz entry = (Quiz) target;

        if (!errors.hasErrors()) {

            if (entry.getDflt() && !entry.getPblshd()) {
                errors.rejectValue("dflt", "error.default.published", new String[]{entry.getRlnm()}, "Url name not unique: " + entry.getRlnm());
            }

            // make sure the url name is unique
            Quiz tmp = quizService.findQuizByUrlName(entry.getRlnm());

            if (tmp != null) {
                if ((tmp.getId() != null && entry.getId() == null) || !tmp.getId().equals(entry.getId())) {
                    errors.rejectValue("rlnm", "error.duplicate.url.name", new String[]{entry.getRlnm()}, "Url name not unique: " + entry.getRlnm());
                }
            }
        }
    }
}