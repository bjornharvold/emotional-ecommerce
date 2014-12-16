/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.repository;

import com.lela.domain.document.RestartableProcessRecord;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * User: Chris Tallent
 * Date: 9/24/12
 * Time: 2:52 PM
 */
public interface RestartableProcessRecordRepository extends PagingAndSortingRepository<RestartableProcessRecord, ObjectId>, QueryDslPredicateExecutor<RestartableProcessRecord> {
    @Query(value = "{ prcss: ?0 }", fields = "{ ky: 1 }")
    public List<RestartableProcessRecord> findAllWithKey(String process);
}

