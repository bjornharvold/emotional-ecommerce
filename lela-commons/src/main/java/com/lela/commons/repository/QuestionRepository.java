/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.Question;
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
public interface QuestionRepository extends PagingAndSortingRepository<Question, ObjectId>, QueryDslPredicateExecutor<Question>, QuestionRepositoryCustom {
    @Query("{ 'nm' : ?0 }")
    Question findByName(String name);

    @Query("{ 'tp' : ?0 }")
    List<Question> findByType(QuestionType type);

    @Query("{ 'rlnm' : ?0 }")
    Question findByUrlName(String urlName);
}
