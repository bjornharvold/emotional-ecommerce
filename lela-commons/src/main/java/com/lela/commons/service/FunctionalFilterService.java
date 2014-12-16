/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.domain.document.FunctionalFilter;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 6/4/12
 * Time: 8:06 PM
 * Responsibility:
 */
public interface FunctionalFilterService {
    FunctionalFilter saveFunctionalFilter(FunctionalFilter ff);
    List<FunctionalFilter> saveFunctionalFilters(List<FunctionalFilter> list);
    List<FunctionalFilter> findFunctionalFiltersByUrlName(String categoryUrlName);
    FunctionalFilter findFunctionalFilterByUrlNameAndKey(String categoryUrlName, String key);
    FunctionalFilter removeFunctionalFilter(ObjectId id);

    Long findFunctionalFilterCountByUrlName(String urlName);
}
