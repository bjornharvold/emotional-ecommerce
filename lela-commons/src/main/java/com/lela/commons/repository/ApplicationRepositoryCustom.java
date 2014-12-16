/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.Application;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 7/3/12
 * Time: 2:14 PM
 * Responsibility:
 */
public interface ApplicationRepositoryCustom {
    List<Application> findAll(Integer page, Integer maxResults, List<String> fields);
    List<Application> findAll(List<String> fields);
}
