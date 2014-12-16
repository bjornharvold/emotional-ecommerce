/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.FunctionalFilter;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 6/13/11
 * Time: 2:56 PM
 * Responsibility:
 */
public interface FunctionalFilterRepository extends PagingAndSortingRepository<FunctionalFilter, ObjectId>, QueryDslPredicateExecutor<FunctionalFilter> {
    List<FunctionalFilter> findByRlnm(String urlName, Sort sort);
}
