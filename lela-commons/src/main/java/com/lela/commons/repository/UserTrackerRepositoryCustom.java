/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.commons.repository;

import com.lela.domain.document.Redirect;
import com.lela.domain.document.User;
import com.lela.domain.document.UserTracker;
import com.lela.domain.document.UserVisit;
import com.lela.domain.enums.AnalyticsSubType;
import com.lela.domain.enums.AuthenticationType;
import com.lela.domain.enums.InteractionType;
import com.lela.domain.enums.RegistrationType;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

/**
 * User: Chris Tallent
 * Date: 6/6/12
 * Time: 9:59 AM
 */
public interface UserTrackerRepositoryCustom {
    boolean updateRegistrationAffiliateIdentifiers(String userCode, String affiliateUrlName, String campaignUrlName, String referrerAffiliateUrlName);
    void updateWithLoggedInUser(String trackerUserCode, User user);
    boolean existsForUser(String userCode);
    void addVisit(String userCode, UserVisit visit);
    ObjectId findCurrentVisit(String userCode);
    void trackClick(ObjectId visitId);
    Date findLastVisitDate(String userCode);
    void setVisitAuthType(ObjectId visitId, AuthenticationType authenticationType);
    void trackRegistrationComplete(String userCode, RegistrationType registrationType);
    void trackRegistrationStart(String userTrackerId);
    void trackQuizComplete(String userCode, String quizUrlName, String applicationUrlName, String affiliateUrlName, InteractionType quizType);
    void trackQuizStart(String userCode);
    void addRedirect(String userCode, Redirect redirect);
    int findVisitCount(String userCode);
    UserVisit findFirstVisit(String userCode);
    UserTracker findUserTracker(String userCode, String[] fields);
    void updateVisitAffiliateIdentifiers(String userCode, String affiliateUrlName, String campaignUrlName, String referrerAffiliateUrlName);

    boolean updateRegistrationDomainAffiliate(String userCode, String domainAffiliateUrlName, String domain);

    void updateVisitDomainAffiliate(String userCode, String domainAffiliateUrlName, String domain);
}
