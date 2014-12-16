/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.AffiliateSale;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created Michael Ball
 */
public interface AffiliateSaleRepository
        extends PagingAndSortingRepository<AffiliateSale, ObjectId>, QueryDslPredicateExecutor<AffiliateSale> {
}
