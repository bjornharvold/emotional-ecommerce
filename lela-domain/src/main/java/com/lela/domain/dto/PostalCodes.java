/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import com.lela.domain.document.PostalCode;

import java.util.List;

/**
 * Created by Chris Tallent
 * Date: 11/9/11
 * Time: 12:37 PM
 * Responsibility:
 */
public class PostalCodes extends AbstractJSONPayload {
    private List<PostalCode> list;

    public PostalCodes() {
    }

    public PostalCodes(List<PostalCode> list) {
        this.list = list;
    }

    public List<PostalCode> getList() {
        return list;
    }

    public void setList(List<PostalCode> list) {
        this.list = list;
    }
}
