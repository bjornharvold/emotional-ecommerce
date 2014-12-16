/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.Blog;
import com.lela.domain.enums.PublishStatus;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created Chris Tallent
 */
public interface BlogRepository
        extends PagingAndSortingRepository<Blog, ObjectId>, QueryDslPredicateExecutor<Blog> {

    @Query("{ 'rlnm' : ?0, 'stts' : ?1 }")
    Blog findByUrlNameAndStatus(String urlName, PublishStatus status);

    @Query("{ 'rlnm' : ?0 }")
    Blog findByUrlName(String urlName);
}
