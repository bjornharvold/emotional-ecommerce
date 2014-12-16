/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.commons.event.UpdateRemoteIPAddressEvent;
import com.lela.domain.document.Coupon;
import com.lela.domain.document.Event;
import com.lela.domain.document.FunctionalFilterPreset;
import com.lela.domain.document.Motivator;
import com.lela.domain.document.Reward;
import com.lela.domain.document.Social;
import com.lela.domain.document.User;
import com.lela.domain.document.UserAnswer;
import com.lela.domain.document.UserAssociation;
import com.lela.domain.document.UserEvent;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.Profile;
import com.lela.domain.dto.SubscribeToEmailList;
import com.lela.domain.dto.UnsubscribeFromEmailList;
import com.lela.domain.dto.UserAccountDto;
import com.lela.domain.dto.UserAttributes;
import com.lela.domain.dto.report.UserUserSupplementEntry;
import com.lela.domain.dto.user.Address;
import com.lela.domain.dto.user.ProfilePictureUpload;
import com.lela.domain.dto.user.UserSearchQuery;
import com.lela.domain.enums.Gender;
import com.lela.domain.enums.MotivatorSource;
import org.apache.poi.ss.usermodel.Workbook;
import org.bson.types.ObjectId;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * Created by Bjorn Harvold
 * Date: 6/19/11
 * Time: 1:14 PM
 * Responsibility:
 */
public interface UserService extends UserDetailsService {
    User findUser(ObjectId id);

    User findUser(ObjectId id, boolean enrich);

    User saveUser(User user);

    List<UserUserSupplementEntry> findUsers(Integer page, Integer maxResults, List<String> fields);

    User findUserByEmail(String email);

    Boolean isValid(String encryptedPassword, String rawPassword);

    Boolean resetPassword(String email);

    String encryptPassword(String password);

    void recomputeFriendshipLevel(String userCode);

    List<User> findUserIdsBySocialNetwork(String providerId, String providerUserId);

    List<User> findUserIdsConnectedToSocialNetwork(String providerId, Set<String> providerUserIds);

    List<User> saveUsers(List<User> list);

    void removeUser(User user);

    void removeUser(String userCode);

    List<String> motivatorLocalizedKeys(User user);

    Future<String> subscribeToEmailList(SubscribeToEmailList dto);

    Future<String> unsubscribeFromEmailList(UnsubscribeFromEmailList dto);

    void autoLogin(ObjectId userId, String remoteAddress);

    List<UserSupplement> findUsersByEventUrlName(String urlName);

    List<User> findUsersByCampaignUrlName(String urlName);

    List<User> findUsersByAffiliateAccountUrlName(String urlName);

    User updateUserAndRefreshSecurityContext(User user);

    List<UserUserSupplementEntry> findUsersByQuery(UserSearchQuery query);

    Long findFacebookUserCount(UserSearchQuery query);

    void verifyEmail(String tkn) throws IllegalAccessException;

    List<User> findUsersBySocialNetwork(String providerId);

    List<UserUserSupplementEntry> findUsersWithAlerts();

    void processUserAttributes(String userCode, UserAttributes userAttributes);

    Long findUserCount();

    List<UserUserSupplementEntry> findAllUsers(Integer chunk, List<String> fields);

    Workbook generateUserMotivatorReport();

    Long findFacebookUserCount();

    Long findUserCount(UserSearchQuery query);

    boolean shouldUserSeeRecommendedProducts(String userCode);

    Motivator findBestMotivator(String userCode);

    List<UserAnswer> saveUserAnswers(String userCode, List<UserAnswer> list);

    List<UserAnswer> findUserAnswers(String userCode);

    Motivator saveMotivator(String userCode, Motivator motivator);

    List<FunctionalFilterPreset> findFunctionalFilterPresets(String userCode);

    List<FunctionalFilterPreset> saveFunctionalFilterPresets(String userCode, List<FunctionalFilterPreset> filterPresets);

    List<UserAssociation> findUserAssociations(String userCode);

    List<UserAssociation> saveUserAssociations(String userCode, List<UserAssociation> associations);

    Long findUserCountByCouponCode(String couponCode);

    List<Coupon> findCoupons(String userCode);

    List<Coupon> saveCoupons(String userCode, List<Coupon> coupons);

    UserSupplement findUserSupplementByCouponCode(String couponCode);

    Coupon findCouponByCouponCode(String couponCode);

    List<Coupon> findCouponsByOfferUrlName(String offerUrlName);

    Long findUserByCouponOfferUrlNameCount(String offerUrlName);

    UserSupplement findUserSupplement(String userCode);

    UserSupplement saveUserSupplement(UserSupplement us);

    List<Social> findSocials(String userCode);

    List<Social> saveSocials(String userCode, List<Social> socials);

    Motivator findMotivator(String userCode, MotivatorSource type);

    User findUserByCode(String userCode);

    Gender saveGender(String userCode, Gender gender);

    Map<String, List<String>> findUserAttributes(String userCode);

    List<UserEvent> findUserEvents(String userCode);

    List<Reward> findRewards(String userCode);

    void removeUserSupplement(String userCode);

    List<UserUserSupplementEntry> findsUsersForCampaignReport(String campaignUrlName);

    UserSupplement findUserSupplementByEmail(String email);

    UserSupplement findUserSupplement(String userCode, List<String> fields);

    void updateProfilePicture(String userCode, ProfilePictureUpload picture);

    List<UserUserSupplementEntry> findsUsersForAffiliateReport(String affiliateUrlName);

    void updateProfile(String userCode, Profile profile);

    UserSupplement updateUserAccount(User user, UserAccountDto userAccountDto);

    List<User> findUsersCreatedBetween(Date startDate, Date endDate);

    List<UserUserSupplementEntry> findUsers(Integer page, Integer maxResults, List<String> fieldsOnUser, List<String> fieldsOnUserSupplement);

    Address addOrUpdateAddress(String userCode, Address address);

    void saveAge(String userCode, Integer age);

    List<UserUserSupplementEntry> findUsersForEventReport(String eventUrlName);

    UserEvent saveUserEvent(String userCode, Event event, String urlName, Map params) throws IllegalAccessException;
    
    void updateLastLoginRemoteAddress(UpdateRemoteIPAddressEvent uriae);
}
