/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.repository;

import com.lela.domain.document.Blog;
import com.lela.domain.document.CssStyle;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * User: Chris Tallent
 * Date: 12/4/12
 * Time: 4:38 PM
 */
public interface CssStyleRepository extends PagingAndSortingRepository<CssStyle, ObjectId>, QueryDslPredicateExecutor<CssStyle> {
    @Query("{ 'rlnm' : ?0 }")
    CssStyle findByUrlName(String urlName);
}

