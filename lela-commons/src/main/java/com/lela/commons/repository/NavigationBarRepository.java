/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.NavigationBar;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created Bjorn Harvold
 */
public interface NavigationBarRepository
        extends PagingAndSortingRepository<NavigationBar, ObjectId>,
        QueryDslPredicateExecutor<NavigationBar>, NavigationBarRepositoryCustom {

    @Query("{ 'rlnm' : ?0 }")
    NavigationBar findByUrlName(String urlName);

    @Query("{ $or:[{'grps.chldrn.rlnm' : ?0}, {'grps.rlnm' : ?0}] }")
    NavigationBar findByGroupUrlName(String urlName);
}
