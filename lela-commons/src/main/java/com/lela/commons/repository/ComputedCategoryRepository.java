/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.Analytics;
import com.lela.domain.document.ComputedCategory;
import com.lela.domain.enums.AnalyticsSubType;
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
public interface ComputedCategoryRepository extends PagingAndSortingRepository<ComputedCategory, ObjectId>, QueryDslPredicateExecutor<ComputedCategory>, ComputedCategoryRepositoryCustom {

    @Query("{'cd' : ?0, 'rlnm' : ?1}")
    ComputedCategory findComputedCategory(String userCode, String categoryUrlName);

    @Query("{'cd' : ?0}")
    List<ComputedCategory> findComputedCategories(String userCode);
}
