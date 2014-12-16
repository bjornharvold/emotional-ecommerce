/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.Task;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

public interface TaskRepository
        extends PagingAndSortingRepository<Task, ObjectId>, QueryDslPredicateExecutor<Task>, TaskRepositoryCustom {

    @Query("{'rcpnt' : ?0}")
    List<Task> findTasksByRecipient(String recipientId);
}
