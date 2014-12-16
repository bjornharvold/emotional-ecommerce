/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.domain.dto.dom.DOMElement;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 10/1/12
 * Time: 9:53 PM
 * Responsibility:
 */
public interface HTMLUtilityService {
    List<DOMElement> fetchImagesFromUrl(String urlS, Integer minWidth, Integer minHeight);
    List<DOMElement> fetchImagesFromUrl(String urlS, Integer minWidth);
    List<DOMElement> fetchImagesFromUrl(String urlS);
}
