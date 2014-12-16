/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.Press;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created Bjorn Harvold
 */
public interface PressRepository
        extends PagingAndSortingRepository<Press, ObjectId>, QueryDslPredicateExecutor<Press>, PressRepositoryCustom {

}
