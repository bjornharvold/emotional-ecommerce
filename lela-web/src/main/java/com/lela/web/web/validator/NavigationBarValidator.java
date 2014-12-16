/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.web.web.validator;

import com.lela.commons.service.NavigationBarService;
import com.lela.domain.document.NavigationBar;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validates that a navigation bar submission is correct
 */
@Component
public class NavigationBarValidator implements Validator {

    private final NavigationBarService navigationBarService;

    @Autowired
    public NavigationBarValidator(NavigationBarService navigationBarService) {
        this.navigationBarService = navigationBarService;
    }
    
    public boolean supports(Class<?> clazz) {
        return NavigationBar.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        NavigationBar entry = (NavigationBar) target;

        if (!errors.hasErrors()) {

            // make sure the url name is unique
            NavigationBar tmp = navigationBarService.findNavigationBarByUrlName(entry.getRlnm());

            if (tmp != null) {
                // the url name has to be unique across locale
                if (((tmp.getId() != null && entry.getId() == null) || !tmp.getId().equals(entry.getId()))
                        && entry.getLcl().equals(tmp.getLcl())) {
                    errors.rejectValue("rlnm", "error.duplicate.url.name", new String[]{entry.getRlnm()}, "Url name not unique: " + entry.getRlnm());
                }
            }
        }
    }
}