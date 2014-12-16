/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.Question;
import com.lela.domain.document.Quiz;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 6/22/12
 * Time: 4:36 PM
 * Responsibility:
 */
public interface QuizRepositoryCustom {
    void setDefaultFlagOnCollection(String language, Boolean isDefault);
    List<Quiz> findAll(List<String> fields);
}
