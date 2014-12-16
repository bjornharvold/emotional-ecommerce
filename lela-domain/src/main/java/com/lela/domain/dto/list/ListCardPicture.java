/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.list;

import com.lela.domain.dto.AbstractJSONPayload;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 9/17/12
 * Time: 11:40 AM
 * Responsibility:
 */
public class ListCardPicture extends AbstractJSONPayload implements Serializable {
    private static final long serialVersionUID = 7914811233487600470L;

    private MultipartFile multipartFile;

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

}
