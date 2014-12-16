/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.commons.event.UpdateZipEvent;
import com.lela.domain.document.Coupon;
import com.lela.domain.document.Event;
import com.lela.domain.document.FunctionalFilterPreset;
import com.lela.domain.document.ListCard;
import com.lela.domain.document.Motivator;
import com.lela.domain.document.Social;
import com.lela.domain.document.UserAnswer;
import com.lela.domain.document.UserAssociation;
import com.lela.domain.dto.UserAttributes;
import com.lela.domain.document.UserEvent;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.user.Address;
import com.lela.domain.dto.user.UserSearchQuery;
import com.lela.domain.enums.Gender;
import com.lela.domain.enums.MotivatorSource;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Bjorn Harvold
 * Date: 8/4/12
 * Time: 10:19 PM
 * Responsibility:
 */
public interface UserSupplementService {
    Motivator findBestMotivator(String userCode);

    Motivator findMotivator(String userCode, MotivatorSource tp);

    void removeMotivator(String userCode, MotivatorSource tp);

    Motivator saveMotivator(String userCode, Motivator motivator);

    void processUserAttributes(String userCode, UserAttributes userAttributes);

    boolean shouldUserSeeRecommendedProducts(String userCode);

    void recomputeFriendshipLevel(String userCode);

    List<String> motivatorLocalizedKeys(String userCode);

    UserEvent saveUserEvent(String userCode, Event event, String urlName, Map params) throws IllegalAccessException;

    List<UserSupplement> findUsersWithMotivators(Integer page, Integer maxResults, List<String> fields);

    Long findFacebookUserCount(List<String> userCodes);

    List<UserSupplement> findUserIdsBySocialNetwork(String providerId, String providerUserId);

    List<UserSupplement> findUserIdsConnectedToSocialNetwork(String providerId, Set<String> providerUserIds);

    List<UserSupplement> findUsersBySocialNetwork(String providerId);

    void removeUserSupplement(String userCode);

    void removeUserSupplement(UserSupplement us);

    List<UserSupplement> findUsersWithAlerts();

    List<UserSupplement> findByEventUrlName(String urlName);

    List<UserSupplement> findByCampaignUrlName(String urlName);

    List<UserSupplement> findByAffiliateAccountUrlName(String urlName);

    UserSupplement findUserSupplement(String userCode);

    List<UserAnswer> saveUserAnswers(String userCode, List<UserAnswer> list);

    List<UserAnswer> findUserAnswers(String userCode);

    List<FunctionalFilterPreset> findFunctionalFilterPresets(String userCode);

    List<FunctionalFilterPreset> saveFunctionalFilterPresets(String userCode, List<FunctionalFilterPreset> filterPresets);

    List<UserAssociation> findUserAssociations(String userCode);

    List<UserAssociation> saveUserAssociations(String userCode, List<UserAssociation> userAssociations);

    Long findUserCountByCouponCode(String couponCode);

    List<Coupon> findCoupons(String userCode);

    List<Coupon> saveCoupons(String userCode, List<Coupon> coupons);

    UserSupplement findUserSupplementByCouponCode(String couponCode);

    Coupon findCouponByCouponCode(String couponCode);

    List<Coupon> findCouponsByOfferUrlName(String offerUrlName);

    Long findUserSupplementByOfferUrlNameCount(String offerUrlName);

    UserSupplement saveUserSupplement(UserSupplement us);

    List<Social> findSocials(String userCode);

    List<Social> saveSocials(String userCode, List<Social> socials);

    Map<String, List<String>> findUserAttributes(String userCode);

    List<UserSupplement> findUserSupplementsForCampaignReport(String campaignUrlName);

    UserSupplement findUserSupplement(String userCode, List<String> fields);
    List<UserSupplement> findUserSupplementsForAffiliateReport(String affiliateUrlName);

    UserSupplement findUserSupplementByEmail(String email);

    List<UserSupplement> findUserSupplementsByCodes(List<String> userCodes, List<String> fields);

    List<UserSupplement> findUserSupplementsByQuery(UserSearchQuery query);

    Long findUserSupplementCountByQuery(UserSearchQuery query);

    Address addOrUpdateAddress(String userCode, Address address);

    void saveAge(String userCode, Integer age);

    void saveGender(String userCode, Gender gender);

    List<UserSupplement> findUserSupplementsForEventReport(String eventUrlName);
    
    void updateCurrentZip(UpdateZipEvent uze);

    void trackAffiliateNotified(String userCode, Boolean notified);
}
