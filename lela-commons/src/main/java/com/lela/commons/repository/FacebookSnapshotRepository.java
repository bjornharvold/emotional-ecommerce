/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.FacebookSnapshot;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface FacebookSnapshotRepository extends PagingAndSortingRepository<FacebookSnapshot, ObjectId>, QueryDslPredicateExecutor<FacebookSnapshot>, FacebookSnapshotRepositoryCustom {
    @Query("{ 'lelaEmail' : ?0 }")
    FacebookSnapshot findByLelaEmailAddress(String emailAddress);

    @Query("{ error: null, motivatorsDone: false }")
    List<FacebookSnapshot> findValidWithoutMotivators();
}
