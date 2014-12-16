/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.test.service;

import com.lela.commons.remote.MerchantClient;
import com.lela.commons.remote.impl.AmazonMerchantClient;
import com.lela.commons.service.BranchService;
import com.lela.commons.service.ItemService;
import com.lela.commons.service.OfferService;
import com.lela.commons.service.PostalCodeService;
import com.lela.commons.service.StoreService;
import com.lela.commons.service.impl.MerchantServiceImpl;
import com.lela.commons.web.ResourceNotFoundException;
import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.Attribute;
import com.lela.domain.document.AvailableInStore;
import com.lela.domain.document.Item;
import com.lela.domain.document.Redirect;
import com.lela.domain.document.User;
import com.lela.domain.enums.MerchantType;
import com.lela.domain.enums.RedirectType;
import com.maxmind.geoip.LookupService;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * User: Chris Tallent
 * Date: 12/13/12
 * Time: 2:21 PM
 */
@RunWith(MockitoJUnitRunner.class)
public class MerchantServiceUnitTest {

    private static final String USER_CODE = "user-code";
    private static final String MERCHANTID = "merchantid";
    private static final String MERCHANT_SOURCE = "merchant-source";
    private static final String POPSHOPS_MERCHANT = "popshops-merchant";
    private static final String ITEM_URL_NAME = "item-rlnm";
    private static final String TEST_PURCHASE_URL = "test-purchase-url";

    @Mock private Map<String, MerchantClient> merchantClients;

    @Mock private PostalCodeService postalCodeService;

    @Mock private StoreService storeService;

    @Mock private BranchService branchService;

    @Mock private LookupService lookupService;

    @Mock private LookupService lookupServiceIpv6;

    @Mock private ItemService itemService;

    @Mock private OfferService offerService;

    @InjectMocks MerchantServiceImpl merchantService;

    private ObjectId userId;
    private User user;
    private Item item;
    private AffiliateAccount affiliateWithCart;
    private AffiliateAccount affiliateNoCart;

    @Before
    public void setup() {
        userId = new ObjectId();
        user = new User();
        user.setId(userId);
        user.setCd(USER_CODE);

        item = new Item();
        item.setId(new ObjectId());
        item.setRlnm(ITEM_URL_NAME);
        item.setStrs(new ArrayList<AvailableInStore>());
        item.setSbttrs(new ArrayList<Attribute>());
        item.getSbttrs().add(new Attribute("PurchaseUrl", TEST_PURCHASE_URL));

        AvailableInStore avail = new AvailableInStore();
        avail.setId(new ObjectId());
        avail.setMrchntd(MERCHANTID);
        avail.setMrchntsrc(MERCHANT_SOURCE);
        avail.setTms(new ArrayList<Item>());
        avail.getTms().add(item);

        item.getStrs().add(avail);

        avail = new AvailableInStore();
        avail.setId(new ObjectId());
        avail.setMrchntd(AmazonMerchantClient.MERCHANT_ID);
        avail.setTms(new ArrayList<Item>());
        avail.getTms().add(item);

        item.getStrs().add(avail);

        avail = new AvailableInStore();
        avail.setId(new ObjectId());
        avail.setMrchntd(POPSHOPS_MERCHANT);
        avail.setTms(new ArrayList<Item>());
        avail.getTms().add(item);

        item.getStrs().add(avail);

        affiliateWithCart = new AffiliateAccount();
        affiliateWithCart.setNm("Test Affiliate");
        affiliateWithCart.setRlnm("test-affiliate-rlnm");
        affiliateWithCart.setShwcrt(true);

        affiliateNoCart = new AffiliateAccount();
        affiliateNoCart.setNm("No Cart Affiliate");
        affiliateNoCart.setRlnm("no-cart-affiliate-rlnm");
        affiliateNoCart.setShwcrt(false);
    }

    @Test
    public void testBuyRedirect() {
        // Set mock data
        when(itemService.findItemByUrlName(ITEM_URL_NAME)).thenReturn(item);

        Redirect redirect = merchantService.constructRedirect(ITEM_URL_NAME, MERCHANTID, userId);

        // White box verifications
        verify(merchantClients).get(MERCHANT_SOURCE);

        // Verify the redirect
        assertNotNull("Redirect is null", redirect);
        assertNotNull("Redirect is missing ID", redirect.getId());
        assertNotNull("Redirect does not have date", redirect.getRdrctdt());
        assertEquals("Item does not have correct Add to Cart URL", TEST_PURCHASE_URL, redirect.getRl());
        assertEquals("Redirect type is not Add to Cart", RedirectType.BUY_IT, redirect.getTp());

        Map<String, Object> expectedAttrs = new HashMap<String, Object>();
        expectedAttrs.put("mrchntd", MERCHANTID);
        expectedAttrs.put("rlnm", ITEM_URL_NAME);

        assertEquals("Redirect attributes are incorrect", expectedAttrs, redirect.getAttrs());
    }

    @Test
    public void testBuyRedirectAmazonSource() {
        // Set mock data
        when(itemService.findItemByUrlName(ITEM_URL_NAME)).thenReturn(item);
        Redirect redirect = merchantService.constructRedirect(ITEM_URL_NAME, AmazonMerchantClient.MERCHANT_ID, userId);

        // White box verifications
        verify(merchantClients).get(MerchantType.AMZN.toString());

        // Verify the redirect
        assertNotNull("Redirect is null", redirect);
        assertNotNull("Redirect is missing ID", redirect.getId());
        assertNotNull("Redirect does not have date", redirect.getRdrctdt());
        assertEquals("Item does not have correct Add to Cart URL", TEST_PURCHASE_URL, redirect.getRl());
        assertEquals("Redirect type is not Add to Cart", RedirectType.BUY_IT, redirect.getTp());

        Map<String, Object> expectedAttrs = new HashMap<String, Object>();
        expectedAttrs.put("mrchntd", AmazonMerchantClient.MERCHANT_ID);
        expectedAttrs.put("rlnm", ITEM_URL_NAME);

        assertEquals("Redirect attributes are incorrect", expectedAttrs, redirect.getAttrs());
    }

    @Test
    public void testBuyRedirectLegacyPopshops() {
        // Set mock data
        when(itemService.findItemByUrlName(ITEM_URL_NAME)).thenReturn(item);
        Redirect redirect = merchantService.constructRedirect(ITEM_URL_NAME, POPSHOPS_MERCHANT, userId);

        // White box verifications
        verify(merchantClients).get(MerchantType.POPSHOPS.toString());

        // Verify the redirect
        assertNotNull("Redirect is null", redirect);
        assertNotNull("Redirect is missing ID", redirect.getId());
        assertNotNull("Redirect does not have date", redirect.getRdrctdt());
        assertEquals("Item does not have correct Add to Cart URL", TEST_PURCHASE_URL, redirect.getRl());
        assertEquals("Redirect type is not Add to Cart", RedirectType.BUY_IT, redirect.getTp());

        Map<String, Object> expectedAttrs = new HashMap<String, Object>();
        expectedAttrs.put("mrchntd", POPSHOPS_MERCHANT);
        expectedAttrs.put("rlnm", ITEM_URL_NAME);

        assertEquals("Redirect attributes are incorrect", expectedAttrs, redirect.getAttrs());
    }

    @Test
    public void testAddToCart() {
        // Set mock data
        when(itemService.findItemByUrlName(ITEM_URL_NAME)).thenReturn(item);

        Redirect redirect = merchantService.constructAddToCartRedirect(affiliateWithCart, ITEM_URL_NAME, MERCHANTID, userId);

        // Verify the redirect
        assertNotNull("Redirect is null", redirect);
        assertNotNull("Redirect is missing ID", redirect.getId());
        assertNotNull("Redirect does not have date", redirect.getRdrctdt());
        assertEquals("Item does not have correct Add to Cart URL", TEST_PURCHASE_URL, redirect.getRl());
        assertEquals("Redirect type is not Add to Cart", RedirectType.ADD_TO_CART, redirect.getTp());

        Map<String, Object> expectedAttrs = new HashMap<String, Object>();
        expectedAttrs.put("mrchntd", MERCHANTID);
        expectedAttrs.put("rlnm", ITEM_URL_NAME);

        assertEquals("Redirect attributes are incorrect", expectedAttrs, redirect.getAttrs());
    }

    @Test
    public void test404NoDomain() {
        boolean threwException = false;
        try {
            merchantService.constructAddToCartRedirect(null, ITEM_URL_NAME, MERCHANTID, userId);
        } catch (ResourceNotFoundException e) {
            threwException = true;
        }

        if (!threwException) {
            fail("Add to Cart with no domain should throw exception");
        }
    }

    @Test
    public void test404NoCartOnDomain() {
        boolean threwException = false;
        try {
            merchantService.constructAddToCartRedirect(affiliateNoCart, ITEM_URL_NAME, MERCHANTID, userId);
        } catch (ResourceNotFoundException e) {
            threwException = true;
        }

        if (!threwException) {
            fail("Add to Cart with no domain should throw exception");
        }
    }

}
