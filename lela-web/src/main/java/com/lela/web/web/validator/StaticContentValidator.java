/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.web.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.lela.commons.service.StaticContentService;
import com.lela.domain.document.StaticContent;

/**
 * Validates that a blog submission is correct
 */
@Component
public class StaticContentValidator implements Validator {

    private final StaticContentService staticContentService;

    @Autowired
    public StaticContentValidator(StaticContentService staticContentService) {
        this.staticContentService = staticContentService;
    }

    public boolean supports(Class<?> clazz) {
        return StaticContent.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        StaticContent entry = (StaticContent) target;

        if (!errors.hasErrors()) {

            // make sure the url name is unique
            StaticContent tmp = staticContentService.findStaticContentByUrlName(entry.getRlnm());

            if (tmp != null) {
                if ((tmp.getId() != null && entry.getId() == null) || !tmp.getId().equals(entry.getId())) {
                    errors.rejectValue("rlnm", "error.duplicate.url.name", new String[]{entry.getRlnm()}, "Url name not unique: " + entry.getRlnm());
                }
            }
        }
    }
}