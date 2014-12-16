/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.PostalCode;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created Chris Tallent
 * Date: 11/9/11
 * Time: 2:56 PM
 * Responsibility:
 */
public interface PostalCodeRepository extends PagingAndSortingRepository<PostalCode, ObjectId>, QueryDslPredicateExecutor<PostalCode> {
    @Query("{ 'cd' : ?0 }")
    PostalCode findByPostalCode(String postalCode);

    @Query("{ 'ct' : ?0, 'stnm' : ?1 }")
    PostalCode findByCityAndStateName(String city, String stateName);
}
