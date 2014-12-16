/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.test.spring;

import com.lela.commons.repository.UserSupplementRepository;
import com.lela.commons.repository.UserTrackerRepository;
import com.lela.commons.service.AffiliateService;
import com.lela.commons.service.LocalCacheEvictionService;
import com.lela.commons.service.UserService;
import com.lela.commons.service.UserTrackerService;
import com.lela.commons.spring.analytics.UserSessionAndTrackingHandlerInterceptor;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.User;
import com.lela.domain.document.UserAssociation;
import com.lela.domain.document.UserAssociationAttribute;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.document.UserTracker;
import com.lela.domain.enums.AssociationType;
import com.lela.web.test.AbstractFunctionalTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.Cookie;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Testing For:
 *  - Is UserTracker db entry created?
 *  - Is UserTracker user code correct?
 *  - Is UserTracker user visit count correct?
 *  - Is UserTracker affiliate id correct?
 *  - Are UserTracker domain & domain affiliate id correct?
 *  - Is count of UserTracker entries correct?
 *  - Is UserSupplement db entry created?
 *  - Is UserSupplement user code correct?
 *  - Is UserSupplement user associate with affiliate correct?
 *
 * TEST CASES
 * These test cases represent visits to the webapp using a browser, not API visits
 *
 *  - First time visitor, anonymous, no cookie, no affiliate (anonymous Lela.com visitor)
 *  - First time visitor, anonymous, no cookie, affiliate (anonymous Affiliate banner visitor)
 *  - First time visitor, anonymous, no cookie, domain (anonymous Subdomain visitor)
 *  - First time visitor, anonymous, no cookie, domain, affiliate (anonymous Subdomain visitor with a different Affiliate banner)
 *
 *  - Return visitor, anonymous, session cookie, no affiliate (Second visit in the same session, no cookies)
 *  - Return visitor, anonymous, _lelacd cookie, no session cookie, no affiliate (Same visit, within 30 minutes)
 *  - Return visitor, anonymous, _lelacd cookie, no session cookie, no affiliate (Second visit, after session timeout)
 *  - Return visitor, anonymous, _lelacd cookie, same session cookie, no affiliate (Second visit on Lela.com, consistent user code)
 *  - Return visitor, anonymous, _lelacd request, no cookies, no affiliate (Following link to site from widget on third party site in Safari, third party cookies disabled)
 *  - Return visitor, anonymous, _lelacd cookie, no session cookie, no UserTracker (Return visitor encountering a UserTracker bug on prior visit)
 *
 *  - Return visitor, anonymous, session cookie, affiliate (Second Techlicious visit in the same session, no cookies)
 *  - Return visitor, anonymous, _lelacd cookie, no session cookie, affiliate (Second Techlicious visit, after session timeout)
 *  - Return visitor, anonymous, _lelacd cookie, same session cookie, affiliate (Second Techlicious visit on Lela.com, consistent user code)
 *  - Return visitor, anonymous, _lelacd request, no cookies, affiliate (Following link to site from widget on Techlicious third party site in Safari, third party cookies disabled)
 *
 *  - Return visitor, anonymous, session cookie, domain (Second BabyAge visit in the same session, no cookies)
 *  - Return visitor, anonymous, _lelacd cookie, no session cookie, domain (Second BabyAge visit, after session timeout)
 *  - Return visitor, anonymous, _lelacd cookie, same session cookie, domain (Second BabyAge visit on Lela.com, consistent user code)
 *
 *  - Return user, logged out, _lelacd cookie, no session cookie, no affiliate (Return user where logout did not clear the _lelacd cookie)
 *  - Return user, logged out, _lelacd cookie, no session cookie, affiliate (Return user where logout did not clear the _lelacd cookie, Affiliate tracked on visit but not user)
 *  - Return user, logged out, _lelacd cookie, no session cookie, domain (Return user where logout did not clear the _lelacd cookie, Domain tracked on visit but not user)
 *
 *  - Return user, logged in lela-security-cookie, _lelacd cookie, same session cookie, no affiliate (Remember me return visitor to Lela.com)
 *  - Return user, logged in lela-security-cookie, _lelacd cookie, no session cookie, no affiliate (Remember me return visitor to Lela.com after session timeout)
 *  - Return user, logged in lela-security-cookie, no _lelacd cookie, no session cookie, no affiliate (Remember me return visitor missing _lelacd cookie after session timeout)
 *
 *  - Return user, logged in lela-security-cookie, _lelacd cookie, same session cookie, affiliate (Remember me return visitor to Lela.com, Affiliate tracked on visit but not user)
 *  - Return user, logged in lela-security-cookie, _lelacd cookie, no session cookie, affiliate (Remember me return visitor to Lela.com after session timeout, Affiliate tracked on visit but not user)
 *  - Return user, logged in lela-security-cookie, no _lelacd cookie, no session cookie, affiliate (Remember me return visitor missing _lelacd cookie after session timeout, Affiliate tracked on visit but not user)
 *
 *  - Return user, logged in lela-security-cookie, _lelacd cookie, same session cookie, domain (Remember me return visitor to Lela.com, Domain tracked on visit but not user)
 *  - Return user, logged in lela-security-cookie, _lelacd cookie, no session cookie, domain (Remember me return visitor to Lela.com after session timeout, Domain tracked on visit but not user)
 *
 *  - Robot does not create UserTracker or UserSupplement or fire Events
 *
 *  Cases Not Covered
 *  - Login/Logout
 *  - Visit to affiliate landing page via AffiliateController after the interceptor
 *  - API related session/affiliate visits
 *
 * User: Chris Tallent
 * Date: 12/21/12
 * Time: 6:29 AM
 */
public class UserSessionAndTrackingHandlerInterceptorFunctionalTest extends AbstractFunctionalTest {
    private final static Logger log = LoggerFactory.getLogger(UserSessionAndTrackingHandlerInterceptorFunctionalTest.class);

    private final static String AFFILIATE_RLNM = "interceptor-affiliate-rlnm";
    private static final String AFFILIATE_NM = "interceptor-affiliate-nm";

    private final static String DOMAIN_AFFILIATE_RLNM = "interceptor-domain-rlnm";
    private final static String DOMAIN_AFFILIATE_NM = "interceptor-domain-nm";
    private static final String DOMAIN_AFFILIATE_URL = "test.domain.affiliate.com";

    private final static String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)";
    private final static String USER_CODE = "interceptor-functional-test";

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    UserService userService;

    @Autowired
    UserTrackerService userTrackerService;

    @Autowired
    UserTrackerRepository userTrackerRepository;

    @Autowired
    UserSupplementRepository userSupplementRepository;

    @Autowired
    AffiliateService affiliateService;

    @Autowired
    LocalCacheEvictionService localCacheEvictionService;

    @Autowired
    UserSessionAndTrackingHandlerInterceptor interceptor;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockHttpSession session;

    private ArrayList<String> userCodes = new ArrayList<String>();

    private AffiliateAccount affiliate;
    private AffiliateAccount domainAffiliate;

    @Before
    public void setup() {
        request = new MockHttpServletRequest();
        request.addHeader("User-Agent", USER_AGENT);
        request.setServerName("www.lela.com");

        session = new MockHttpSession();
        request.setSession(session);

        response = new MockHttpServletResponse();
    }

    @After
    public void teardown() {
        for (String userCode : userCodes) {
            log.info("Removing user: " + userCode + " from database");
            mongoTemplate.remove(query(where("cd").is(userCode)), User.class);
            mongoTemplate.remove(query(where("cd").is(userCode)), UserSupplement.class);
            mongoTemplate.remove(query(where("srcd").is(userCode)), UserTracker.class);

            localCacheEvictionService.evictUser(userCode);
            localCacheEvictionService.evictUserSupplement(userCode);
        }

        if (affiliate != null) {
            mongoTemplate.remove(query(where("rlnm").is(AFFILIATE_RLNM)), AffiliateAccount.class);
            localCacheEvictionService.evictAffiliateAccount(AFFILIATE_RLNM);
        }

        if (domainAffiliate != null) {
            mongoTemplate.remove(query(where("rlnm").is(DOMAIN_AFFILIATE_RLNM)), AffiliateAccount.class);
            localCacheEvictionService.evictAffiliateAccount(DOMAIN_AFFILIATE_RLNM);
        }
    }

    /**
     * - First time visitor, anonymous, no cookie, no affiliate (anonymous Lela.com visitor)
     */
    @Test
    public void newAnonymousSessionCreatesUserTracker() {
        try {
            interceptor.preHandle(request, response, new Object());

            User user = validateUser();

            UserSupplement us = userSupplementRepository.findUserSupplement(user.getCd());
            assertNull("User supplement should not be created", us);

            UserTracker ut = validateSingleUserVisit(user);
        } catch (Exception e) {
            fail("Exception thrown: " + e);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     *  - First time visitor, anonymous, no cookie, affiliate (anonymous Affiliate banner visitor)
     */
    @Test
    public void newAnonymousNoCookieAffiliate() {
        try {
            createAffiliate();
            request.addParameter(WebConstants.AFFILIATE_ID_REQUEST_PARAMETER, AFFILIATE_RLNM);

            // Call the interceptor
            interceptor.preHandle(request, response, new Object());

            // Validate results
            User user = validateUser();
            validateUserSupplementAffiliate(user, affiliate);

            UserTracker ut = validateSingleUserVisit(user);
            assertEquals("Affiliate not set on UserTracker", AFFILIATE_RLNM, ut.getFfltccntrlnm());
            assertEquals("Affiliate not set on UserVisit", AFFILIATE_RLNM, ut.getVsts().get(0).getFfltccntrlnm());
        } catch (Exception e) {
            fail("Exception thrown: " + e);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     *  - First time visitor, anonymous, no cookie, domain (anonymous Subdomain visitor)
     */
    @Test
    public void newAnonymousNoCookieDomain() {
        try {
            createDomainAffiliate();
            request.setServerName(DOMAIN_AFFILIATE_URL);

            // Call the interceptor
            interceptor.preHandle(request, response, new Object());

            // Validate results
            User user = validateUser();
            validateUserSupplementAffiliate(user, domainAffiliate);

            UserTracker ut = validateSingleUserVisit(user);
            assertEquals("Affiliate not set on UserTracker", DOMAIN_AFFILIATE_RLNM, ut.getFfltccntrlnm());
            assertEquals("Affiliate not set on UserVisit", DOMAIN_AFFILIATE_RLNM, ut.getVsts().get(0).getFfltccntrlnm());
            assertEquals("Domain Affiliate not set on UserTracker", DOMAIN_AFFILIATE_RLNM, ut.getDmnffltrlnm());
            assertEquals("Domain URL not set on UserTracker", DOMAIN_AFFILIATE_URL, ut.getDmn());
            assertEquals("Domain Affiliate not set on UserVisit", DOMAIN_AFFILIATE_RLNM, ut.getVsts().get(0).getDmnffltrlnm());
            assertEquals("Domain URL not set on UserVisit", DOMAIN_AFFILIATE_URL, ut.getVsts().get(0).getDmn());
        } catch (Exception e) {
            fail("Exception thrown: " + e);
            e.printStackTrace();
        }
    }

    /**
     *  - First time visitor, anonymous, no cookie, domain, affiliate (anonymous Subdomain visitor with a different Affiliate banner)
     */
    @Test
    public void newAnonymousNoCookieDomainAndAffiliate() {
        try {
            createAffiliate();
            createDomainAffiliate();
            request.setServerName(DOMAIN_AFFILIATE_URL);
            request.setParameter(WebConstants.AFFILIATE_ID_REQUEST_PARAMETER, AFFILIATE_RLNM);

            // Call the interceptor
            interceptor.preHandle(request, response, new Object());

            // Validate results
            User user = validateUser();
            validateUserSupplementAffiliate(user, affiliate);

            UserTracker ut = validateSingleUserVisit(user);
            assertEquals("Affiliate not set on UserTracker", AFFILIATE_RLNM, ut.getFfltccntrlnm());
            assertEquals("Affiliate not set on UserVisit", AFFILIATE_RLNM, ut.getVsts().get(0).getFfltccntrlnm());
            assertEquals("Domain Affiliate not set on UserTracker", DOMAIN_AFFILIATE_RLNM, ut.getDmnffltrlnm());
            assertEquals("Domain URL not set on UserTracker", DOMAIN_AFFILIATE_URL, ut.getDmn());
            assertEquals("Domain Affiliate not set on UserVisit", DOMAIN_AFFILIATE_RLNM, ut.getVsts().get(0).getDmnffltrlnm());
            assertEquals("Domain URL not set on UserVisit", DOMAIN_AFFILIATE_URL, ut.getVsts().get(0).getDmn());
        } catch (Exception e) {
            fail("Exception thrown: " + e);
            e.printStackTrace();
        }
    }

    /**
    *  - Return visitor, anonymous, session cookie, no affiliate (Second visit in the same session, no cookies)
    */
    @Test
    public void returnAnonymousSession() {
        try {
            // First Visit
            interceptor.preHandle(request, response, new Object());
            User original = validateUser();

            // Make a new request with the same session
            request = new MockHttpServletRequest();
            request.setSession(session);

            // Return Visit
            interceptor.preHandle(request, response, new Object());

            User returned = validateUser();

            assertEquals("Original and returned user are not the same in the same session", original, returned);

            UserSupplement us = userSupplementRepository.findUserSupplement(returned.getCd());
            assertNull("User supplement should not be created", us);

            validateSingleUserVisit(returned);
        } catch (Exception e) {
            fail("Exception thrown: " + e);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     *  - Return visitor, anonymous, _lelacd cookie, no session cookie, no affiliate (Same visit within 30 minutes)
     */
    @Test
    public void returnAnonymousLelacdCookieNewSession() {
        try {
            // First Visit
            interceptor.preHandle(request, response, new Object());
            User original = validateUser();

            // Make a new request with the same session and a _lelacd cookie
            request = new MockHttpServletRequest();
            session = new MockHttpSession();
            request.setSession(session);

            request.setCookies(new Cookie(WebConstants.USER_CODE_COOKIE, original.getCd()));

            // Return Visit
            interceptor.preHandle(request, response, new Object());

            User returned = validateUser();

            assertEquals("Original and returned user should have the same user code", original.getCd(), returned.getCd());

            UserSupplement us = userSupplementRepository.findUserSupplement(returned.getCd());
            assertNull("User supplement should not be created", us);

            validateSingleUserVisit(returned);
        } catch (Exception e) {
            fail("Exception thrown: " + e);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     *  - Return visitor, anonymous, _lelacd cookie, no session cookie, no affiliate (Second visit, after session timeout)
     */
    @Test
    public void returnVisitorAnonymousLelacdCookieCreatesTwoUserVisitsAfter30Minutes() {
        try {
//            // First Visit
//            interceptor.preHandle(request, response, new Object());
//            User original = validateUser();
//
//            // Make a new request with the same session and a _lelacd cookie
//            request = new MockHttpServletRequest();
//            session = new MockHttpSession();
//            request.setSession(session);
//
//            request.setCookies(new Cookie(WebConstants.USER_CODE_COOKIE, original.getCd()));
//
//            // Return Visit
//            interceptor.preHandle(request, response, new Object());
//
//            User returned = validateUser();
//
//            assertEquals("Original and returned user should have the same user code", original.getCd(), returned.getCd());
//
//            UserSupplement us = userSupplementRepository.findUserSupplement(returned.getCd());
//            assertNull("User supplement should not be created", us);
//
//            validateSingleUserVisit(returned);
        } catch (Exception e) {
            fail("Exception thrown: " + e);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     *  - Return visitor, anonymous, _lelacd cookie, same session cookie, no affiliate (Second visit on Lela.com, consistent user code)
     */
    @Test
    public void returnAnonymousLelacdCookieSameSession() {
        try {
            // First Visit
            interceptor.preHandle(request, response, new Object());
            User original = validateUser();

            // Make a new request with the same session and a _lelacd cookie
            request = new MockHttpServletRequest();
            request.setSession(session);

            request.setCookies(new Cookie(WebConstants.USER_CODE_COOKIE, original.getCd()));

            // Return Visit
            interceptor.preHandle(request, response, new Object());

            User returned = validateUser();

            assertEquals("Original and returned user should have the same user code", original.getCd(), returned.getCd());

            UserSupplement us = userSupplementRepository.findUserSupplement(returned.getCd());
            assertNull("User supplement should not be created", us);

            validateSingleUserVisit(returned);
        } catch (Exception e) {
            fail("Exception thrown: " + e);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     *  - Return visitor, anonymous, _lelacd request, no cookies, no affiliate (Following link to site from widget on third party site in Safari, third party cookies disabled)
     */
    @Test
    public void returnAnonymousLelacdRequestNewSession() {
        try {
            // First Visit
            interceptor.preHandle(request, response, new Object());
            User original = validateUser();

            // Make a new request with the same session and a _lelacd cookie
            request = new MockHttpServletRequest();
            request.setParameter(WebConstants.USER_CODE_REQUEST_PARAM, original.getCd());

            session = new MockHttpSession();
            request.setSession(session);

            // Return Visit
            interceptor.preHandle(request, response, new Object());

            User returned = validateUser();

            assertEquals("Original and returned user should have the same user code", original.getCd(), returned.getCd());

            UserSupplement us = userSupplementRepository.findUserSupplement(returned.getCd());
            assertNull("User supplement should not be created", us);

            validateSingleUserVisit(returned);
        } catch (Exception e) {
            fail("Exception thrown: " + e);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    private User validateUser() {
        User user = (User)session.getAttribute(WebConstants.USER);
        assertNotNull("User was not placed in session", user);
        userCodes.add(user.getCd());
        return user;
    }

    private UserTracker validateSingleUserVisit(User user) {
        UserTracker ut = userTrackerRepository.findByUserCode(user.getCd());
        assertNotNull("UserTracker was not created for user", ut);
        assertEquals("UserTracker has wrong user code", user.getCd(), ut.getSrcd());
        assertNotNull("UserVisit was not created", ut.getVsts());
        assertEquals("Only 1 UserVisit should exist", 1, ut.getVsts().size());
        assertEquals("User Visit count incorrect", (long)1, (long)ut.getVstcnt());
        return ut;
    }

    private void validateUserSupplementAffiliate(User user, AffiliateAccount affiliate) {
        UserSupplement us = userSupplementRepository.findUserSupplement(user.getCd());
        assertNotNull("User supplement should be created", us);
        assertNotNull("User supplement should have user associations", us.getSsctns());
        assertEquals("User supplement should have one affiliate", (long) 1, (long) us.getSsctns().size());

        UserAssociation assoc = us.getSsctns().get(0);
        assertEquals("Affiliate association incorrect type", AssociationType.AFFILIATE, assoc.getTp());
        assertEquals("Affiliate association incorrect name", affiliate.getNm(), assoc.getNm());
        assertNotNull("Affiliate association should have attributes", assoc.getAttrs());
        assertEquals("Affiliate association should have one attribute", (long) 1, (long) assoc.getAttrs().size());

        UserAssociationAttribute attr = assoc.getAttrs().get(0);
        assertEquals("Affiliate association attribute wrong key", "ffltccntrlnm", attr.getKy());
        assertEquals("Affiliate association attribute wrong value", affiliate.getRlnm(), attr.getVl());
    }

    private void createAffiliate() {
        affiliate = new AffiliateAccount();
        affiliate.setRlnm(AFFILIATE_RLNM);
        affiliate.setNm(AFFILIATE_NM);

        affiliate = affiliateService.saveAffiliateAccount(affiliate);
    }

    private void createDomainAffiliate() {
        domainAffiliate = new AffiliateAccount();
        domainAffiliate.setRlnm(DOMAIN_AFFILIATE_RLNM);
        domainAffiliate.setNm(DOMAIN_AFFILIATE_NM);
        domainAffiliate.setDmn(DOMAIN_AFFILIATE_URL);

        domainAffiliate = affiliateService.saveAffiliateAccount(domainAffiliate);
    }
}
