/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.web.validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Bjorn Harvold
 * Date: 3/24/12
 * Time: 3:28 AM
 * Responsibility:
 */
public abstract class FileUploadValidator {
    private final String[] allowedFileExtensions;
    private final Long allowedFileSizeInBytes;

    public FileUploadValidator(String[] allowedFileExtensions, Long allowedFileSizeInBytes) {
        this.allowedFileExtensions = allowedFileExtensions;
        this.allowedFileSizeInBytes = allowedFileSizeInBytes;
    }

    public FileUploadValidator(Long allowedFileSizeInBytes) {
        this.allowedFileSizeInBytes = allowedFileSizeInBytes;
        this.allowedFileExtensions = null;
    }

    public FileUploadValidator(String[] allowedFileExtensions) {
        this.allowedFileExtensions = allowedFileExtensions;
        this.allowedFileSizeInBytes = null;
    }

    public void validate(MultipartFile mf, Errors errors) {
        boolean match = false;

        if (mf != null && !mf.isEmpty()) {

            if (allowedFileExtensions != null) {
                for (String extension : allowedFileExtensions) {
                    if (StringUtils.endsWithIgnoreCase(mf.getOriginalFilename(), extension)) {
                        match = true;
                    }
                }

                if (!match) {
                    StringBuilder sb = new StringBuilder();
                    for (String extension : allowedFileExtensions) {
                        sb.append(extension);
                        sb.append(", ");
                    }

                    sb.delete(sb.length() - 2, sb.length() - 1);
                    errors.rejectValue("multipartFile", "error.form.file.extension", new String[]{sb.toString()}, "Required " + sb.toString() + " file");
                }
            }

            if (allowedFileSizeInBytes != null && allowedFileSizeInBytes < mf.getSize()) {
                errors.rejectValue("multipartFile", "error.form.file.size", new String[]{Long.toString(mf.getSize()), allowedFileSizeInBytes.toString()}, String.format("File too big: %d. Max file size: %d", mf.getSize(), allowedFileSizeInBytes));
            }
        }
    }
}
