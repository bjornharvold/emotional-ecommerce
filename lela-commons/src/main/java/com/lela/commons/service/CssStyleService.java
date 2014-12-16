/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.service;

import com.lela.domain.document.CssStyle;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * User: Chris Tallent
 * Date: 12/4/12
 * Time: 4:45 PM
 */
public interface CssStyleService {
    List<CssStyle> findCssStyles();
    Page<CssStyle> findStyles(Integer page, Integer maxResults);
    CssStyle findStyleByUrlName(String urlName);
    CssStyle saveStyle(CssStyle style);
    void removeStyle(String urlName);

}
