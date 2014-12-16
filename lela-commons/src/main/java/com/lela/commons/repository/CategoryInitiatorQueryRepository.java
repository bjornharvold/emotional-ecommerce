package com.lela.commons.repository;

import com.lela.domain.document.CategoryInitiatorQuery;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 11/2/12
 * Time: 8:59 AM
 * To change this template use File | Settings | File Templates.
 */
public interface CategoryInitiatorQueryRepository extends PagingAndSortingRepository<CategoryInitiatorQuery, ObjectId>, QueryDslPredicateExecutor<CategoryInitiatorQuery>{
    @Query("{ 'srid' : ?0 }")
    List<CategoryInitiatorQuery> findByUserCode(ObjectId srid);
}
