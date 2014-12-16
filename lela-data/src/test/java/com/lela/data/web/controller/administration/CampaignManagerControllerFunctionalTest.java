/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.data.web.controller.administration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;

import com.lela.commons.service.AffiliateService;
import com.lela.commons.service.CampaignService;
import com.lela.commons.service.LocalCacheEvictionService;
import com.lela.commons.service.UserService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.data.web.AbstractFunctionalTest;
import com.lela.data.web.controller.administration.affiliate.CampaignManagerController;
import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.Campaign;
import com.lela.domain.document.User;

public class CampaignManagerControllerFunctionalTest extends AbstractFunctionalTest {

    private static final Logger log = LoggerFactory.getLogger(CampaignManagerControllerFunctionalTest.class);
    private static final String EMAIL = "testuser4kijmkhj@yopmail.com";
    private static final String REDIRECT_URL = "redirect/url";
    private static final String AFFILIATE_NM = "affiliate";
    private static final String AFFILIATE_RL = "http://google.com";
    private static final String AFFILIATE_ACCOUNT_NM = "affilateaccount";
    private static final String CAMPAIGN_NM = "campaign";
    private static final String CAMPAIGN_REDIRECT = "/some/redirect";

    private AffiliateAccount account = null;
    private Campaign campaign = null;

    @Autowired
    private CampaignManagerController campaignManagerController;

    @Autowired
    private UserService userService;

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private AffiliateService affiliateService;

    @Autowired
    private LocalCacheEvictionService localCacheEvictionService;

    @Before
    public void setup() {

        SpringSecurityHelper.secureChannel();

        GregorianCalendar oneMonthAgo = new GregorianCalendar();
        oneMonthAgo.add(Calendar.MONTH, -1);

        GregorianCalendar oneMonthFromNow = new GregorianCalendar();
        oneMonthFromNow.add(Calendar.MONTH, 1);

        // Create an affiliate
        account = new AffiliateAccount();
        account.setId(new ObjectId());
        account.setNm(AFFILIATE_ACCOUNT_NM);
        account.setRlnm(AFFILIATE_ACCOUNT_NM);
        account.setCtv(true);

        affiliateService.saveAffiliateAccount(account);

        // Create a campaign
        campaign = new Campaign();
        campaign.setNm(CAMPAIGN_NM);
        campaign.setRlnm(CAMPAIGN_NM);
        campaign.setRdrctrl(CAMPAIGN_REDIRECT);
        campaign.setCtv(true);
        campaign.setStrtdt(oneMonthAgo.getTime());
        campaign.setNddt(oneMonthFromNow.getTime());

        campaignService.saveCampaign(campaign);

        SpringSecurityHelper.unsecureChannel();
    }

    @After
    public void tearDown() {
        User user = userService.findUserByEmail(EMAIL);

        if (user != null) {
            SpringSecurityHelper.secureChannel();
            userService.removeUser(user);
            SpringSecurityHelper.unsecureChannel();
        }

        SpringSecurityHelper.secureChannel();
        if (account != null) {
            affiliateService.removeAffiliateAccount(account.getRlnm());
            localCacheEvictionService.evictAffiliateAccount(account.getRlnm());
            account = null;
        }

        if (campaign != null) {
            campaignService.removeCampaign(campaign.getRlnm());
            localCacheEvictionService.evictCampaign(campaign.getRlnm());
            campaign = null;
        }

        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testCampaignDailyBreakdown() {

        log.info("Testing Campaign Daily Breakdown report...");
        SpringSecurityHelper.secureChannel();
        MockHttpServletResponse response = new MockHttpServletResponse();

        try {
            campaignManagerController.downloadCampaignBreakdown(CAMPAIGN_NM, response);
            assertNotNull("Response output stream is not null", response.getOutputStream());
        } catch (Exception e) {
            log.error("Caught exception in test", e);
            fail("Caught exception in test: " + e.getMessage());
        }
        SpringSecurityHelper.unsecureChannel();
    }
}
