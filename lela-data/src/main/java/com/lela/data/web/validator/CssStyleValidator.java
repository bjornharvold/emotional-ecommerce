/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.data.web.validator;

import com.lela.commons.service.CssStyleService;
import com.lela.domain.document.CssStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validates that a Css Style submission is correct
 */
@Component
public class CssStyleValidator implements Validator {
    private final CssStyleService cssStyleService;

    @Autowired
    public CssStyleValidator(CssStyleService cssStyleService) {
        this.cssStyleService = cssStyleService;
    }

    public boolean supports(Class<?> clazz) {
        return CssStyle.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        CssStyle entry = (CssStyle) target;

        if (!errors.hasErrors()) {
            // make sure the url name is unique
            CssStyle tmp = cssStyleService.findStyleByUrlName(entry.getRlnm());

            if (tmp != null) {
                if ((tmp.getId() != null && entry.getId() == null) || !tmp.getId().equals(entry.getId())) {
                    errors.rejectValue("rlnm", "error.duplicate.url.name", new String[]{entry.getRlnm()}, "Url name not unique: " + entry.getRlnm());
                }
            }
        }
    }
}