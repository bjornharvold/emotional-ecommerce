/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.test.service;

import com.jeraff.kissmetrics.client.KissMetricsClient;
import com.jeraff.kissmetrics.client.KissMetricsProperties;
import com.lela.commons.exception.KissMetricsRuntimeException;
import com.lela.commons.repository.UserTrackerRepository;
import com.lela.commons.service.EventService;
import com.lela.commons.service.ProfileService;
import com.lela.commons.service.UserTrackerService;
import com.lela.commons.service.impl.UserTrackerServiceImpl;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.test.AbstractFunctionalTest;
import com.lela.domain.document.*;
import com.lela.domain.dto.AffiliateTransaction;
import com.lela.domain.enums.AuthenticationType;
import com.lela.domain.enums.EventType;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Created by Mike Ball
 * Date: 6/23/11
 * Time: 11:16 AM
 * Responsibility:
 */
public class UserTrackerServiceFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(UserTrackerServiceFunctionalTest.class);
    private static final String USER_TRACKER_SERVICE_FUNCTIONAL_TEST = "UserTrackerServiceFunctionalTest";

    @Autowired
    private UserTrackerService userTrackerService;

    @Autowired
    private UserTrackerRepository userTrackerRepository;

    @Autowired
    private ProfileService profileService;

    private User user;

    @Before
    public void setUp() {
        user = createRandomUser(true);
        user = profileService.registerTestUser(user);
    }

    @Test
    public void testRedirectSaleTracking() {
        SpringSecurityHelper.unsecureChannel();

        log.info("Create a userTracker and save it");

        String userCode = null;

        log.info("Securing channel...");
        SpringSecurityHelper.secureChannel();
        log.info("Channel secured");

        try {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_2) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.91 Safari/537.11");

            userCode = userTrackerService.trackLogin(user, null, AuthenticationType.FACEBOOK, request);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

        log.info("Saving a Redirect.");
        Redirect redirect = new Redirect();
        try {
            userTrackerService.trackRedirect(userCode, redirect);
            assertNotNull("redirect is missing an id", redirect.getId());
            log.info("Redirect persisted successfully");
        } catch (Exception ex) {
            fail("Did not expect an exception here: " + ex.getMessage());
            log.info("Was not able to persist a redirect within secure context", ex);
        }

        log.info("Saving redirect Sale...");
        Redirect saleRedirect = new Redirect();
        saleRedirect.setId(redirect.getId());
        Sale sale = new Sale();
        sale.setCmmssnmnt(1.0d);
        sale.setSlmnt(10.0d);
        saleRedirect.getSls().add(sale);

        AffiliateTransaction affiliateTransaction = new AffiliateTransaction();
        affiliateTransaction.setRedirect(saleRedirect);

        userTrackerService.trackRedirectSale(affiliateTransaction);

        UserTracker userTracker = userTrackerRepository.findByRedirectId(redirect.getId());
        System.out.println(userTracker.getId());
        assertEquals("There should be one sale.", 1, userTracker.getRdrcts().get(0).getSls().size());
        assertEquals("The commission value should be set.", Double.valueOf(1.0d), userTracker.getRdrcts().get(0).getSls().get(0).getCmmssnmnt());
        assertEquals("The sale amount should be set.", Double.valueOf(10.0d), userTracker.getRdrcts().get(0).getSls().get(0).getSlmnt());
        assertEquals("The total sale amount should be set.", Double.valueOf(10.0d), userTracker.getRdrcts().get(0).getTtlsls());
        assertEquals("The total commission amount should be set.", Double.valueOf(1.0d), userTracker.getRdrcts().get(0).getTtlcmmssn());

        saleRedirect = new Redirect();
        saleRedirect.setId(redirect.getId());
        sale = new Sale();
        sale.setCmmssnmnt(1.13d);
        sale.setSlmnt(10.33d);
        saleRedirect.getSls().add(sale);

        affiliateTransaction = new AffiliateTransaction();
        affiliateTransaction.setRedirect(saleRedirect);

        userTrackerService.trackRedirectSale(affiliateTransaction);

        userTracker = userTrackerRepository.findByRedirectId(redirect.getId());
        assertEquals("There should be two sales.", 2, userTracker.getRdrcts().get(0).getSls().size());
        assertEquals("The commission value should be set.", Double.valueOf(1.13d), userTracker.getRdrcts().get(0).getSls().get(1).getCmmssnmnt());
        assertEquals("The sale amount should be set.", Double.valueOf(10.33d), userTracker.getRdrcts().get(0).getSls().get(1).getSlmnt());
        assertEquals("The total sale amount should be set.", Double.valueOf(20.33d), userTracker.getRdrcts().get(0).getTtlsls());
        assertEquals("The total commission amount should be set.", Double.valueOf(2.13d), userTracker.getRdrcts().get(0).getTtlcmmssn());
        log.info("Test complete!");
    }




}
