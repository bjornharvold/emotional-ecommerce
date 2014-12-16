/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.Branch;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created Chris Tallent
 * Date: 11/9/11
 * Time: 2:56 PM
 * Responsibility:
 */
public interface BranchRepository
        extends PagingAndSortingRepository<Branch, ObjectId>, QueryDslPredicateExecutor<Branch>, BranchRepositoryCustom {
    @Query("{ 'rlnm' : ?0 }")
    Branch findByUrlName(String urlName);

    @Query("{ 'ffltrlnm' : ?0 }")
    List<Branch> findByAffiliateUrlName(String affiliateUrlName);

    @Query("{ 'lclcd' : ?0 }")
    Branch findByLocalCode(String localCode);

    @Query("{ 'mrchntd' : ?0 }")
    List<Branch> findByMerchantId(String merchantId);

    @Query("{ 'cty' : ?0 }")
    List<Branch> findByCity(String city);
}
