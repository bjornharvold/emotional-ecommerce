/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.list;

import com.lela.domain.dto.AbstractJSONPayload;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 10/2/12
 * Time: 10:07 PM
 * Responsibility:
 */
public class ExternalListCardPicture extends AbstractJSONPayload implements Serializable {
    private static final long serialVersionUID = 7711847869366780094L;

    @NotNull
    @NotEmpty
    private String url;

    @NotNull
    @NotEmpty
    private String width;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }
}
