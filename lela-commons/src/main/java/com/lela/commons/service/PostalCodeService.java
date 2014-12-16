/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.domain.document.PostalCode;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 5/10/12
 * Time: 4:31 PM
 * Responsibility:
 */
public interface PostalCodeService {
    List<PostalCode> savePostalCodes(List<PostalCode> list);
    PostalCode savePostalCode(PostalCode postalCode);
    PostalCode removePostalCode(String code);
    PostalCode findPostalCodeByCode(String code);
    PostalCode findPostalCodeByCityAndStateName(String city, String stateName);
}
