/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.Question;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 6/22/12
 * Time: 4:36 PM
 * Responsibility:
 */
public interface QuestionRepositoryCustom {
    List<Question> findAll(List<String> fields);
}
