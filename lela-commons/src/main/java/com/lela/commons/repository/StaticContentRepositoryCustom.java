/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.Item;
import com.lela.domain.document.StaticContent;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 1/6/12
 * Time: 2:48 AM
 * Responsibility:
 */
public interface StaticContentRepositoryCustom {
    List<StaticContent> findAll(Integer page, Integer size, List<String> fields);

    List<StaticContent> findAll(List<String> fields);
}
