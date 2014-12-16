/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.user;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 9/13/12
 * Time: 3:34 PM
 * Responsibility:
 */
public class ProfilePictureUpload implements Serializable {
    private static final long serialVersionUID = -5323846537099845547L;

    private MultipartFile multipartFile;

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }
}
