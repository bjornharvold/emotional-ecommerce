/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.domain.document.OAuthClientDetail;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 6/4/12
 * Time: 7:18 PM
 * Responsibility:
 */
public interface OAuthService {
    OAuthClientDetail findOAuthClientDetailsByClientId(String clientId);
    void saveOAuthClientDetails(List<OAuthClientDetail> list);
    void saveOAuthClientDetail(OAuthClientDetail detail);
    OAuthClientDetail removeOAuthClientDetails(String clientId);
}
