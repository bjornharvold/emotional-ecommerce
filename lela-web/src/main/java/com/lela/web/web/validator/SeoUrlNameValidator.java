/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.web.web.validator;

import com.lela.commons.service.SeoUrlNameService;
import com.lela.domain.document.SeoUrlName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SeoUrlNameValidator implements Validator {
    private final SeoUrlNameService seoUrlNameService;

    @Autowired
    public SeoUrlNameValidator(SeoUrlNameService seoUrlNameService) {
        this.seoUrlNameService = seoUrlNameService;
    }


    public boolean supports(Class<?> clazz) {
        return SeoUrlName.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        SeoUrlName seoUrlName = (SeoUrlName) target;

        if (!errors.hasErrors()) {
            // check for srlnm availability
            SeoUrlName tmp = seoUrlNameService.findSeoUrlName(seoUrlName.getSrlnm());

            if (tmp != null) {
                if (!tmp.getId().equals(seoUrlName.getId())) {
                    errors.rejectValue("srlnm", "error.duplicate.seo.url.name", null, "Seo url name is already taken");
                }
            }
        }
    }
}