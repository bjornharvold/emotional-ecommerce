package com.lela.commons.repository;

import com.lela.domain.document.UserVerification;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by IntelliJ IDEA.
 * User: Michael Ball
 * Date: 5/14/12
 * Time: 1:45 PM
 * To change this template use File | Settings | File Templates.
 */
public interface VerificationRepository extends PagingAndSortingRepository<UserVerification, ObjectId>, QueryDslPredicateExecutor<UserVerification> {
    @Query("{ 'srid' : ?0 }")
    UserVerification findByUserId(String userId);

    @Query("{ 'tkn' : ?0 }")
    UserVerification findByToken(String tkn);
}
