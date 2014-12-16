/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.data.web.validator;

import com.lela.commons.service.ApplicationService;
import com.lela.commons.service.QuizService;
import com.lela.domain.document.Application;
import com.lela.domain.document.Quiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validates that a blog submission is correct
 */
@Component
public class ApplicationValidator implements Validator {

    private final ApplicationService applicationService;

    @Autowired
    public ApplicationValidator(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    public boolean supports(Class<?> clazz) {
        return Application.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        Application entry = (Application) target;

        if (!errors.hasErrors()) {

            // make sure the url name is unique
            Application tmp = applicationService.findApplicationByUrlName(entry.getRlnm());

            if (tmp != null) {
                if ((tmp.getId() != null && entry.getId() == null) || !tmp.getId().equals(entry.getId())) {
                    errors.rejectValue("rlnm", "error.duplicate.url.name", new String[]{entry.getRlnm()}, "Url name not unique: " + entry.getRlnm());
                }
            }
        }
    }
}