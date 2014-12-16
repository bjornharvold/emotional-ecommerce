/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.list;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;

/**
 * Created by Bjorn Harvold
 * Date: 10/8/12
 * Time: 12:17 PM
 * Responsibility:
 */
public class ExternalImageRequest {
    @NotNull
    @NotEmpty
    @URL
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
