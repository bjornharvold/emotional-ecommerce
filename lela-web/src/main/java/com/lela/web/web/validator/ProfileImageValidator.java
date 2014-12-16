/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.web.web.validator;

import com.lela.commons.web.validator.ImageFileUploadValidator;
import com.lela.domain.dto.press.PressImageEntry;
import com.lela.domain.dto.user.ProfilePictureUpload;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validates a profile picture
 */
public class ProfileImageValidator extends ImageFileUploadValidator implements Validator {
    private static final Long allowedFileSizeInBytes = 2097152L; // 2 mb

    public ProfileImageValidator() {
        super(allowedFileSizeInBytes);
    }

    @Override
    public boolean supports(Class clazz) {
        return ProfilePictureUpload.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        ProfilePictureUpload entry = (ProfilePictureUpload) target;

        super.validate(entry.getMultipartFile(), errors);

    }
}