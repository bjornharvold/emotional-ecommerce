/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.OAuthClientDetail;
import org.bson.types.ObjectId;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Bjorn Harvold
 * Date: 6/13/11
 * Time: 2:56 PM
 * Responsibility:
 */
public interface OAuthClientDetailsRepository extends PagingAndSortingRepository<OAuthClientDetail, ObjectId>, QueryDslPredicateExecutor<OAuthClientDetail> {
    OAuthClientDetail findByClientId(String clientId);
}
