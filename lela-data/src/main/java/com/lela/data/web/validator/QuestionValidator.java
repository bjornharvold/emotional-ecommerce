/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.data.web.validator;

import com.lela.commons.service.QuizService;
import com.lela.domain.document.Question;
import com.lela.domain.document.Quiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validates that a blog submission is correct
 */
@Component
public class QuestionValidator implements Validator {

    private final QuizService quizService;

    @Autowired
    public QuestionValidator(QuizService quizService) {
        this.quizService = quizService;
    }

    public boolean supports(Class<?> clazz) {
        return Question.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        Question entry = (Question) target;

        if (!errors.hasErrors()) {

            // make sure the url name is unique
            Question tmp = quizService.findQuestionByUrlName(entry.getRlnm());

            if (tmp != null) {
                if ((tmp.getId() != null && entry.getId() == null) || !tmp.getId().equals(entry.getId())) {
                    errors.rejectValue("rlnm", "error.duplicate.url.name", new String[]{entry.getRlnm()}, "Url name not unique: " + entry.getRlnm());
                }
            }
        }
    }
}