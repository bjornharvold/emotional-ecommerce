/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.UserTracker;
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
public interface UserTrackerRepository extends PagingAndSortingRepository<UserTracker, ObjectId>, QueryDslPredicateExecutor<UserTracker>, UserTrackerRepositoryCustom {
    @Query("{ srcd: ?0 }")
    public UserTracker findByUserCode(String userCode);

    @Query(value = "{ 'rdrcts._id': ?0 }", fields = "{ 'srcd': 1, 'rdrcts': 1 }")
    public UserTracker findByRedirectId(ObjectId redirectId);

    @Query(value = "{ srcd: ?0 }", fields = "{ dmn: 1, dmnffltrlnm: 1, ffltccntrlnm: 1, cmpgnrlnm: 1, rfrrlnm: 1 }")
    public UserTracker findRegistrationAffiliateByUserCode(String userCode);
}
