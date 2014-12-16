package com.lela.web.test.controller;

import com.lela.commons.service.BranchService;
import com.lela.commons.service.EventService;
import com.lela.commons.service.ItemService;
import com.lela.commons.service.OfferService;
import com.lela.commons.service.ProfileService;
import com.lela.commons.service.StoreService;
import com.lela.util.test.integration.oauth.ServerRunning;
import com.lela.domain.document.Branch;
import com.lela.domain.document.Coupon;
import com.lela.domain.document.Item;
import com.lela.domain.document.Offer;
import com.lela.domain.document.Store;
import com.lela.domain.document.User;
import com.lela.domain.dto.Principal;
import com.lela.commons.service.LocalCacheEvictionService;
import com.lela.commons.service.OfferCouponService;
import com.lela.commons.service.UserService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.domain.dto.coupon.CouponGenerationRequest;
import com.lela.domain.dto.coupon.CouponGenerationResult;
import com.lela.domain.dto.coupon.CouponRedemptionRequest;
import com.lela.util.utilities.GreenMailHelper;
import com.lela.web.test.AbstractFunctionalTest;
import com.lela.web.web.controller.OfferCouponController;
import com.lela.commons.web.utils.WebConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class OfferCouponControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(OfferCouponControllerFunctionalTest.class);

    @Rule
    public ServerRunning serverRunning = ServerRunning.isRunning();

    private static final String ITEM = "mountainbuggyduo";
    private static final String STORE_URL_NAME = "teststore";
    private static final String STORE_MERCHANT_ID = "1";
    private static final String BRANCH_URL_NAME = "testbranch";
    private static final String BRANCH_LOCAL_CODE = "testbranchcode";
    private static final String BRANCH_URL_NAME2 = "testbranch2";
    private static final String BRANCH_LOCAL_CODE2 = "testbranchcode2";
    private static final String OFFER_TEST = "testoffer";
    private static final String OFFER_TEST2 = "testoffer2";
    private static final String LOCAL_COUPON_EMAIL = "testuser11kijmkhj@yopmail.com";
    private static final String LELA_SUPPORT_EMAIL = "testuser10kijmkhj@yopmail.com";

    private static final String COUPON_REQUESTED_SUBJECT = "LelaLocal coupon requested: ";
    private static final String COUPON_TRANSACTION_SUBJECT = "LelaLocal transaction confirmation: ";

    private static final String INVALID_OFFER_TEST = "invalid_testoffer";
    private static final String OFFER_NOT_EXISTS = "error.offer.not.exists";

    @Autowired
    private OfferCouponController offerCouponController;

    @Autowired
    private OfferCouponService offerCouponService;

    @Autowired
    private EventService eventService;

    @Autowired
    private LocalCacheEvictionService localCacheEvictionService;

    @Autowired
    private UserService userService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private BranchService branchService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private ProfileService profileService;

    private User user;
    
    @Before
    public void setUp() {
        SpringSecurityHelper.secureChannel();

        log.info("Create a store and save it");
        Store store = new Store();
        store.setNm(STORE_URL_NAME);
        store.setRlnm(STORE_URL_NAME);
        store.setMrchntd(STORE_MERCHANT_ID);

        try {
            storeService.saveStore(store);
        } catch (Exception ex) {
            log.info("Error saving new store for test: " + ex.getMessage());
        }

        log.info("Create a branch for store");
        store = storeService.findStoreByUrlName(STORE_URL_NAME);
        Branch branch = new Branch();
        branch.setNm(BRANCH_URL_NAME);
        branch.setRlnm(BRANCH_URL_NAME);
        branch.setMrchntnm(STORE_URL_NAME);
        branch.setMrchntd(STORE_MERCHANT_ID);
        branch.setLclcd(BRANCH_LOCAL_CODE);

        Branch branch2 = new Branch();
        branch2.setNm(BRANCH_URL_NAME2);
        branch2.setRlnm(BRANCH_URL_NAME2);
        branch2.setMrchntnm(STORE_URL_NAME);
        branch2.setMrchntd(STORE_MERCHANT_ID);
        branch2.setLclcd(BRANCH_LOCAL_CODE2);

        try {
            branchService.saveBranch(branch);
        } catch (Exception ex) {
            log.info("Error saving new branch for test: " + ex.getMessage());
        }

        try {
            branchService.saveBranch(branch2);
        } catch (Exception ex) {
            log.info("Error saving second new branch for test: " + ex.getMessage());
        }

        log.info("Clear user coupons...");
        user = createRandomUser(true);
        user = profileService.registerTestUser(user);

        log.info("Clear offers...");
        Offer offer = offerService.findOfferByUrlName(OFFER_TEST);
        if (offer != null) {
            offerService.removeOffer(offer.getRlnm());
            localCacheEvictionService.evictOffer(offer.getRlnm());
        }
        Offer offer2 = offerService.findOfferByUrlName(OFFER_TEST2);
        if (offer2 != null) {
            offerService.removeOffer(offer2.getRlnm());
            localCacheEvictionService.evictOffer(offer2.getRlnm());
        }

        SpringSecurityHelper.unsecureChannel();
    }

    @After
    public void tearDown() {
        SpringSecurityHelper.secureChannel();

        log.info("Delete branches...");
        branchService.removeBranch(BRANCH_URL_NAME);
        branchService.removeBranch(BRANCH_URL_NAME2);

        Store store = storeService.findStoreByUrlName(STORE_URL_NAME);

        log.info("Deleting store...");
        storeService.removeStore(store.getRlnm());
        localCacheEvictionService.evictStore(store.getRlnm());

        log.info("Deleting user coupons...");
        if (user != null) {
            userService.removeUser(user);
        }

        log.info("Deleting offers...");
        Offer offer = offerService.findOfferByUrlName(OFFER_TEST);
        if (offer != null) {
            offerService.removeOffer(offer.getRlnm());
            localCacheEvictionService.evictOffer(offer.getRlnm());
        }
        Offer offer2 = offerService.findOfferByUrlName(OFFER_TEST2);
        if (offer2 != null) {
            offerService.removeOffer(offer2.getRlnm());
            localCacheEvictionService.evictOffer(offer2.getRlnm());
        }

        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testShowDeals() {
        SpringSecurityHelper.secureChannel(new Principal(user));

        try {
            Model model = new BindingAwareModelMap();
            String view = offerCouponController.deals(model);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "redirect:/store/giggle", view);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testShowOffer() {
        SpringSecurityHelper.secureChannel(new Principal(user));

        Date startDate = new Date();
        Calendar expirationDate = new GregorianCalendar();
        expirationDate.add(Calendar.DAY_OF_MONTH, 1);

        Offer offer = new Offer(OFFER_TEST, BRANCH_URL_NAME, "0% off all items",
                startDate, expirationDate.getTime(), "Test Offer not valid", false, 1);
        offer = offerService.saveOffer(offer);
        assertNotNull("Offer id is null", offer.getId());

        Item item = itemService.findItemByUrlName(ITEM);
        assertNotNull("User1 is missing", item);
        assertNotNull("User1 is missing an id", item.getId());

        try {
            Model model = new BindingAwareModelMap();
            String view = offerCouponController.show(OFFER_TEST, model);
            Offer showOffer = (Offer) model.asMap().get(WebConstants.OFFER);
            Branch showBranch = (Branch) model.asMap().get(WebConstants.BRANCH);
            assertEquals("view name is incorrect", "local.coupon.create", view);
            assertNotNull(showOffer);
            assertEquals("Wrong offer returned", OFFER_TEST, showOffer.getRlnm());
            assertNotNull(showBranch);
            assertEquals("Wrong branch returned", BRANCH_URL_NAME, showBranch.getRlnm());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testShowInvalidOffer() {
        SpringSecurityHelper.secureChannel(new Principal(user));

        try {
            Model model = new BindingAwareModelMap();
            String view = offerCouponController.show(INVALID_OFFER_TEST, model);
            String error = (String) model.asMap().get(WebConstants.ERROR);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "local.coupon.create", view);
            assertNotNull("error not provided", error);
            assertEquals("incorrect error provided", OFFER_NOT_EXISTS, error);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testCreateCoupon() {
        SpringSecurityHelper.secureChannel(new Principal(user));

        Date startDate = new Date();
        Calendar expirationDate = new GregorianCalendar();
        expirationDate.add(Calendar.DAY_OF_MONTH, 1);

        Offer offer = new Offer(OFFER_TEST, BRANCH_URL_NAME, "0% off all items",
                startDate, expirationDate.getTime(), "Test Offer not valid", false, 1);
        offer = offerService.saveOffer(offer);
        assertNotNull("Offer id is null", offer.getId());

        Item item = itemService.findItemByUrlName(ITEM);
        assertNotNull("User1 is missing", item);
        assertNotNull("User1 is missing an id", item.getId());

        try {
            Model model = new BindingAwareModelMap();
            String view = offerCouponController.generateCouponForUser(new CouponGenerationRequest(OFFER_TEST, item.getRlnm()), model);
            CouponGenerationResult coupon = (CouponGenerationResult) model.asMap().get(WebConstants.COUPON);
            assertNull("Coupon shouldn't have been created", coupon.getCoupon());
            assertEquals("view name is incorrect", "local.coupon.confirmation", view);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        try {
            assertTrue("Email didn't arrive", greenMail.waitForIncomingEmail(5000, 2));
            List<MimeMessage> receivedMessages = GreenMailHelper.getReceivedMessages(user.getMl());
            MimeMessage receivedMessage = receivedMessages.get(0);
            Address address = receivedMessage.getAllRecipients()[0];
            assertEquals("Recipient email address incorrect", user.getMl(), address.toString());
            address = receivedMessage.getFrom()[0];
            assertEquals("From email address incorrect", LELA_SUPPORT_EMAIL, address.toString());
            assertEquals("Email subject line incorrect", "0% off all items", receivedMessage.getSubject());

            receivedMessages = GreenMailHelper.getReceivedMessages(LOCAL_COUPON_EMAIL);
            receivedMessage = receivedMessages.get(0);
            address = receivedMessage.getAllRecipients()[0];
            assertEquals("Recipient email address incorrect", LOCAL_COUPON_EMAIL, address.toString());
            address = receivedMessage.getFrom()[0];
            assertEquals("From email address incorrect", LELA_SUPPORT_EMAIL, address.toString());
            assertEquals("Email subject line incorrect", COUPON_REQUESTED_SUBJECT + BRANCH_URL_NAME, receivedMessage.getSubject());
        } catch (InterruptedException e) {
            fail("Green mail exception waiting for incoming email");
        } catch (MessagingException e) {
            fail("Green mail exception getting mail data");
        }

        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testCreateCouponWithoutItem() {
        SpringSecurityHelper.secureChannel(new Principal(user));

        Date startDate = new Date();
        Calendar expirationDate = new GregorianCalendar();
        expirationDate.add(Calendar.DAY_OF_MONTH, 1);

        Offer offer = new Offer(OFFER_TEST, BRANCH_URL_NAME, "0% off all items",
                startDate, expirationDate.getTime(), "Test Offer not valid", false, 1);
        offer = offerService.saveOffer(offer);
        assertNotNull("Offer id is null", offer.getId());

        Item item = itemService.findItemByUrlName(ITEM);
        assertNotNull("User1 is missing", item);
        assertNotNull("User1 is missing an id", item.getId());

        try {
            Model model = new BindingAwareModelMap();
            String view = offerCouponController.generateCouponForUser(new CouponGenerationRequest(OFFER_TEST, null), model);
            CouponGenerationResult coupon = (CouponGenerationResult) model.asMap().get(WebConstants.COUPON);
            assertNull("Coupon shouldn't have been created", coupon.getCoupon());
            assertEquals("view name is incorrect", "local.coupon.confirmation", view);
            assertNotNull("Coupon is null", coupon.getCoupon());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        try {
            assertTrue("Email didn't arrive", AbstractFunctionalTest.greenMail.waitForIncomingEmail(5000, 4));
            List<MimeMessage> receivedMessages = GreenMailHelper.getReceivedMessages(user.getMl());
            MimeMessage receivedMessage = receivedMessages.get(1);
            Address address = receivedMessage.getAllRecipients()[0];
            assertEquals("Recipient email address incorrect", user.getMl(), address.toString());
            address = receivedMessage.getFrom()[0];
            assertEquals("From email address incorrect", LELA_SUPPORT_EMAIL, address.toString());
            assertEquals("Email subject line incorrect", "0% off all items", receivedMessage.getSubject());

            receivedMessages = GreenMailHelper.getReceivedMessages(LOCAL_COUPON_EMAIL);
            receivedMessage = receivedMessages.get(1);
            address = receivedMessage.getAllRecipients()[0];
            assertEquals("Recipient email address incorrect", LOCAL_COUPON_EMAIL, address.toString());
            address = receivedMessage.getFrom()[0];
            assertEquals("From email address incorrect", LELA_SUPPORT_EMAIL, address.toString());
            assertEquals("Email subject line incorrect", COUPON_REQUESTED_SUBJECT + BRANCH_URL_NAME, receivedMessage.getSubject());
        } catch (InterruptedException e) {
            fail("Green mail exception waiting for incoming email");
        } catch (MessagingException e) {
            fail("Green mail exception getting mail data");
        }

        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testRedeemedCoupon() {
        SpringSecurityHelper.secureChannel(new Principal(user));

        Date startDate = new Date();
        Calendar expirationDate = new GregorianCalendar();
        expirationDate.add(Calendar.DAY_OF_MONTH, 1);

        Offer offer = new Offer(OFFER_TEST, BRANCH_URL_NAME, "0% off all items",
                startDate, expirationDate.getTime(), "Test Offer not valid", false, 1);
        offer = offerService.saveOffer(offer);
        assertNotNull("Offer id is null", offer.getId());

        Item item = itemService.findItemByUrlName(ITEM);
        assertNotNull("User1 is missing", item);
        assertNotNull("User1 is missing an id", item.getId());

        Coupon coupon1 = null;

        try {
            Model model = new BindingAwareModelMap();
            String view = offerCouponController.generateCouponForUser(new CouponGenerationRequest(OFFER_TEST, item.getRlnm()), model);
            CouponGenerationResult coupon = (CouponGenerationResult) model.asMap().get(WebConstants.COUPON);
            assertNull("Coupon shouldn't have been created", coupon.getCoupon());
            assertEquals("view name is incorrect", "local.coupon.confirmation", view);
            assertNotNull("Coupon created is null", coupon.getCoupon());
            assertNotNull("Coupon code is null", coupon.getCoupon().getCpncd());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        try {
            Model model = new BindingAwareModelMap();
            String view = offerCouponController.showRedeemCouponForm(BRANCH_URL_NAME, coupon1.getCpncd(), model);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "local.coupon.redeem", view);

            Coupon couponToView = (Coupon) model.asMap().get(WebConstants.COUPON);
            assertNotNull("Coupon returned is null", couponToView);
            assertEquals("Incorrect coupon returned", coupon1.getCpncd(), couponToView.getCpncd());

            Offer offerToView = (Offer) model.asMap().get(WebConstants.OFFER);
            assertNotNull("Offer returned is null", offerToView);
            assertEquals("Incorrect offer returned", OFFER_TEST, offerToView.getRlnm());

            Branch branchToView = (Branch) model.asMap().get(WebConstants.BRANCH);
            assertNotNull("Branch returned is null", branchToView);
            assertEquals("Incorrect branch returned", BRANCH_URL_NAME, branchToView.getRlnm());

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        Coupon coupon2 = null;
        try {
            Model model = new BindingAwareModelMap();
            RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
            String view = offerCouponController.redeemCoupon(new CouponRedemptionRequest(BRANCH_URL_NAME, coupon1.getCpncd(), BRANCH_LOCAL_CODE), model, redirectAttributes);
            coupon2 = (Coupon) model.asMap().get(WebConstants.COUPON);
            assertEquals("view name is incorrect", "local.coupon.redeem.success", view);
            assertNotNull("Coupon2 with redeemed date is null", coupon2);
            assertNotNull("Redemption date on coupon is null", coupon2.getRdmptndt());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info(coupon1.getCpnd().toString());
        log.info(coupon2.getCpnd().toString());
        assertTrue("Wrong coupon had buyer name changed", coupon1.getCpnd().equals(coupon2.getCpnd()));

        try {
            assertTrue("Email didn't arrive", AbstractFunctionalTest.greenMail.waitForIncomingEmail(5000, 7));
            List<MimeMessage> receivedMessages = GreenMailHelper.getReceivedMessages(LOCAL_COUPON_EMAIL);
            MimeMessage receivedMessage = receivedMessages.get(3);
            Address address = receivedMessage.getAllRecipients()[0];
            assertEquals("Recipient email address incorrect", LOCAL_COUPON_EMAIL, address.toString());
            address = receivedMessage.getFrom()[0];
            assertEquals("From email address incorrect", LELA_SUPPORT_EMAIL, address.toString());
            assertEquals("Email subject line incorrect", COUPON_TRANSACTION_SUBJECT + BRANCH_URL_NAME, receivedMessage.getSubject());
        } catch (InterruptedException e) {
            fail("Green mail exception waiting for incoming email");
        } catch (MessagingException e) {
            fail("Green mail exception getting mail data");
        }

        Coupon coupon3 = null;
        try {
            Model model = new BindingAwareModelMap();
            String view = offerCouponController.confirmation(OFFER_TEST, coupon2.getCpncd(), model);
            coupon3 = (Coupon) model.asMap().get(WebConstants.COUPON);
            assertEquals("view name is incorrect", "local.coupon.confirmation", view);
            assertNotNull("Coupon3 with redeemed date is null", coupon3);
            assertNotNull("Redemption date on coupon is null", coupon3.getRdmptndt());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info(coupon1.getCpnd().toString());
        log.info(coupon2.getCpnd().toString());
        assertTrue("Wrong coupon had buyer name changed", coupon1.getCpnd().equals(coupon2.getCpnd()));

        try {
            Model model = new BindingAwareModelMap();
            String view = offerCouponController.generateCouponForUser(new CouponGenerationRequest(OFFER_TEST, item.getRlnm()), model);
            Coupon coupon4 = (Coupon) model.asMap().get(WebConstants.COUPON);
            assertEquals("view name is incorrect", "local.coupon.create", view);
            assertNotNull("Coupon created is null", coupon4);
            assertEquals("Correct coupon not returned", coupon1.getCpncd(), coupon4.getCpncd());
            String error = (String) model.asMap().get(WebConstants.ERROR);
            assertNotNull("Error is null", error);
            assertEquals("Incorrect error given", "error.coupon.already.redeemed", error);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testRedeemedCouponAtAnotherBranch() {
        SpringSecurityHelper.secureChannel(new Principal(user));

        Date startDate = new Date();
        Calendar expirationDate = new GregorianCalendar();
        expirationDate.add(Calendar.DAY_OF_MONTH, 1);

        Offer offer = new Offer(OFFER_TEST, BRANCH_URL_NAME, "0% off all items",
                startDate, expirationDate.getTime(), "Test Offer not valid", false, 1);
        offer = offerService.saveOffer(offer);
        assertNotNull("Offer id is null", offer.getId());

        Item item = itemService.findItemByUrlName(ITEM);
        assertNotNull("User1 is missing", item);
        assertNotNull("User1 is missing an id", item.getId());

        Coupon coupon1 = null;

        try {
            Model model = new BindingAwareModelMap();
            String view = offerCouponController.generateCouponForUser(new CouponGenerationRequest(OFFER_TEST, item.getRlnm()), model);
            CouponGenerationResult coupon = (CouponGenerationResult) model.asMap().get(WebConstants.COUPON);
            assertNull("Coupon shouldn't have been created", coupon.getCoupon());
            assertEquals("view name is incorrect", "local.coupon.confirmation", view);
            assertNotNull("Coupon created is null", coupon.getCoupon());
            assertNotNull("Coupon code is null", coupon.getCoupon().getCpncd());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        branchService.findBranchByUrlName(BRANCH_URL_NAME);

        Coupon coupon2 = null;
        try {
            Model model = new BindingAwareModelMap();
            RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
            String view = offerCouponController.redeemCoupon(new CouponRedemptionRequest(BRANCH_URL_NAME, coupon1.getCpncd(), BRANCH_LOCAL_CODE2), model, redirectAttributes);
            coupon2 = (Coupon) model.asMap().get(WebConstants.COUPON);
            assertEquals("view name is incorrect", "local.coupon.redeem.success", view);
            assertNotNull("Coupon2 with redeemed date is null", coupon2);
            assertNotNull("Redemption date on coupon is null", coupon2.getRdmptndt());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info(coupon1.getCpnd().toString());
        log.info(coupon2.getCpnd().toString());
        assertTrue("Wrong coupon had buyer name changed", coupon1.getCpnd().equals(coupon2.getCpnd()));

        try {
            assertTrue("Email didn't arrive", AbstractFunctionalTest.greenMail.waitForIncomingEmail(5000, 6));
        } catch (InterruptedException e) {
            fail("Green mail exception waiting for incoming email");
        }

        Coupon coupon3 = null;
        try {
            Model model = new BindingAwareModelMap();
            String view = offerCouponController.confirmation(OFFER_TEST, coupon2.getCpncd(), model);
            coupon3 = (Coupon) model.asMap().get(WebConstants.COUPON);
            assertEquals("view name is incorrect", "local.coupon.confirmation", view);
            assertNotNull("Coupon3 with redeemed date is null", coupon3);
            assertNotNull("Redemption date on coupon is null", coupon3.getRdmptndt());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info(coupon1.getCpnd().toString());
        log.info(coupon2.getCpnd().toString());
        assertTrue("Wrong coupon had buyer name changed", coupon1.getCpnd().equals(coupon2.getCpnd()));

        try {
            Model model = new BindingAwareModelMap();
            String view = offerCouponController.generateCouponForUser(new CouponGenerationRequest(OFFER_TEST, item.getRlnm()), model);
            Coupon coupon4 = (Coupon) model.asMap().get(WebConstants.COUPON);
            assertEquals("view name is incorrect", "local.coupon.create", view);
            assertNotNull("Coupon created is null", coupon4);
            assertEquals("Correct coupon not returned", coupon1.getCpncd(), coupon4.getCpncd());
            String error = (String) model.asMap().get(WebConstants.ERROR);
            assertNotNull("Error is null", error);
            assertEquals("Incorrect error given", "error.coupon.already.redeemed", error);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testInvalid() {
        SpringSecurityHelper.secureChannel(new Principal(user));

        Date startDate = new Date();
        Calendar expirationDate = new GregorianCalendar();
        expirationDate.add(Calendar.DAY_OF_MONTH, 1);

        Offer offer = new Offer(OFFER_TEST, BRANCH_URL_NAME, "0% off all items",
                startDate, expirationDate.getTime(), "Test Offer not valid", false, 1);
        offer = offerService.saveOffer(offer);
        assertNotNull("Offer id is null", offer.getId());

        Item item = itemService.findItemByUrlName(ITEM);
        assertNotNull("User1 is missing", item);
        assertNotNull("User1 is missing an id", item.getId());

        try {
            Model model = new BindingAwareModelMap();
            String view = offerCouponController.generateCouponForUser(new CouponGenerationRequest(OFFER_TEST, item.getRlnm()), model);
            CouponGenerationResult coupon = (CouponGenerationResult) model.asMap().get(WebConstants.COUPON);
            assertNull("Coupon shouldn't have been created", coupon.getCoupon());
            assertEquals("view name is incorrect", "local.coupon.create", view);
            String error = (String) model.asMap().get(WebConstants.ERROR);
            assertNotNull("Error is null", error);
            assertEquals("Incorrect error given", "error.coupon.full.name.not.given", error);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        offer = offerService.findOfferByUrlName(OFFER_TEST);
        offer.setSdltd(true);
        offerService.saveOffer(offer);

        try {
            Model model = new BindingAwareModelMap();
            String view = offerCouponController.generateCouponForUser(new CouponGenerationRequest(OFFER_TEST, item.getRlnm()), model);
            Coupon coupon = (Coupon) model.asMap().get(WebConstants.COUPON);
            assertEquals("view name is incorrect", "local.coupon.create", view);
            assertNull("Coupon shouldn't have been created", coupon);
            String error = (String) model.asMap().get(WebConstants.ERROR);
            assertNotNull("Error is null", error);
            assertEquals("Incorrect error given", "error.offer.removed", error);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        offer.setSdltd(false);
        expirationDate.add(Calendar.DAY_OF_MONTH, -5);
        offer.setXprtndt(expirationDate.getTime());
        offerService.saveOffer(offer);

        try {
            Model model = new BindingAwareModelMap();
            String view = offerCouponController.generateCouponForUser(new CouponGenerationRequest(OFFER_TEST, item.getRlnm()), model);
            Coupon coupon = (Coupon) model.asMap().get(WebConstants.COUPON);
            assertEquals("view name is incorrect", "local.coupon.create", view);
            assertNull("Coupon shouldn't have been created", coupon);
            String error = (String) model.asMap().get(WebConstants.ERROR);
            assertNotNull("Error is null", error);
            assertEquals("Incorrect error given", "error.offer.expired", error);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
    }
}
