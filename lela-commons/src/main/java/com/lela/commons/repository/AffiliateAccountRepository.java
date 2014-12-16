/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.AffiliateAccount;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created Chris Tallent
 */
public interface AffiliateAccountRepository
        extends PagingAndSortingRepository<AffiliateAccount, ObjectId>, QueryDslPredicateExecutor<AffiliateAccount>, AffiliateAccountRepositoryCustom {

    @Query("{ 'rlnm' : ?0 }")
    AffiliateAccount findByUrlName(String urlName);

    @Query("{ 'dmn' : ?0 }")
    AffiliateAccount findByDomain(String domain);

    @Query("{$and: [{'dmnnvbr' : { $exists:true } }, {'dmnnvbr ': { $ne:'' } } ] } ")
    List<AffiliateAccount> findWithNavigationBar();
}
