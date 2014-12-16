/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.Item;
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
public interface ItemRepository extends PagingAndSortingRepository<Item, ObjectId>, QueryDslPredicateExecutor<Item>, ItemRepositoryCustom {

    @Query("{ 'rlnm' : ?0 }")
    Item findByUrlName(String urlName);

    @Query("{ 'strs.rlnm' : ?0, 'ctgry.rlnm' : ?1 }")
    List<Item> findByStoreUrlNameAndCategoryUrlNames(String storeUrlName, String categoryUrlName);

    @Query("{ 'wnr.rlnm' : ?0, 'ctgry.rlnm' : ?1 }")
    List<Item> findByOwnerUrlNameAndCategoryUrlNames(String ownerUrlName, String categoryUrlName);

    @Query(value = "{ 'ctgry.rlnm' : ?0, 'vlbl' : false }", fields = "{ 'id' : 1 }")
    List<Item> findUnavailableItemIdsByCategoryUrlName(String categoryUrlName);

    @Query(value = "{ $and: [ { 'strs.tms.sbttrs': { $elemMatch: {'ky': ?0 ,'vl': ?1 } } }, {'ctgry.rlnm':{$in: ?2 } } ] }")
    List<Item> findItemsInCategory(String key, String value, List<String> categoryUrlNames);
}
