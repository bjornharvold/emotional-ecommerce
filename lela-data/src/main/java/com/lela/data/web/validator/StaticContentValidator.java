/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.data.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.lela.commons.service.StaticContentService;
import com.lela.commons.web.validator.ImageFileUploadValidator;
import com.lela.domain.document.StaticContent;
import com.lela.domain.dto.staticcontent.StaticContentEntry;

/**
 * Validates that a blog submission is correct
 */
@Component
public class StaticContentValidator  extends ImageFileUploadValidator implements Validator {

    private final StaticContentService staticContentService;

    @Autowired
    public StaticContentValidator(StaticContentService staticContentService) {
        this.staticContentService = staticContentService;
    }

    public boolean supports(Class<?> clazz) {
        return StaticContent.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        StaticContentEntry entry = (StaticContentEntry) target;

        if (!errors.hasErrors()) {

            // make sure the url name is unique
            StaticContent tmp = staticContentService.findStaticContentByUrlName(entry.getStaticContent().getRlnm());

            if (tmp != null) {
                if ((tmp.getId() != null && entry.getStaticContent().getId() == null) || !tmp.getId().equals(entry.getStaticContent().getId())) {
                    errors.rejectValue("rlnm", "error.duplicate.url.name", new String[]{entry.getStaticContent().getRlnm()}, "Url name not unique: " + entry.getStaticContent().getRlnm());
                }
            }
        }
        
        // if there's a multipart file we need to validate it as well
        if (entry.getMultipartFile() != null) {
            super.validate(entry.getMultipartFile(), errors);
        }
    }
}