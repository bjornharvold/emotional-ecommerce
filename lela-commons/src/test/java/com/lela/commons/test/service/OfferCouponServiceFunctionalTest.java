package com.lela.commons.test.service;

import com.lela.commons.service.BranchService;
import com.lela.commons.service.ItemService;
import com.lela.commons.service.OfferService;
import com.lela.commons.service.StoreService;
import com.lela.commons.service.OfferValidationException;
import com.lela.domain.document.Branch;
import com.lela.domain.document.Coupon;
import com.lela.domain.document.Item;
import com.lela.domain.document.ListCard;
import com.lela.domain.document.Offer;
import com.lela.domain.document.Store;
import com.lela.domain.document.User;
import com.lela.commons.service.LocalCacheEvictionService;
import com.lela.commons.service.OfferCouponService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.test.AbstractFunctionalTest;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.coupon.CouponGenerationRequest;
import com.lela.domain.dto.coupon.CouponGenerationResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * TODO Coupons are not working!!!!!
 * Tests everything concerning offers and coupons
 */
public class OfferCouponServiceFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(OfferCouponServiceFunctionalTest.class);
    private static final String STORE_URL_NAME = "teststore";
    private static final String STORE_MERCHANT_ID = "1";
    private static final String BRANCH_URL_NAME = "testbranch";
    private static final String BRANCH_LOCAL_CODE = "testbranchcode";
    private static final String MULTIPLE_COUPONS_OFFER_TEST = "testoffer";
    private static final String SINGLE_COUPON_OFFER_TEST = "testoffer2";
    private static final String CATEGORY_URL_NAME = "testoffercouponcategory";

//    @Rule
//    public ServerRunning serverRunning = ServerRunning.isRunning();

    @Autowired
    private StoreService storeService;

    @Autowired
    private LocalCacheEvictionService localCacheEvictionService;

    @Autowired
    private OfferCouponService offerCouponService;

    @Autowired
    private BranchService branchService;

    @Autowired
    private MongoOperations mongo;

    @Autowired
    private ItemService itemService;

    @Autowired
    private OfferService offerService;

    private User user;
    private UserSupplement us;
    private Store store;
    private Branch branch;
    private Offer offerMultiplePerPerson;
    private Offer offerOnePerPerson;
    private Item item;

    @Before
    public void setUp() {
        log.info("Setting up test...");
        SpringSecurityHelper.secureChannel();

        log.info("Creating objects to support this test");

        log.info("Creating a store");
        store = new Store();
        store.setNm(STORE_URL_NAME);
        store.setRlnm(STORE_URL_NAME);
        store.setMrchntd(STORE_MERCHANT_ID);
        store = storeService.saveStore(store);
        assertNotNull("Store id is null", store.getId());

        log.info("Create a branch for store");
        branch = new Branch();
        branch.setNm(BRANCH_URL_NAME);
        branch.setRlnm(BRANCH_URL_NAME);
        branch.setMrchntnm(STORE_URL_NAME);
        branch.setMrchntd(STORE_MERCHANT_ID);
        branch.setLclcd(BRANCH_LOCAL_CODE);
        branch = branchService.saveBranch(branch);
        assertNotNull("Branch id is null", branch.getId());

        log.info("Creating an item");
        item = createRandomItem(CATEGORY_URL_NAME);
        item = itemService.saveItem(item);
        assertNotNull("Item id is null", item.getId());

        log.info("Creating user for this test");
        user = createRandomUser(true);

        us = createRandomUserSupplement(user);
        us = userService.saveUserSupplement(us);

        user = userService.saveUser(user);
        assertNotNull("User id is null", user.getId());

        Date startDate = new Date();
        Calendar expirationDate = new GregorianCalendar();
        expirationDate.add(Calendar.DAY_OF_MONTH, 1);

        offerMultiplePerPerson = new Offer(MULTIPLE_COUPONS_OFFER_TEST, BRANCH_URL_NAME, "0% off all items",
                startDate, expirationDate.getTime(), "Test Offer not valid", false, 1);
        offerMultiplePerPerson = offerService.saveOffer(offerMultiplePerPerson);
        assertNotNull("Offer id is null", offerMultiplePerPerson.getId());

        offerOnePerPerson = new Offer(SINGLE_COUPON_OFFER_TEST, BRANCH_URL_NAME, "0% off all items",
                startDate, expirationDate.getTime(), "Test Offer not valid", true, 1);
        offerOnePerPerson = offerService.saveOffer(offerOnePerPerson);
        assertNotNull("Offer id is null", offerOnePerPerson.getId());

        SpringSecurityHelper.unsecureChannel();

        log.info("Setting up test complete");
    }

    @After
    public void tearDown() {
        log.info("Tearing down test...");
        SpringSecurityHelper.secureChannel();

        log.info("Delete branch...");
        mongo.remove(query(where("rlnm").is(BRANCH_URL_NAME)), Branch.class);
        branch = branchService.findBranchByUrlName(BRANCH_URL_NAME);
        assertNull("Branch did not get deleted", branch);

        log.info("Deleting store...");
        mongo.remove(query(where("rlnm").is(STORE_URL_NAME)), Store.class);
        localCacheEvictionService.evictStore(STORE_URL_NAME);
        store = storeService.findStoreByUrlName(STORE_URL_NAME);
        assertNull("Store did not get deleted", store);

        log.info("Deleting user...");
        if (user != null) {
            userService.removeUser(user);
            user = userService.findUser(user.getId());
            assertNull("User did not get deleted", user);
        }

        if (item != null) {
            itemService.removeItem(item.getRlnm());
        }

        log.info("Deleting offers...");
        mongo.remove(query(where("rlnm").is(MULTIPLE_COUPONS_OFFER_TEST)), Offer.class);
        localCacheEvictionService.evictOffer(MULTIPLE_COUPONS_OFFER_TEST);
        offerMultiplePerPerson = offerService.findOfferByUrlName(MULTIPLE_COUPONS_OFFER_TEST);
        assertNull("Multiple offer per person object was not deleted", offerMultiplePerPerson);

        mongo.remove(query(where("rlnm").is(SINGLE_COUPON_OFFER_TEST)), Offer.class);
        localCacheEvictionService.evictOffer(SINGLE_COUPON_OFFER_TEST);
        offerOnePerPerson = offerService.findOfferByUrlName(SINGLE_COUPON_OFFER_TEST);
        assertNull("One offer per person object was not deleted", offerOnePerPerson);

        SpringSecurityHelper.unsecureChannel();
        
        log.info("Tear down complete");
    }

    @Test
    @Ignore
    public void testOfferAndCouponWorkflow() {
        log.info("Testing testOfferAndCouponWorkflow()...");

        try {
            log.info("Testing that there is at least one offer for the branch");
            List<Offer> offers = offerService.findOffersByBranchUrlName(BRANCH_URL_NAME);
            assertEquals("Offers for branch incorrect", 2, offers.size(), 0);
            
            log.info("Creating a coupon for a user");
            CouponGenerationResult couponResult1 = offerCouponService.createCouponForItem(user.getCd(), new CouponGenerationRequest(MULTIPLE_COUPONS_OFFER_TEST, item.getRlnm()));

            assertNotNull("Coupon result is null", couponResult1);
            assertNotNull("Created coupon is missing", couponResult1.getCoupon());
            assertNotNull("Created coupon code is missing", couponResult1.getCoupon().getCpncd());

            log.info("Coupon successfully created for user");
            log.info("Retrieving the same coupon from the db based on the coupon code and comparing the two");
            Coupon couponFound = offerCouponService.findCoupon(couponResult1.getCoupon().getCpncd());
            assertNotNull("Coupon is missing", couponFound);
            assertEquals("Created and obtained offer not the same", couponResult1.getCoupon().getCpnd(), couponFound.getCpnd());
            log.info("Coupons are identical");
            
            log.info("Retrieving all coupons for a specific offer url name");
            List<Coupon> couponsByOfferUrlName = offerCouponService.findCouponsByOfferUrlName(MULTIPLE_COUPONS_OFFER_TEST);
            assertEquals("Coupon list size is incorrect", 1, couponsByOfferUrlName.size(), 0);
            log.info("Retrieving coupons for offer url name worked");

            log.info("Retrieving all coupons for a specific branch url name");
            List<Coupon> couponsByBranch = offerCouponService.findCouponsByBranch(branch.getRlnm());
            assertEquals("Multiple coupons should not have been created", 1, couponsByBranch.size(), 1);
            log.info("Retrieving coupons for branch url name worked");
            
            log.info("Retrieve coupon user and compare coupons");
            UserSupplement us = userService.findUserSupplement(user.getCd());
            assertNotNull("User coupons are null", us.getCpns());
            assertEquals("User has incorrect number of coupons", 1, us.getCpns().size());
            Coupon userCoupon = us.getCpns().get(0);
            assertEquals("User coupon is incorrect", couponResult1.getCoupon().getCpnd(), userCoupon.getCpnd());
            log.info("Comparison was successful");

            log.info("Checking that the store associated with the offer got added to the user's list of saved stores");
            assertNotNull("User saved stores are null", us.getBrds().get(0));
            assertEquals("User has incorrect number of saved stores", 1, us.getBrds().get(0).getCrds().size());
            ListCard userSavedStore = us.getBrds().get(0).getCrds().get(0);
            assertEquals("User saved store is incorrect", STORE_URL_NAME, userSavedStore.getRlnm());
            log.info("Store was successfully added to user's list of saved stores");

            log.info("Now we're going to try to create a new coupon for the same user and same offer");
            log.info("This offer supports more than one coupon per person");
            CouponGenerationResult couponResult2 = offerCouponService.createCouponForItem(user.getCd(), new CouponGenerationRequest(MULTIPLE_COUPONS_OFFER_TEST, item.getRlnm()));
            assertNotNull("Coupon result is null", couponResult2);
            assertNotNull("Created coupon is missing", couponResult2.getCoupon());
            assertNotNull("Created coupon code is missing", couponResult2.getCoupon().getCpncd());
            log.info("We retrieved a coupon but it should be exactly the same as we created before. Let's compare");
            assertEquals("Coupons are not the same", couponResult1.getCoupon().getFfrrlnm(), couponResult2.getCoupon().getFfrrlnm());
            assertEquals("Coupons are not the same", couponResult1.getCoupon().getCpncd(), couponResult2.getCoupon().getCpncd());
            log.info("Coupons are identical");

        } catch (OfferValidationException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }

        log.info("Testing testOfferAndCouponWorkflow() successful");
    }

    /*
    @Test
    public void testCreateOfferWithOnePerPersonCoupons() {

        try {
            CouponGenerationResult couponResult = offerCouponService.createCouponForItem(user, new CouponGenerationRequest(SINGLE_COUPON_OFFER_TEST, item.getRlnm()));
            assertNotNull("Created coupon is missing", couponResult.getCoupon());
            Coupon couponFound = offerCouponService.findCoupon(couponResult.getCoupon().getCpncd());
            assertNotNull("Found coupon is missing", couponFound);
            assertEquals("Created and obtained offer not the same", couponResult.getCoupon().getCpnd(), couponFound.getCpnd());

            user = userService.findUserByEmail(user.getMl());
            assertNotNull("User coupons are null", user.getCpns());
            assertEquals("User has incorrect number of coupons", 1, user.getCpns().size());
            Coupon userCoupon = user.getCpns().get(0);
            assertEquals("User coupon is incorrect", couponResult.getCoupon().getCpnd(), userCoupon.getCpnd());
        } catch (OfferValidationException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testOfferAndCouponGenerations() {

        try {
            CouponGenerationResult coupon1 = offerCouponService.createCouponForItem(user, new CouponGenerationRequest(MULTIPLE_COUPONS_OFFER_TEST, item.getRlnm()));
            CouponGenerationResult coupon2 = offerCouponService.createCouponForItem(user, new CouponGenerationRequest(MULTIPLE_COUPONS_OFFER_TEST, item.getRlnm()));

            user = userService.findUserByEmail(user.getMl());

            assertEquals("Multiple coupons should not have been created", 1, user.getCpns().size(), 0);
            assertTrue("Same coupon should have been retrieved", coupon1.getCoupon().getCpncd().equals(coupon2.getCoupon().getCpncd()));

            coupon1 = offerCouponService.createCouponForItem(user, new CouponGenerationRequest(SINGLE_COUPON_OFFER_TEST, item.getRlnm()));
            coupon2 = offerCouponService.createCouponForItem(user, new CouponGenerationRequest(SINGLE_COUPON_OFFER_TEST, item.getRlnm()));

            user = userService.findUserByEmail(user.getMl());

            assertEquals("Multiple coupons should have been created", 2, user.getCpns().size(), 0);
            assertTrue("Same coupon should not have been retrieved", coupon1.getCoupon().getCpncd().equals(coupon2.getCoupon().getCpncd()));
        } catch (OfferValidationException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testCouponRedeemed() {

        try {
            CouponGenerationResult couponResult = offerCouponService.createCouponForItem(user, new CouponGenerationRequest(MULTIPLE_COUPONS_OFFER_TEST, item.getRlnm()));

            user = userService.findUserByEmail(user.getMl());
            assertEquals("Incorrect number of coupons created", 1, user.getCpns().size(), 0);
            Coupon userCoupon = user.getCpns().get(0);
            assertTrue("Generated coupon and user coupon do not match", couponResult.getCoupon().getCpncd().equals(userCoupon.getCpncd()));
            assertTrue("Coupon doesn't match offer", couponResult.getCoupon().getFfrrlnm().equals(MULTIPLE_COUPONS_OFFER_TEST));

            Date date = new Date();
            Coupon coupon = offerCouponService.redeemCoupon(couponResult.getCoupon().getCpncd());
            assertNotNull("Redeemed coupon is null", coupon);
            assertEquals("Redeemed date doesn't match", date, coupon.getRdmptndt());

            CouponGenerationResult couponResult2 = offerCouponService.createCouponForItem(user, new CouponGenerationRequest(MULTIPLE_COUPONS_OFFER_TEST, item.getRlnm()));
            assertNotNull("Create coupon returns null", couponResult2);
            assertEquals("New coupon shouldn't have been created", coupon.getCpncd(), couponResult2.getCoupon().getCpncd());

        } catch (OfferValidationException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testRemoveOrDisableOffer() {
        offerCouponService.removeOrDisableOffer(MULTIPLE_COUPONS_OFFER_TEST);
        Offer offerAfter = referenceDataService.findOfferByUrlName(MULTIPLE_COUPONS_OFFER_TEST);
        assertNull("Offer was not removed", offerAfter);
    }
    */
}
