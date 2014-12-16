/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.web.web.validator;

import com.lela.commons.web.validator.ImageFileUploadValidator;
import com.lela.domain.dto.list.ListCardPicture;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validates that a list card image is correct
 */
public class ListCardImageValidator extends ImageFileUploadValidator implements Validator {
    private static final Long ALLOWED_FILE_SIZE_IN_BYTES = 1024000L;

    public ListCardImageValidator() {
        super(ALLOWED_FILE_SIZE_IN_BYTES);
    }

    public boolean supports(Class<?> clazz) {
        return ListCardPicture.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        ListCardPicture entry = (ListCardPicture) target;

        if (entry.getMultipartFile() == null || entry.getMultipartFile().isEmpty()) {
            errors.rejectValue("multipartFile", "NotNull", null, null);
        } else if (entry.getMultipartFile() != null && !entry.getMultipartFile().isEmpty()) {
            super.validate(entry.getMultipartFile(), errors);
        }

    }
}