package com.lela.commons.repository;

import com.lela.domain.document.Offer;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Martin Gamboa
 * Date: 12/16/11
 * Time: 10:54 AM
 * To change this template use File | Settings | File Templates.
 */
public interface OfferRepository extends PagingAndSortingRepository<Offer, ObjectId>, QueryDslPredicateExecutor<Offer>, OfferRepositoryCustom {
    @Query("{ 'rlnm' : ?0 }")
    Offer findByUrlName(String urlName);

    @Query("{ 'brnchrlnm' : ?0 }")
    List<Offer> findByBranchUrlName(String branchUrlName);
}
