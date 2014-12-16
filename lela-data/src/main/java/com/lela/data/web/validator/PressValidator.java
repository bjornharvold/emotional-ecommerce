/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.data.web.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.lela.commons.service.PressService;
import com.lela.domain.document.Press;

/**
 * Validates that a blog submission is correct
 */
@Component
public class PressValidator implements Validator {

    private final PressService pressService;

    @Autowired
    public PressValidator(PressService pressService) {
        this.pressService = pressService;
    }

    public boolean supports(Class<?> clazz) {
        return Press.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        Press entry = (Press) target;

        if (!errors.hasErrors()) {

            // make sure the url name is unique
            List<String> fields = new ArrayList<String>(2);
            fields.add("id");
            fields.add("rlnm");
            Press tmp = pressService.findPressByUrlName(entry.getRlnm(), fields);

            if (tmp != null) {
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nm", "error.form.field.required");
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "rlnm", "error.form.field.required");
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "srlnm", "error.form.field.required");

                // don't check for this until all other errors have been taken care of
                if (!errors.hasErrors()) {
                    if ((tmp.getId() != null && entry.getId() == null) || !tmp.getId().equals(entry.getId())) {
                        errors.rejectValue("rlnm", "error.duplicate.url.name", new String[]{entry.getRlnm()}, "Url name not unique: " + entry.getRlnm());
                    }
                }
            }
        }
    }
}