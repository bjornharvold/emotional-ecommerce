/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Set;


/**
 * Created by Bjorn Harvold
 * Date: 6/13/11
 * Time: 2:56 PM
 * Responsibility:
 */
public interface UserSupplementRepository extends PagingAndSortingRepository<UserSupplement, ObjectId>, QueryDslPredicateExecutor<UserSupplement>, UserSupplementRepositoryCustom {

    @Query("{'cd' : ?0}")
    UserSupplement findUserSupplement(String userCode);

    @Query(value = "{ 'scls.providerId' : ?0, 'scls.providerUserId' : ?1 }", fields = "{ 'cd' : 1 }")
    List<UserSupplement> findUserIdsBySocialNetwork(String providerId, String providerUserId);

    @Query(value = "{ 'scls.providerId' : ?0, 'scls.providerUserId' : { $in : ?1 } }", fields = "{ 'cd' : 1 }")
    List<UserSupplement> findUserIdsConnectedToSocialNetwork(String providerId, Set<String> providerUserIds);

    @Query("{ 'scls.providerId': ?0 }")
    List<UserSupplement> findUsersBySocialNetwork(String providerId);

    @Query("{'cpns.cpncd' : ?0 }")
    UserSupplement findByCouponCode(String couponCode);

    @Query("{ 'vnts.rlnm' : ?0 }")
    List<UserSupplement> findByEventUrlName(String urlName);

    @Query("{ ssctns: { $elemMatch: { tp: 'AFFILIATE', attrs: { $elemMatch: { ky: 'ffltccntrlnm', vl: ?0  }  } } } }")
    List<UserSupplement> findByAffiliateAccountUrlName(String urlName);

    @Query(value = "{ ssctns: { $elemMatch: { tp: 'AFFILIATE', attrs: { $elemMatch: { ky: 'ffltccntrlnm', vl: ?0 }  } } } }", fields = "{ 'cd' : 1, 'lnm': 1, 'fnm': 1, 'ml': 1, 'cdt': 1 }")
    List<UserSupplement> findUserReportDataByAffiliateAccountUrlName(String urlName);

    @Query(value = "{ 'vnts.rlnm': ?0 }", fields = "{ 'cd' : 1, 'lnm': 1, 'fnm': 1, 'ml': 1, 'cdt': 1 }")
    List<UserSupplement> findUserReportDataByEventUrlName(String urlName);

    @Query("{ ssctns: { $elemMatch: { tp: 'AFFILIATE', attrs: { $elemMatch: { ky: 'cmpgnrlnm', vl: ?0  }  } } } }")
    List<UserSupplement> findByCampaignUrlName(String urlName);

    //@Query( value = "{ $and: [{ lrts: { $not: { $size: 0} }}, { lrts: { $exists: true } }]}", fields = "{ ml: 1, lrts: 1 }")
    @Query( value = "{ 'brds.crds.lrt': { $exists: true }}", fields = "{ cd: 1, brds: 1 }")
    List<UserSupplement> findUsersWithAlerts();

    @Query("{ 'cpns.ffrrlnm' : ?0 }")
    List<UserSupplement> findByOfferUrlName(String offerUrlName);

}
