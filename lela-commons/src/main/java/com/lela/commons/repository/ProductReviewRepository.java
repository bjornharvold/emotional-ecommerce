package com.lela.commons.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.lela.domain.document.ProductReview;

public interface ProductReviewRepository extends PagingAndSortingRepository<ProductReview, ObjectId>, QueryDslPredicateExecutor<ProductReview>, ProductReviewRepositoryCustom {
    
    @Query("{ 'tmrlnm' : ?0 }")
    List<ProductReview> findByItemUrlName(String itemUrlName);
}
