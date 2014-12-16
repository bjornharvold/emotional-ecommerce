/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.commons.repository;

import com.lela.domain.document.JobExecution;
import org.bson.types.ObjectId;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * User: Chris Tallent
 * Date: 8/20/12
 * Time: 2:31 PM
 */
public interface JobExecutionRepository extends PagingAndSortingRepository<JobExecution, ObjectId>, QueryDslPredicateExecutor<JobExecution>, JobExecutionRepositoryCustom {
}

