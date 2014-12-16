/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.web.web.validator;

import com.lela.commons.web.validator.ImageFileUploadValidator;
import com.lela.domain.dto.press.PressImageEntry;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validates that a press image submission is correct
 */
public class PressImageValidator extends ImageFileUploadValidator implements Validator {

    public boolean supports(Class<?> clazz) {
        return PressImageEntry.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        PressImageEntry entry = (PressImageEntry) target;

        if (StringUtils.isBlank(entry.getMgrl()) && (entry.getMultipartFile() == null || entry.getMultipartFile().isEmpty())) {
            errors.rejectValue("multipartFile", "NotNull", null, null);
        } else if (entry.getMultipartFile() != null && !entry.getMultipartFile().isEmpty()) {
            super.validate(entry.getMultipartFile(), errors);
        }

    }
}