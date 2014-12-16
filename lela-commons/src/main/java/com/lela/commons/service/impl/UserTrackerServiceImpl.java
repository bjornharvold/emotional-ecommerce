/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import com.lela.commons.event.EventHelper;
import com.lela.commons.event.PurchaseEvent;
import com.lela.commons.event.UserVisitEvent;
import com.lela.commons.repository.UserTrackerRepository;
import com.lela.commons.service.UserService;
import com.lela.commons.service.UserTrackerService;
import com.lela.commons.spring.mobile.WurflDevice;
import com.lela.domain.document.*;
import com.lela.domain.dto.AffiliateIdentifiers;
import com.lela.domain.dto.AffiliateTransaction;
import com.lela.domain.enums.*;
import com.lela.util.utilities.mixpanel.RequestUtils;
import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by Chris Tallent
 */
@Service("userTrackerService")
public class UserTrackerServiceImpl implements UserTrackerService {

    private static final Logger log = LoggerFactory.getLogger(UserTrackerServiceImpl.class);

    private static final int MINUTES_BETWEEN_VISITS = 30;

    private static final String USER_AGENT = "User-Agent";
    private static final String REFERRER = "Referer";

    @Autowired
    private UserTrackerRepository userTrackerRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MongoTemplate mongoTemplate;

    public UserTrackerServiceImpl() {    }

    /**
     * This method makes sure the user tracker exists for a user, but does not initiate a new UserVisit
     */
    @Override
    public String ensureUserTracker(User user, HttpServletRequest request) {
        if (allowUserAgent(request)) {
            boolean userTrackerExists = userTrackerRepository.existsForUser(user.getCd());
            if (!userTrackerExists) {
                UserTracker tracker = new UserTracker();
                tracker.setSrcd(user.getCd());
                userTrackerRepository.save(tracker);

                // Add the first user visit
                UserVisit visit = createVisit(request);
                addVisit(user, visit);
            }
        }

        return user.getCd();
    }

    /**
     * This method will track a new session and thus new visit
     */
    @Override
    public String trackUserVisitStart(User user, HttpServletRequest request) {
        ensureUserTracker(user, request);
        if (allowUserAgent(request)) {
            Date lastVisitDate = userTrackerRepository.findLastVisitDate(user.getCd());

            if (lastVisitDate == null || (lastVisitDate != null && Minutes.minutesBetween(new DateTime(lastVisitDate.getTime()), new DateTime()).getMinutes() > MINUTES_BETWEEN_VISITS)) {
                // Add a new UserVisit
                UserVisit visit = createVisit(request);
                addVisit(user, visit);
            }
        }

        return user.getCd();
    }

    private void addVisit(User user, UserVisit visit) {
        userTrackerRepository.addVisit(user.getCd(), visit);
        EventHelper.post(new UserVisitEvent(user, visit));
    }

    /**
     * Merge the two trackers if there are two and they are for different user codes
     * Create a tracker if the user doesn't have one and there is no transient... determine this by count
     * Update the tracker with the correct user code and user id if transient
     * <p/>
     * Scenarios
     * =========
     * - User logs in through Facebook
     * - User logs in using username / password
     * - User is logged in using Remember Me
     * - New User registers through Facebook and is auto-logged in
     * - New User registers with username / password and is auto-logged in
     * <p/>
     * In all scenarios the user might have been clicking through the site anonymously in the session
     * prior to the login or registration event and the anonymous clicks will be consolidated
     */
    @Override
    public String trackLogin(User user, String transientUserCode, AuthenticationType authenticationType, HttpServletRequest request) {
        String result = null;
        if (allowUserAgent(request)) {
            boolean existsForLoggedInUser = userTrackerRepository.existsForUser(user.getCd());
            if (existsForLoggedInUser && !user.getCd().equals(transientUserCode)) {
                if (transientUserCode != null) {
                    UserTracker transientTracker = userTrackerRepository.findByUserCode(transientUserCode);
                    if (transientTracker != null) {
                        // Merge the transient tracker data current UserVisit into the existing user data
                        // TODO - Should this perform consolidation in case of logoff/logon events
                        UserVisit visit = transientTracker.getVsts().get(transientTracker.getVsts().size() - 1);
                        visit.setLgntp(authenticationType);
                        addVisit(user, visit);

                        // Then remove the transient tracker and user supplement
                        userTrackerRepository.delete(transientTracker);
                        userService.removeUserSupplement(transientUserCode);
                    }
                    //else addVisit
                } else {
                    // Determine if a new visit should be created if there has been a period elapsed
                    // between remember me logins
                    Date lastVisitDate = userTrackerRepository.findLastVisitDate(user.getCd());
                    if (lastVisitDate != null && Minutes.minutesBetween(new DateTime(lastVisitDate.getTime()), new DateTime()).getMinutes() > MINUTES_BETWEEN_VISITS) {
                        // Add a new UserVisit
                        UserVisit visit = createVisit(request);
                        visit.setLgntp(authenticationType);
                        addVisit(user, visit);
                    } else {
                        // Set the auth type on the existing visit... this has to be done in separate queries since
                        // mongodb doesn't support self-referential queries
                        ObjectId visitId = userTrackerRepository.findCurrentVisit(user.getCd());
                        userTrackerRepository.setVisitAuthType(visitId, authenticationType);
                    }
                }
            } else if (transientUserCode != null) {
                // User the transient User Tracker for the logged in user
                // Use the transient visit
                userTrackerRepository.updateWithLoggedInUser(transientUserCode, user);

                // Set the auth type... this has to be done in separate queries since
                // mongodb doesn't support self-referential queries
                ObjectId visitId = userTrackerRepository.findCurrentVisit(user.getCd());
                userTrackerRepository.setVisitAuthType(visitId, authenticationType);
            } else {
                // Create a User Tracker entry now
                UserTracker tracker = new UserTracker();
                tracker.setSrcd(user.getCd());
                tracker.setSrid(user.getId());

                // Add a new UserVisit
                UserVisit visit = createVisit(request);
                visit.setLgntp(authenticationType);
                tracker.addVisit(visit);
                EventHelper.post(new UserVisitEvent(user, visit));

                userTrackerRepository.save(tracker);
            }

            result = user.getCd();
        }

        return result;
    }

    @Override
    @Async
    public void trackRegistrationStart(String userTrackerId) {
        if (userTrackerId != null) {
            userTrackerRepository.trackRegistrationStart(userTrackerId);
        }
    }

    @Override
    @Async
    public void trackRegistrationComplete(String userTrackerId, RegistrationType registrationType) {
        if (userTrackerId != null) {
            userTrackerRepository.trackRegistrationComplete(userTrackerId, registrationType);
        }
    }

    /**
     * For an API registration, there may not be a UserTracker in the session if the user is
     * using an off-site application widget.  If this is an API call from our site from the
     * Lela.com quiz, then an anonymous user tracker will exist
     *
     * @param user
     */
    @Override
    public String trackApiRegistration(User user, String transientUserCode, HttpServletRequest request) {

        String result = null;
        if (allowUserAgent(request)) {
            // Track the login... this will create a user tracker or use the existing anonymous
            result = trackLogin(user, transientUserCode, AuthenticationType.API, request);

            // Track the registration event
            trackRegistrationStart(result);

            // Track the registration complete
            trackRegistrationComplete(result, RegistrationType.API);
        }

        return result;
    }

    @Override
    @Async
    public void trackQuizStart(String userCode) {
        if (userCode != null) {
            userTrackerRepository.trackQuizStart(userCode);
        }
    }

    @Override
    @Async
    public void trackQuizComplete(String userCode, String quizUrlName, String applicationUrlName, String affiliateUrlName, InteractionType quizType) {
        if (userCode != null) {
            userTrackerRepository.trackQuizComplete(userCode, quizUrlName, applicationUrlName, affiliateUrlName, quizType);
        }
    }

    @Override
    @Async
    public void trackClick(String userTrackerId) {
        if (userTrackerId != null) {
            // Get the current visit
            ObjectId visitId = userTrackerRepository.findCurrentVisit(userTrackerId);
            if (visitId != null) {
                userTrackerRepository.trackClick(visitId);
            }
        }
    }

    @Override
    public boolean trackAffiliateIdentifiers(String userCode, String affiliateUrlName, String campaignUrlName, String referrerAffiliateUrlName) {
        return trackAffiliateIdentifiers(userCode, null, affiliateUrlName, campaignUrlName, referrerAffiliateUrlName);
    }

    @Override
    public boolean trackAffiliateIdentifiers(String userCode, AffiliateAccount domainAffiliate, String affiliateUrlName, String campaignUrlName, String referrerAffiliateUrlName) {
        boolean trackedRegistration = false;
        if (userCode != null) {
            User user = userService.findUserByCode(userCode);
            if (domainAffiliate != null) {
                if (affiliateUrlName == null) {
                    affiliateUrlName = domainAffiliate.getRlnm();
                }

                // If the user is already registered, we will not track the domain on the UserTracker, only UserVisit
                if (user == null) {
                    userTrackerRepository.updateRegistrationDomainAffiliate(userCode, domainAffiliate.getRlnm(), domainAffiliate.getDmn());
                }

                userTrackerRepository.updateVisitDomainAffiliate(userCode, domainAffiliate.getRlnm(), domainAffiliate.getDmn());
            }

            if (affiliateUrlName != null || campaignUrlName != null || referrerAffiliateUrlName != null) {
                // If the user is already registered, we will not track further registration affiliates
                if (user == null) {
                    trackedRegistration = userTrackerRepository.updateRegistrationAffiliateIdentifiers(userCode, affiliateUrlName, campaignUrlName, referrerAffiliateUrlName);
                }

                userTrackerRepository.updateVisitAffiliateIdentifiers(userCode, affiliateUrlName, campaignUrlName, referrerAffiliateUrlName);
            }
        }

        return trackedRegistration;
    }

    @Override
    public Redirect trackRedirect(String userCode, Redirect redirect) {
        if (userCode != null) {
            // Find the current visit
            ObjectId visitId = userTrackerRepository.findCurrentVisit(userCode);
            if (visitId == null) {
                throw new IllegalStateException("No user visit exists for user code: " + userCode);
            }

            // Add the visit id and ensure there is an object id
            if (redirect.getId() == null) {
                redirect.setId(new ObjectId());
            }

            redirect.setVstd(visitId);

            userTrackerRepository.addRedirect(userCode, redirect);
        }

        return redirect;
    }

    @Override
    public boolean isFirstVisit(User user) {
        boolean result = true;

        if (user.getCd() != null) {
            result = userTrackerRepository.findVisitCount(user.getCd()) <= 1;
        }

        return result;
    }

    @Override
    public boolean isRepeatUser(User user) {
        boolean result = false;

        if (user.getId() != null) {
            // greater than 1 because the first gets fired off immediately after manual login
            result = userTrackerRepository.findVisitCount(user.getCd()) > 1;
        }

        return result;
    }

    @Override
    public boolean isQuizComplete(String userCode) {
        boolean result = false;

        UserTracker tracker = userTrackerRepository.findUserTracker(userCode, new String[]{"qzcmpltdt"});
        if (tracker != null) {
            result = tracker.getQzcmpltdt() != null;
        }

        return result;
    }

    @Override
    public UserVisit findFirstVisit(User user) {
        UserVisit result = null;
        if (user.getCd() != null) {
            result = userTrackerRepository.findFirstVisit(user.getCd());
        }

        return result;
    }

    private UserVisit createVisit(HttpServletRequest request) {
        UserVisit visit = new UserVisit();

        if (request != null) {
            Device device = DeviceUtils.getCurrentDevice(request);
            if (device != null) {
                if (device.isNormal()) {
                    visit.setDvctp(DeviceType.NORMAL);
                } else if (device.isMobile()) {
                    visit.setDvctp(DeviceType.MOBILE);
                } else if (device.isTablet()) {
                    visit.setDvctp(DeviceType.TABLET);
                }

                if (device instanceof WurflDevice) {
                    WurflDevice wurflDevice = (WurflDevice) device;
                    visit.setWrfld(wurflDevice.getDeviceRootId());
                }
            } else {
                visit.setDvctp(DeviceType.NORMAL);
            }

            visit.setSrgnt(request.getHeader(USER_AGENT));
            visit.setRfr(request.getHeader(REFERRER));
        }

        return visit;
    }

    @Override
    public void trackRedirectSale(AffiliateTransaction affiliateTransaction) {
        Query query = query(where("rdrcts._id").is(affiliateTransaction.getRedirect().getId()));
        for (Sale sale : affiliateTransaction.getRedirect().getSls()) {
            Update update = new Update().push("rdrcts.$.sls", sale).inc("rdrcts.$.ttlsls", sale.getSlmnt()).inc("rdrcts.$.ttlcmmssn", sale.getCmmssnmnt());
            WriteResult result = mongoTemplate.updateFirst(query, update, UserTracker.class);
            log.info(result.toString());

            // Track KissMetrics
            //get the user code
            UserTracker userTracker = userTrackerRepository.findByRedirectId(affiliateTransaction.getRedirect().getId());

            if(userTracker!=null)
            {
                User user = userService.findUserByCode(userTracker.getSrcd());
                EventHelper.post(new PurchaseEvent(user, userTracker, sale));
            }
            else
            {
                EventHelper.post(new PurchaseEvent(null, userTracker, sale));
            }
        }
    }

    @Override
    public void trackAnonymousSale(AffiliateTransaction affiliateTransaction) {
        // Track KissMetrics
        for (Sale sale : affiliateTransaction.getRedirect().getSls()) {
          EventHelper.post(new PurchaseEvent(null, null, sale));
        }
    }

    @Override
    public AffiliateIdentifiers findAffiliateIdentifiers(String userCode) {
        AffiliateIdentifiers result = new AffiliateIdentifiers();

        UserTracker ut = userTrackerRepository.findRegistrationAffiliateByUserCode(userCode);
        if (ut != null) {
            if (ut.getDmn() != null && ut.getDmn().length() > 0) {
                result.setDmn(ut.getDmn());
            }

            if (ut.getDmnffltrlnm() != null && ut.getDmnffltrlnm().length() > 0) {
                result.setDmnffltrlnm(ut.getDmnffltrlnm());
            }

            if (ut.getFfltccntrlnm() != null && ut.getFfltccntrlnm().length() > 0) {
                result.setFfltccntrlnm(ut.getFfltccntrlnm());
            }

            if (ut.getCmpgnrlnm() != null && ut.getCmpgnrlnm().length() > 0) {
                result.setCmpgnrlnm(ut.getCmpgnrlnm());
            }

            if (ut.getRfrrlnm() != null && ut.getRfrrlnm().length() > 0) {
                result.setRfrrlnm(ut.getRfrrlnm());
            }
        }

        return result;
    }

    @Override
    public UserTracker findByUserCode(String userCode) {
        return userTrackerRepository.findByUserCode(userCode);
    }

    public UserTracker findByRedirectId(String redirectId)
    {
        return userTrackerRepository.findByRedirectId(new ObjectId(redirectId));
    }

    private boolean allowUserAgent(HttpServletRequest request) {
        boolean result = true;
        if (request != null) {
            result = !RequestUtils.isBot(request);
        }

        return result;
    }


}
