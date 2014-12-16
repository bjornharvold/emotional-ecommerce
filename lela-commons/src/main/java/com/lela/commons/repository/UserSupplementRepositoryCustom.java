/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.user.UserSearchQuery;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 8/8/12
 * Time: 2:39 PM
 * Responsibility:
 */
public interface UserSupplementRepositoryCustom {
    Long findUserCountByCouponCode(String couponCode);

    List<UserSupplement> findUsersWithMotivators(Integer page, Integer maxResults, List<String> fields);

    Page<UserSupplement> findByAffiliateUrlName(String urlName, Integer page, Integer maxResults);

    Long findFacebookUserCount(List<String> userCodes);

    Long findByOfferUrlNameCount(String offerUrlName);

    List<UserSupplement> findUserSupplementsForCampaign(String campaignUrlName);

    UserSupplement findUserSupplement(String userCode, List<String> fields);

    List<UserSupplement> findByCodes(List<String> userCodes, List<String> fields);

    Long findUserSupplementCountByQuery(UserSearchQuery query);

    List<UserSupplement> findUserSupplementsByQuery(UserSearchQuery query);

    void trackAffiliateNotified(String userCode, Boolean notified);
}
