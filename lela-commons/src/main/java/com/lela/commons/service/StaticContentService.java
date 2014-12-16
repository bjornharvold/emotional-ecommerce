/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.lela.domain.document.StaticContent;
import com.lela.domain.dto.staticcontent.StaticContentEntry;

/**
 * Created by Bjorn Harvold
 * Date: 6/4/12
 * Time: 7:25 PM
 * Responsibility:
 */
public interface StaticContentService {
    Page<StaticContent> findStaticContents(Integer page, Integer size);
    StaticContent findStaticContentByUrlName(String urlName);
    StaticContent saveStaticContent(StaticContent content);
    StaticContent removeStaticContent(String rlnm);
    Long findStaticContentCount();
    List<StaticContent> paginateThroughAllStaticContent(Integer maxResults, List<String> fields);
    List<StaticContent> findStaticContents(Integer page, Integer size, List<String> fields);

    List<StaticContent> findStaticContents(List<String> fields);

    StaticContent findStaticContentByUrlName(String urlName, Object obj);
    
    StaticContentEntry saveStaticContent(StaticContentEntry staticContentEntry);
}
