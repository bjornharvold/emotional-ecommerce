/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.web.web.validator;

import com.lela.commons.web.validator.ImageFileUploadValidator;
import com.lela.domain.dto.list.ExternalListCard;
import com.lela.domain.dto.press.PressImageEntry;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validates that a press image submission is correct
 */
public class ExternalListCardValidator extends ImageFileUploadValidator implements Validator {

    public boolean supports(Class<?> clazz) {
        return ExternalListCard.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        ExternalListCard entry = (ExternalListCard) target;

        // the file and the url can't both be empty
        if (StringUtils.isNotBlank(entry.getImageUrl()) && entry.getMultipartFile() != null && !entry.getMultipartFile().isEmpty()) {
            errors.rejectValue("multipartFile", "list.card.external.image.empty", null, null);
        }

        if (entry.getMultipartFile() != null && !entry.getMultipartFile().isEmpty()) {
            super.validate(entry.getMultipartFile(), errors);
        }

    }
}