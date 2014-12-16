/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Bjorn Harvold
 * Date: 3/24/12
 * Time: 3:29 AM
 * Responsibility:
 */
public abstract class ImageFileUploadValidator extends FileUploadValidator {
    private final static String[] allowedFileExtensions = {"jpg", "png", "gif"};

    public ImageFileUploadValidator(Long allowedFileSizeInBytes) {
        super(allowedFileExtensions, allowedFileSizeInBytes);
    }

    public ImageFileUploadValidator() {
        super(allowedFileExtensions);
    }

    public void validate(MultipartFile mf, Errors errors) {
        super.validate(mf, errors);
    }
}
