/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.ProductGrid;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductGridRepository
        extends PagingAndSortingRepository<ProductGrid, ObjectId>, QueryDslPredicateExecutor<ProductGrid>, ProductGridRepositoryCustom {

    @Query("{ 'rlnm' : ?0 }")
    ProductGrid findByUrlName(String urlName);
}
