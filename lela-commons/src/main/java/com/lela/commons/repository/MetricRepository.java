/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.Metric;
import com.lela.domain.enums.MetricType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Bjorn Harvold
 * Date: 6/13/11
 * Time: 2:56 PM
 * Responsibility:
 */
public interface MetricRepository extends PagingAndSortingRepository<Metric, ObjectId>, QueryDslPredicateExecutor<Metric> {
    @Query("{ 'tp' : ?0 }")
    Metric findByType(MetricType type);
}
