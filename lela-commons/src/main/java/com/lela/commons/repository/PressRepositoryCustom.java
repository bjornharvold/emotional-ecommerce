/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.Press;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 6/21/12
 * Time: 2:41 PM
 * Responsibility:
 */
public interface PressRepositoryCustom {
    List<Press> findAllPressUrlNames();
    Press findByUrlName(String urlName);
    Press findByUrlName(String urlName, List<String> fields);
}
