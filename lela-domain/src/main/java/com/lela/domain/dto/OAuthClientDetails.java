/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import com.lela.domain.document.OAuthClientDetail;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 7/28/11
 * Time: 12:37 PM
 * Responsibility:
 */
public class OAuthClientDetails extends AbstractJSONPayload {
    private List<OAuthClientDetail> list;

    public OAuthClientDetails() {
    }

    public OAuthClientDetails(List<OAuthClientDetail> list) {
        this.list = list;
    }

    public List<OAuthClientDetail> getList() {
        return list;
    }

    public void setList(List<OAuthClientDetail> list) {
        this.list = list;
    }
}
