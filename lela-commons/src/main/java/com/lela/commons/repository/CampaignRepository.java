/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.Campaign;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created Chris Tallent
 */
public interface CampaignRepository
        extends PagingAndSortingRepository<Campaign, ObjectId>, QueryDslPredicateExecutor<Campaign> {

    @Query("{ 'rlnm' : ?0 }")
    Campaign findByUrlName(String urlName);
}
