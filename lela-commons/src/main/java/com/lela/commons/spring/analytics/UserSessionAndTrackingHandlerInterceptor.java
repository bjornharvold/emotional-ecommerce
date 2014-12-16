package com.lela.commons.spring.analytics;

import com.lela.commons.event.EventHelper;
import com.lela.commons.event.PageViewEvent;
import com.lela.commons.service.AffiliateService;
import com.lela.commons.service.CampaignService;
import com.lela.commons.service.UserTrackerService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.utilities.UserSessionTrackingHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.Campaign;
import com.lela.domain.document.User;
import com.lela.domain.dto.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * User: Chris Tallent
 * Date: 8/1/12
 * Time: 6:56 AM
 */
public class UserSessionAndTrackingHandlerInterceptor extends HandlerInterceptorAdapter {
    private Logger log = LoggerFactory.getLogger(UserSessionAndTrackingHandlerInterceptor.class);

    private final UserSessionTrackingHelper userSessionTrackingHelper;
    private final UserTrackerService userTrackerService;
    private final AffiliateService affiliateService;
    private final CampaignService campaignService;
    private final String[] mappingExclusions;

    public UserSessionAndTrackingHandlerInterceptor(UserSessionTrackingHelper userSessionTrackingHelper,
                                                    UserTrackerService userTrackerService,
                                                    AffiliateService affiliateService,
                                                    CampaignService campaignService,
                                                    String[] mappingExclusions) {
        this.userSessionTrackingHelper = userSessionTrackingHelper;
        this.userTrackerService = userTrackerService;
        this.affiliateService = affiliateService;
        this.campaignService = campaignService;
        this.mappingExclusions = mappingExclusions;
    }

    /**
     * User Code handling precedence rules
     *      1 - Logged in user
     *      2 - Request parameter
     *      3 - Cookie
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Ignore excluded mapping paths
        // TODO - Spring MVC 3.2 will add exclusions at the <mvc:interceptors/> configuration level
        String uri = request.getRequestURI();
        if (mappingExclusions != null) {
            for (int i=0; i<mappingExclusions.length; i++) {
                if (uri != null && uri.startsWith(mappingExclusions[i])) {
                    return true;
                }
            }
        }

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
        handleAffiliateTracking(user.getCd(), request);

        // If this is the first time we've seen this user session, track it as a new visit
        if (newTransientUserToTrack || session.getAttribute(WebConstants.SESSION_USER_CODE) == null) {
            userSessionTrackingHelper.setSessionUserAndTrackVisit(request, user);
        }

        return true;
    }

    private void handleAffiliateTracking(String userCode, HttpServletRequest request) {
        String affiliateId = request.getParameter(WebConstants.AFFILIATE_ID_REQUEST_PARAMETER);
        String campaignId = request.getParameter(WebConstants.CAMPAIGN_ID_REQUEST_PARAMETER);
        String referringAffiliateId = request.getParameter(WebConstants.REFERRING_AFFILIATE_ID_REQUEST_PARAMETER);

        // Check for an affiliate based on domain... domain overrides request parameter
        String serverName = request.getServerName();
        AffiliateAccount domain = affiliateService.findAffiliateAccountByDomain(serverName);
        if (domain != null) {
            request.setAttribute(WebConstants.DOMAIN_AFFILIATE, domain);
        }

        if (affiliateId != null || campaignId != null || referringAffiliateId != null || domain != null) {
            if (affiliateId == null && campaignId != null) {
                log.debug("Campaign _cid in request: " + campaignId);
                Campaign campaign = campaignService.findActiveCampaignByUrlName(campaignId);
                if (campaign != null) {
                    affiliateId = campaign.getFfltrlnm();
                }
            }

            if (affiliateId != null) {
                log.debug("Affiliate _aid in request: " + affiliateId);
                AffiliateAccount affiliate = affiliateService.findActiveAffiliateAccountByUrlName(affiliateId);
                if (affiliate != null) {
                    placeBannerInSession(request, affiliate);
                } else {
                    affiliateId = null;
                }
            }

            // Track the Affiliate and Campaign to the UserTracker
            affiliateService.trackAffiliateIdentifiers(userCode, domain, affiliateId, campaignId, referringAffiliateId);
        }
    }

    private void placeBannerInSession(HttpServletRequest request, AffiliateAccount affiliate){
        if (affiliate.hasAssociatedBannerImage()){
            HttpSession session = request.getSession();
            session.setAttribute(WebConstants.BANNER_URL, affiliate.getBnrmgrl());
            session.setAttribute(WebConstants.BANNER_TARGET_URL, affiliate.getBnrrl());
            session.setAttribute(WebConstants.BANNER_ALT_TEXT, affiliate.getBnrlttxt());
            log.debug(String.format("Placed banner image at %s in session. URL pointing to is: %s", affiliate.getBnrmgrl(), affiliate.getBnrrl()));
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        Object untrackable = request.getAttribute(WebConstants.REQUEST_TRACKING_EXCLUDED);
        if(untrackable !=null && (Boolean)untrackable){
            return;
        }

        String contentType = response.getContentType();

        // Ignore excluded mapping paths
        // TODO - Spring MVC 3.2 will add exclusions at the <mvc:interceptors/> configuration level
        String uri = request.getRequestURI();
        if (mappingExclusions != null) {
            for (int i=0; i<mappingExclusions.length; i++) {
                if (uri != null && uri.startsWith(mappingExclusions[i])) {
                    request.setAttribute(WebConstants.REQUEST_TRACKING_EXCLUDED, Boolean.TRUE);
                    return;
                }
            }
        }

        request.setAttribute(WebConstants.REQUEST_TRACKING_EXCLUDED, Boolean.FALSE);

        // Set the User Code in a cookie
        String cookieUserCode = null;

        // Check for a user code cookie
        if (request != null && request.getCookies() != null) {
            for (int i=0; i<request.getCookies().length; i++) {
                Cookie cookie = request.getCookies()[i];
                if (cookie != null && WebConstants.USER_CODE_COOKIE.equals(cookie.getName())) {
                    cookieUserCode = cookie.getValue();
                    break;
                }
            }
        }

        User user = userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(request.getSession());
        if (!user.getCd().equals(cookieUserCode)) {
            Cookie cookie = new Cookie(WebConstants.USER_CODE_COOKIE, user.getCd());
            //cookie.setDomain(request.getServerName());
            cookie.setMaxAge(WebConstants.COOKIE_AGE_SECONDS);
            cookie.setPath(WebConstants.COOKIE_PATH);
            response.addCookie(cookie);
        }

        if (contentType != null && contentType.startsWith("text/html")) {
            HttpSession session = request.getSession();
            String userTrackerId = (String) session.getAttribute(WebConstants.SESSION_USER_CODE);
            if (userTrackerId != null) {
                userTrackerService.trackClick(userTrackerId);
                EventHelper.post(new PageViewEvent(user));
            }
        }
    }
}
