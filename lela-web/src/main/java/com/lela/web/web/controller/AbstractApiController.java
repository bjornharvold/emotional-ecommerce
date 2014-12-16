/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.web.web.controller;

import com.lela.commons.service.AffiliateService;
import com.lela.commons.service.ApplicationService;
import com.lela.commons.service.CampaignService;
import com.lela.commons.service.UserService;
import com.lela.commons.service.UserTrackerService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.utilities.UserSessionTrackingHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.Application;
import com.lela.domain.document.User;
import com.lela.domain.dto.ApiValidationResponse;
import com.lela.domain.dto.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by Bjorn Harvold
 * Date: 7/4/12
 * Time: 5:14 PM
 * Responsibility:
 */
public abstract class AbstractApiController extends AbstractController {
    private final static Logger log = LoggerFactory.getLogger(AbstractApiController.class);

    @Value("${google.analytics.enabled:false}")
    private boolean googleAnalyticsEnabled = false;

    @Value("${kissmetrics.enabled:false}")
    private boolean kmEnabled = false;

    @Value("${kissmetrics.key}")
    private String kmKey;

    protected final UserService userService;
    protected final AffiliateService affiliateService;
    protected final ApplicationService applicationService;
    protected final CampaignService campaignService;
    protected final UserTrackerService userTrackerService;
    protected final UserSessionTrackingHelper userSessionTrackingHelper;

    protected AbstractApiController(UserService userService,
                                    AffiliateService affiliateService,
                                    ApplicationService applicationService,
                                    CampaignService campaignService,
                                    UserTrackerService userTrackerService,
                                    UserSessionTrackingHelper userSessionTrackingHelper) {
        this.userService = userService;
        this.affiliateService = affiliateService;
        this.applicationService = applicationService;
        this.campaignService = campaignService;
        this.userTrackerService = userTrackerService;
        this.userSessionTrackingHelper = userSessionTrackingHelper;
    }

    protected boolean getAnalyticsEnabled() {
        return googleAnalyticsEnabled;
    }

    protected boolean getKmEnabled() {
        return kmEnabled;
    }

    protected String getKmKey() {
        return kmKey;
    }

    protected ApiValidationResponse validateRequest(String affiliateUrlName, String applicationUrlName) {
        ApiValidationResponse result = new ApiValidationResponse();

        // validating and loading up result object as we go
        AffiliateAccount affiliateAccount = affiliateService.findAffiliateAccountByUrlName(affiliateUrlName);

        // first we check that affiliate exists
        if (affiliateAccount != null) {
            result.setAffiliateAccount(affiliateAccount);

            // second we check that the affiliate has the specified application
            if (affiliateAccount.hasApplication(applicationUrlName)) {

                Application application = applicationService.findApplicationByUrlName(applicationUrlName);

                // then we check that the application exists and that it is published
                if (application != null) {
                    result.setApplication(application);

                    if (application.getPblshd()) {
                        result.setValid(true);
                    } else {
                        if (log.isWarnEnabled()) {
                            log.warn(String.format("Application: %s hasn't been published yet", applicationUrlName));
                        }
                    }
                } else {
                    if (log.isWarnEnabled()) {
                        log.warn(String.format("Application: %s doesn't exist", applicationUrlName));
                    }
                }
            }
        } else {
            if (log.isWarnEnabled()) {
                log.warn(String.format("AffiliateAccount: %s doesn't exist", affiliateUrlName));
            }
        }

        return result;
    }

    protected void handleAnalyticsCapture(HttpServletRequest request, HttpServletResponse response, String affiliateUrlName, String applicationUrlName, String campaignId) {
        HttpSession session = request.getSession();

        // Check for a user code in the request
        String requestUserCode = request.getParameter(WebConstants.USER_CODE_REQUEST_PARAM);
        String cookieUserCode = null;

        // Check for a user code cookie
        if ((requestUserCode == null || requestUserCode.length() == 0) && request.getCookies() != null) {
            for (int i=0; i<request.getCookies().length; i++) {
                Cookie cookie = request.getCookies()[i];
                if (cookie != null && WebConstants.USER_CODE_COOKIE.equals(cookie.getName())) {
                    cookieUserCode = cookie.getValue();
                    break;
                }
            }
        }

        String inboundUserCode = requestUserCode != null && requestUserCode.length() > 0 ? requestUserCode : cookieUserCode;

        // Is a user logged in?  If so, disregard any inbound user code and reset the cookie to match the principal
        User user = null;
        Principal principal = SpringSecurityHelper.getSecurityContextPrincipal();
        if (principal != null) {
            user = principal.getUser();
        }

        // No logged in user, Has the anonymous transient user already been created?
        // If so, and the inbound code is different, reset the user code to the inbound value
        boolean newTransientUserToTrack = false;
        if (user == null) {
            user = userSessionTrackingHelper.getSessionUser(session);

            // Is there an inbound user code that needs to override this user code
            if (user != null) {
                if ((inboundUserCode != null && inboundUserCode.length() > 0) && !inboundUserCode.equals(user.getCd())) {
                    // Update the transient user with the new user code
                    user.setCd(inboundUserCode);

                    // TODO:  IS THIS GOING TO DUPLICATE VISITS WHEN THE USER CODE GETS SWITCHED?
                    // MAYBE WE SHOULD JUST ASSUME THE EXISTING USER CODE IF ITS NOT FOR A LOGGED IN USER
                    newTransientUserToTrack = true;
                }
            }
        }

        // There is no logged in user and no anonymous user
        // Create a new anonymous user using the inbound code... if the code is null it will be ignored and a random value created
        if (user == null) {
            user = new User(inboundUserCode);
            newTransientUserToTrack = true;
        }

        // Ensure user tracker exists for the user
        session.setAttribute(WebConstants.SESSION_USER_CODE, userTrackerService.ensureUserTracker(user, request));

        // Process Affiliates BEFORE setting tracking the new user visit which fires a new UserVisit event to Mixpanel
        handleAffiliateTracking(user.getCd(), affiliateUrlName, campaignId);

        // If this is the first time we've seen this user session, track it as a new visit
        if (newTransientUserToTrack || session.getAttribute(WebConstants.SESSION_USER_CODE) == null) {
            userSessionTrackingHelper.setSessionUserAndTrackVisit(request, user);
        }

    }

    private void handleAffiliateTracking(String userCode, String affiliateId, String campaignId) {
        if (affiliateId != null || campaignId != null) {
            // Track the Affiliate and Campaign to the UserTracker
            affiliateService.trackAffiliateIdentifiers(userCode, affiliateId, campaignId, null);
        }
    }
}
