/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.Analytics;
import com.lela.domain.document.Redirect;
import com.lela.domain.document.User;
import com.lela.domain.document.UserTracker;
import com.lela.domain.document.UserVisit;
import com.lela.domain.dto.AffiliateIdentifiers;
import com.lela.domain.dto.AffiliateTransaction;
import com.lela.domain.enums.AuthenticationType;
import com.lela.domain.enums.InteractionType;
import com.lela.domain.enums.RegistrationType;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Chris Tallent
 */
public interface UserTrackerService {
    String trackUserVisitStart(User user, HttpServletRequest request);
    String trackLogin(User user, String trackedUserCode, AuthenticationType authenticationType, HttpServletRequest request);
    boolean trackAffiliateIdentifiers(String userTrackerId, String affiliateUrlName, String campaignUrlName, String referrerAffiliateUrlName);
    void trackClick(String userTrackerId);
    void trackRegistrationComplete(String userTrackerId, RegistrationType registrationType);
    void trackRegistrationStart(String userTrackerId);
    String trackApiRegistration(User user, String transientUserCode, HttpServletRequest request);
    void trackQuizStart(String userTrackerId);
    void trackQuizComplete(String userTrackerId, String quizUrlName, String applicationUrlName, String affiliateUrlName, InteractionType quizType);
    Redirect trackRedirect(String userCode, Redirect redirect);
    boolean isRepeatUser(User user);
    UserVisit findFirstVisit(User user);
    void trackRedirectSale(AffiliateTransaction affiliateTransaction);
    void trackAnonymousSale(AffiliateTransaction affiliateTransaction);
    boolean isQuizComplete(String userCode);
    UserTracker findByRedirectId(String redirectId);
    AffiliateIdentifiers findAffiliateIdentifiers(String userCode);
    UserTracker findByUserCode(String userCode);
    boolean isFirstVisit(User user);

    boolean trackAffiliateIdentifiers(String userCode, AffiliateAccount domain, String affiliateUrlName, String campaignUrlName, String referrerAffiliateUrlName);

    String ensureUserTracker(User user, HttpServletRequest request);
}
