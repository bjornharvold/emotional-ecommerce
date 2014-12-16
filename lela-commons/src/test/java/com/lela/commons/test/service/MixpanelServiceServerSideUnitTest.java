package com.lela.commons.test.service;

import com.google.common.collect.Lists;
import com.lela.commons.event.*;
import com.lela.commons.service.*;
import com.lela.commons.service.impl.MixpanelServiceImpl;
import com.lela.domain.document.*;
import com.lela.domain.dto.AffiliateIdentifiers;
import com.lela.domain.dto.RelevantItemQuery;
import com.lela.domain.dto.quiz.QuizAnswer;
import com.lela.domain.dto.quiz.QuizAnswers;
import com.lela.domain.dto.user.Address;
import com.lela.domain.enums.*;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import com.mixpanel.java.mpmetrics.MPConfig;
import com.mixpanel.java.mpmetrics.MPMetrics;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

import static junit.framework.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 11/5/12
 * Time: 12:24 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(MPMetrics.class)
public class MixpanelServiceServerSideUnitTest {


    MixpanelService mixpanelService = new MixpanelServiceImpl();

    private User user;
    private UserSupplement userSupplement;
    private AffiliateIdentifiers affiliateIdentifiers;

    @Mock MPMetrics mpMetrics;

    @Mock UserService userService;

    @Mock UserTrackerService userTrackerService;

    @Mock
    private ProductEngineService productEngineService;

    @Mock
    private ItemService itemService;

    @Mock
    private CategoryService categoryService;

    @Mock
    LookupService lookupService;

    @Before
    public void setUp() {
        user = new User();
        user.setCd("user-code");

        userSupplement = new UserSupplement();
        userSupplement.setFnm("Jimmy");
        userSupplement.setLnm("Page");
        userSupplement.setMl("jimmy@ledzeppelin.edu");
        userSupplement.setGndr(Gender.MALE);


        affiliateIdentifiers = new AffiliateIdentifiers();
        affiliateIdentifiers.setFfltccntrlnm("affiliateUrlName");
        affiliateIdentifiers.setCmpgnrlnm("campaignUrlName");
        affiliateIdentifiers.setRfrrlnm("referrerUrlName");

        PowerMockito.mockStatic(MPMetrics.class);

        MPConfig mpConfig = new MPConfig(null, null, null);

        Mockito.when(MPMetrics.getInstance(mpConfig)).thenReturn(mpMetrics);
        Mockito.when(MPMetrics.getInstance(eq(mpConfig), anyBoolean())).thenReturn(mpMetrics);

        ReflectionTestUtils.setField(mixpanelService, "enabled", Boolean.TRUE);
        ReflectionTestUtils.setField(mixpanelService, "userService", userService);
        ReflectionTestUtils.setField(mixpanelService, "userTrackerService", userTrackerService);
        ReflectionTestUtils.setField(mixpanelService, "productEngineService", productEngineService);
        ReflectionTestUtils.setField(mixpanelService, "itemService", itemService);
        ReflectionTestUtils.setField(mixpanelService, "categoryService", categoryService);
        ReflectionTestUtils.setField(mixpanelService, "mpConfig", mpConfig);
    }



    @Test
    public void sale()
    {
        Sale sale = new Sale();
        sale.setAdvrtsrNm("Advertiser");
        sale.setCmmssnmnt(1.01);
        sale.setNtwrk("Network");
        sale.setPrcssdt(new Date());
        sale.setPrdctCtgry("productCategory");
        sale.setPrdctd("productId");
        sale.setPrdctNm("productName");
        sale.setQntty(1);
        sale.setRdrd("redirectId");
        sale.setSlmnt(10.01);

        UserTracker userTracker = new UserTracker();
        userTracker.setSrcd(user.getCd());

        PurchaseEvent event = new PurchaseEvent(user, userTracker,  sale);

        ArgumentCaptor<Map<String, Object>> trackArgument = (ArgumentCaptor)ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<Map<String, Object>> setArgument = (ArgumentCaptor)ArgumentCaptor.forClass(Map.class);

        when(userService.findUserSupplement(user.getCd())).thenReturn(userSupplement);
        when(userTrackerService.findAffiliateIdentifiers(user.getCd())).thenReturn(affiliateIdentifiers);

        mixpanelService.purchase(event);

        verify(mpMetrics).identify(user.getCd());
        verify(mpMetrics).track(eq(MixpanelEventType.PURCHASE.getValue()), trackArgument.capture());
        verify(mpMetrics).increment(MixpanelPeoplePropertyType.PURCHASED_ITEMS.getValue(), sale.getQntty());
        verify(mpMetrics).increment(MixpanelPeoplePropertyType.SALES_TOTAL.getValue(), 10);
        verify(mpMetrics).increment(MixpanelPeoplePropertyType.COMMISSION_TOTAL.getValue(), 1);

        Map<String, Object> trackArgumentMap = trackArgument.getValue();

        assertGlobalProperties(trackArgumentMap);
        assertEquals("Product Category not set", sale.getPrdctCtgry(), trackArgumentMap.get(MixpanelEngagementPropertyType.PRODUCT_CATEGORY.getValue()));
        assertEquals("Product Name not set", sale.getPrdctNm(), trackArgumentMap.get(MixpanelEngagementPropertyType.PRODUCT_NAME.getValue()));
        assertEquals("Product Id not set", sale.getPrdctd(), trackArgumentMap.get(MixpanelEngagementPropertyType.PRODUCT_ID.getValue()));
        assertEquals("Advertiser not set", sale.getAdvrtsrNm(), trackArgumentMap.get(MixpanelEngagementPropertyType.ADVERTISER.getValue()));
        assertEquals("Network not set", sale.getNtwrk(), trackArgumentMap.get(MixpanelEngagementPropertyType.NETWORK.getValue()));
        assertEquals("Sales Amount not set", sale.getSlmnt(), trackArgumentMap.get(MixpanelEngagementPropertyType.SALES_AMOUNT.getValue()));
        assertEquals("Commission Amount not set", sale.getCmmssnmnt(), trackArgumentMap.get(MixpanelEngagementPropertyType.COMMISSION_AMOUNT.getValue()));
        assertEquals("Quantity not set", sale.getQntty(), trackArgumentMap.get(MixpanelEngagementPropertyType.QUANTITY.getValue()));
    }

    @Test
    public void testReturn()
    {
        Sale sale = new Sale();
        sale.setAdvrtsrNm("Advertiser");
        sale.setCmmssnmnt(-1.01);
        sale.setNtwrk("Network");
        sale.setPrcssdt(new Date());
        sale.setPrdctCtgry("productCategory");
        sale.setPrdctd("productId");
        sale.setPrdctNm("productName");
        sale.setQntty(-1);
        sale.setRdrd("redirectId");
        sale.setSlmnt(-10.01);

        UserTracker userTracker = new UserTracker();
        userTracker.setSrcd(user.getCd());

        PurchaseEvent event = new PurchaseEvent(user, userTracker, sale);

        ArgumentCaptor<Map<String, Object>> trackArgument = (ArgumentCaptor)ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<Map<String, Object>> setArgument = (ArgumentCaptor)ArgumentCaptor.forClass(Map.class);

        when(userService.findUserSupplement(user.getCd())).thenReturn(userSupplement);
        when(userTrackerService.findAffiliateIdentifiers(user.getCd())).thenReturn(affiliateIdentifiers);

        mixpanelService.purchase(event);

        verify(mpMetrics).identify(user.getCd());
        verify(mpMetrics).track(eq(MixpanelEventType.RETURN.getValue()), trackArgument.capture());
        verify(mpMetrics).increment(MixpanelPeoplePropertyType.PURCHASED_ITEMS.getValue(), sale.getQntty());
        verify(mpMetrics).increment(MixpanelPeoplePropertyType.SALES_TOTAL.getValue(), -10);
        verify(mpMetrics).increment(MixpanelPeoplePropertyType.COMMISSION_TOTAL.getValue(), -1);

        Map<String, Object> trackArgumentMap = trackArgument.getValue();

        assertGlobalProperties(trackArgumentMap);
        assertEquals("Product Category not set", sale.getPrdctCtgry(), trackArgumentMap.get(MixpanelEngagementPropertyType.PRODUCT_CATEGORY.getValue()));
        assertEquals("Product Name not set", sale.getPrdctNm(), trackArgumentMap.get(MixpanelEngagementPropertyType.PRODUCT_NAME.getValue()));
        assertEquals("Product Id not set", sale.getPrdctd(), trackArgumentMap.get(MixpanelEngagementPropertyType.PRODUCT_ID.getValue()));
        assertEquals("Advertiser not set", sale.getAdvrtsrNm(), trackArgumentMap.get(MixpanelEngagementPropertyType.ADVERTISER.getValue()));
        assertEquals("Network not set", sale.getNtwrk(), trackArgumentMap.get(MixpanelEngagementPropertyType.NETWORK.getValue()));
        assertEquals("Sales Amount not set", sale.getSlmnt(), trackArgumentMap.get(MixpanelEngagementPropertyType.SALES_AMOUNT.getValue()));
        assertEquals("Commission Amount not set", sale.getCmmssnmnt(), trackArgumentMap.get(MixpanelEngagementPropertyType.COMMISSION_AMOUNT.getValue()));
        assertEquals("Quantity not set", sale.getQntty(), trackArgumentMap.get(MixpanelEngagementPropertyType.QUANTITY.getValue()));
    }

    @Test
    public void anonymousSale()
    {
        Sale sale = new Sale();
        sale.setAdvrtsrNm("Advertiser");
        sale.setCmmssnmnt(1.01);
        sale.setNtwrk("Network");
        sale.setPrcssdt(new Date());
        sale.setPrdctCtgry("productCategory");
        sale.setPrdctd("productId");
        sale.setPrdctNm("productName");
        sale.setQntty(1);
        sale.setRdrd("redirectId");
        sale.setSlmnt(10.01);

        PurchaseEvent event = new PurchaseEvent(null, null, sale);

        ArgumentCaptor<Map<String, Object>> trackArgument = (ArgumentCaptor)ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<Map<String, Object>> setArgument = (ArgumentCaptor)ArgumentCaptor.forClass(Map.class);

        when(userService.findUserSupplement(user.getCd())).thenReturn(userSupplement);
        when(userTrackerService.findAffiliateIdentifiers(user.getCd())).thenReturn(affiliateIdentifiers);

        mixpanelService.purchase(event);

        verify(mpMetrics).identify(startsWith("anon_"));
        verify(mpMetrics).track(eq(MixpanelEventType.PURCHASE.getValue()), trackArgument.capture());

        Map<String, Object> trackArgumentMap = trackArgument.getValue();

        assertEquals("Product Category not set", sale.getPrdctCtgry(), trackArgumentMap.get(MixpanelEngagementPropertyType.PRODUCT_CATEGORY.getValue()));
        assertEquals("Product Name not set", sale.getPrdctNm(), trackArgumentMap.get(MixpanelEngagementPropertyType.PRODUCT_NAME.getValue()));
        assertEquals("Product Id not set", sale.getPrdctd(), trackArgumentMap.get(MixpanelEngagementPropertyType.PRODUCT_ID.getValue()));
        assertEquals("Advertiser not set", sale.getAdvrtsrNm(), trackArgumentMap.get(MixpanelEngagementPropertyType.ADVERTISER.getValue()));
        assertEquals("Network not set", sale.getNtwrk(), trackArgumentMap.get(MixpanelEngagementPropertyType.NETWORK.getValue()));
        assertEquals("Sales Amount not set", sale.getSlmnt(), trackArgumentMap.get(MixpanelEngagementPropertyType.SALES_AMOUNT.getValue()));
        assertEquals("Commission Amount not set", sale.getCmmssnmnt(), trackArgumentMap.get(MixpanelEngagementPropertyType.COMMISSION_AMOUNT.getValue()));
        assertEquals("Quantity not set", sale.getQntty(), trackArgumentMap.get(MixpanelEngagementPropertyType.QUANTITY.getValue()));


    }

    @Test
    public void anonymousSaleWithTracking()
    {
        Sale sale = new Sale();
        sale.setAdvrtsrNm("Advertiser");
        sale.setCmmssnmnt(1.01);
        sale.setNtwrk("Network");
        sale.setPrcssdt(new Date());
        sale.setPrdctCtgry("productCategory");
        sale.setPrdctd("productId");
        sale.setPrdctNm("productName");
        sale.setQntty(1);
        sale.setRdrd("redirectId");
        sale.setSlmnt(10.01);

        UserTracker userTracker = new UserTracker();
        userTracker.setSrcd(user.getCd());

        PurchaseEvent event = new PurchaseEvent(null, userTracker, sale);

        ArgumentCaptor<Map<String, Object>> trackArgument = (ArgumentCaptor)ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<Map<String, Object>> setArgument = (ArgumentCaptor)ArgumentCaptor.forClass(Map.class);

        when(userService.findUserSupplement(user.getCd())).thenReturn(userSupplement);
        when(userTrackerService.findAffiliateIdentifiers(user.getCd())).thenReturn(affiliateIdentifiers);

        mixpanelService.purchase(event);

        verify(mpMetrics).identify(userTracker.getSrcd());
        verify(mpMetrics).track(eq(MixpanelEventType.PURCHASE.getValue()), trackArgument.capture());

        Map<String, Object> trackArgumentMap = trackArgument.getValue();

        assertEquals("Product Category not set", sale.getPrdctCtgry(), trackArgumentMap.get(MixpanelEngagementPropertyType.PRODUCT_CATEGORY.getValue()));
        assertEquals("Product Name not set", sale.getPrdctNm(), trackArgumentMap.get(MixpanelEngagementPropertyType.PRODUCT_NAME.getValue()));
        assertEquals("Product Id not set", sale.getPrdctd(), trackArgumentMap.get(MixpanelEngagementPropertyType.PRODUCT_ID.getValue()));
        assertEquals("Advertiser not set", sale.getAdvrtsrNm(), trackArgumentMap.get(MixpanelEngagementPropertyType.ADVERTISER.getValue()));
        assertEquals("Network not set", sale.getNtwrk(), trackArgumentMap.get(MixpanelEngagementPropertyType.NETWORK.getValue()));
        assertEquals("Sales Amount not set", sale.getSlmnt(), trackArgumentMap.get(MixpanelEngagementPropertyType.SALES_AMOUNT.getValue()));
        assertEquals("Commission Amount not set", sale.getCmmssnmnt(), trackArgumentMap.get(MixpanelEngagementPropertyType.COMMISSION_AMOUNT.getValue()));
        assertEquals("Quantity not set", sale.getQntty(), trackArgumentMap.get(MixpanelEngagementPropertyType.QUANTITY.getValue()));

        assertEquals("AffiliateId not set", "affiliateUrlName", trackArgumentMap.get(MixpanelEngagementPropertyType.AFFILIATE_ID.getValue()));
        assertEquals("CampaignId not set", "campaignUrlName", trackArgumentMap.get(MixpanelEngagementPropertyType.CAMPAIGN_ID.getValue()));
        assertEquals("AffiliateReferrerId not set", "referrerUrlName", trackArgumentMap.get(MixpanelEngagementPropertyType.AFFILIATE_REFERRER_ID.getValue()));

    }

    private void assertGlobalProperties(Map<String, Object> trackArgumentMap) {
        assertEquals("First Name not set", "Jimmy", trackArgumentMap.get(MixpanelEngagementPropertyType.FIRST_NAME.getValue()));
        assertEquals("Last Name not set", "Page", trackArgumentMap.get(MixpanelEngagementPropertyType.LAST_NAME.getValue()));
        assertEquals("Full Name not set", "Jimmy Page", trackArgumentMap.get(MixpanelEngagementPropertyType.FULL_NAME.getValue()));
        assertEquals("Gender not set", Gender.MALE, trackArgumentMap.get(MixpanelEngagementPropertyType.GENDER.getValue()));
        assertEquals("Email not set", "jimmy@ledzeppelin.edu", trackArgumentMap.get(MixpanelEngagementPropertyType.EMAIL.getValue()));

        assertEquals("Affiliate ID not set", "affiliateUrlName", trackArgumentMap.get(MixpanelEngagementPropertyType.AFFILIATE_ID.getValue()));
        assertEquals("Campaign ID not set", "campaignUrlName", trackArgumentMap.get(MixpanelEngagementPropertyType.CAMPAIGN_ID.getValue()));
        assertEquals("Affiliate Referrer Id not set", "referrerUrlName", trackArgumentMap.get(MixpanelEngagementPropertyType.AFFILIATE_REFERRER_ID.getValue()));
    }
}
