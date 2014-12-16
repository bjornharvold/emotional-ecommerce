/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.Question;
import com.lela.domain.document.Quiz;
import com.lela.domain.enums.QuestionType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 6/13/11
 * Time: 2:56 PM
 * Responsibility:
 */
public interface QuizRepository extends PagingAndSortingRepository<Quiz, ObjectId>, QueryDslPredicateExecutor<Quiz>, QuizRepositoryCustom {
    @Query("{ 'rlnm' : ?0 }")
    Quiz findByUrlName(String urlName);


}
