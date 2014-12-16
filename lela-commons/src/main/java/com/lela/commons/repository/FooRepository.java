/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.Foo;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Bjorn Harvold
 * Date: 7/18/11
 * Time: 12:00 PM
 * Responsibility:
 */
public interface FooRepository extends PagingAndSortingRepository<Foo, ObjectId>, QueryDslPredicateExecutor<Foo> {
    Page<Foo> findByOne(ObjectId fk1, Pageable pageable);
    Page<Foo> findByTwo(String fk2, Pageable pageable);
}
