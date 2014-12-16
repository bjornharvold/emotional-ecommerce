package com.lela.commons.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.lela.domain.document.CronTrigger;

public interface TriggerRepository extends PagingAndSortingRepository<CronTrigger, ObjectId>, QueryDslPredicateExecutor<CronTrigger>{
    @Query("{ 'rlnm' : ?0 }")
    CronTrigger findByUrlName(String urlName);
}
