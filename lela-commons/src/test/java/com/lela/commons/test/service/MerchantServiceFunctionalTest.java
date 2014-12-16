/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.test.service;

import com.lela.commons.service.BranchService;
import com.lela.commons.service.CategoryService;
import com.lela.commons.service.ItemService;
import com.lela.commons.service.PostalCodeService;
import com.lela.commons.service.ProfileService;
import com.lela.commons.service.StoreService;
import com.lela.domain.document.AvailableInStore;
import com.lela.domain.document.Branch;
import com.lela.domain.document.Item;
import com.lela.domain.document.PostalCode;
import com.lela.domain.document.Store;
import com.lela.domain.document.User;
import com.lela.domain.dto.BranchSearchResults;
import com.lela.domain.dto.LocationQuery;
import com.lela.domain.dto.Principal;
import com.lela.commons.remote.MerchantException;
import com.lela.commons.service.LocalCacheEvictionService;
import com.lela.commons.service.MerchantService;
import com.lela.commons.service.UserService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.test.AbstractFunctionalTest;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestOperations;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Bjorn Harvold
 * Date: 9/13/11
 * Time: 12:13 PM
 * Responsibility:
 */
public class MerchantServiceFunctionalTest extends AbstractFunctionalTest {
    private final static Logger log = LoggerFactory.getLogger(MerchantServiceFunctionalTest.class);
    private static final String ITEM_URL_NAME = "teststroller2";
    private static final String ITEM_URL_NAME_2 = "teststroller2";
    private static final String ITEM_CATEGORY = "stroller";

    private static final float LONGITUDE = -106.938716f;
    private static final float LATITUDE = 36.121582f;
    private static final float RADIUS = 20.0f;
    private static final String ZIPCODE = "87018";

    private static final String STORE_NAME = "teststorename";
    private static final Long STORE_TLD = 9900011L;
    private static final ObjectId STORE_ID = new ObjectId();
    private static final String STORE_URLNAME = "teststorerlnm";
    private static final String STORE_MERCHANT_ID = "TST9900011";
    private static final ObjectId BRANCH_ID = new ObjectId();

    private static final long BRANCH_TLD = 8800011L;
    private static final String BRANCH_NAME = "testbranchname";
    private static final String BRANCH_URLNAME = "testbranchrlnm";
    private static final String BRANCH_ADDRESS = "1313 Mockingbird Lane";
    private static final String BRANCH_CITY = "Counselor";
    private static final String BRANCH_STATE = "NM";

    private static final String SERVER_HOSTNAME = "www.lela.com";
    private static final String LOCALHOST = "127.0.0.1";
    private static final String IP_BRANCH_NAME = "testipaddress";
    private static final String IP_BRANCH_URLNAME = "testipaddress";
    private static final long IP_BRANCH_TLD = 9761100L;
    private static final ObjectId IP_BRANCH_ID = new ObjectId();

    private Item item;
    private Store store;
    private Branch branch;
    private Branch ipBranch;
    private String ipAddress;
    private Location ipLocation;

    @Value("${amazon.affiliate.id}")
    private String affiliateId;
    
    @Autowired
    private MerchantService merchantService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private LocalCacheEvictionService localCacheEvictionService;

    @Autowired
    private BranchService branchService;

    @Autowired
    @Qualifier("geoipLookupServiceIpv4")
    private LookupService lookupService;

    @Resource(name = "merchantRestTemplate")
    private RestOperations restTemplate;

    @Autowired
    private ItemService itemService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PostalCodeService postalCodeService;

    private User user;
    private PostalCode postalCode;

    @Before
    public void setup() {
        SpringSecurityHelper.secureChannel();

        user = createRandomUser(true);
        user = profileService.registerTestUser(user);

        // Load the item to be tested from a known state for each test
        localCacheEvictionService.evictItem(ITEM_URL_NAME_2);
        localCacheEvictionService.evictItemCategory(ITEM_CATEGORY);
        item = loadToDatabase(Item.class, ITEM_URL_NAME_2);
        
        // set up branch and store
        store = new Store();
        store.setNm(STORE_NAME);
        store.setId(STORE_ID);
        store.setTld(STORE_TLD);
        store.setRlnm(STORE_URLNAME);
        store.setMrchntd(STORE_MERCHANT_ID);
        store.setPprvd(true);

        storeService.saveStore(store);

        branch = new Branch();
        branch.setId(BRANCH_ID);
        branch.setTld(BRANCH_TLD);
        branch.setNm(BRANCH_NAME);
        branch.setRlnm(BRANCH_URLNAME);
        branch.setMrchntd(STORE_MERCHANT_ID);
        branch.setMrchntnm(STORE_NAME);
        branch.setDdrss(BRANCH_ADDRESS);
        branch.setCty(BRANCH_CITY);
        branch.setSt(BRANCH_STATE);
        branch.setZp(ZIPCODE);
        branch.setLc(new Float[] { LONGITUDE, LATITUDE });
        branch.setPprvd(true);

        branchService.saveBranch(branch);

        // Find the server ip address
        ipAddress = findServerIpAddress();

        // Find the zipcode for the ip address directly using the lookup
        ipLocation = lookupService.getLocation(ipAddress);
        if (ipLocation != null) {
            // Create a branch for the test store for the given zipcode to ensure there is one result minimum
            ipBranch = new Branch();
            ipBranch.setId(IP_BRANCH_ID);
            ipBranch.setTld(IP_BRANCH_TLD);
            ipBranch.setNm(IP_BRANCH_NAME);
            ipBranch.setRlnm(IP_BRANCH_URLNAME);
            ipBranch.setMrchntd(STORE_MERCHANT_ID);
            ipBranch.setMrchntnm(STORE_NAME);
            ipBranch.setCty(ipLocation.city);
            ipBranch.setSt(ipLocation.region);
            ipBranch.setZp(ipLocation.postalCode);
            ipBranch.setLc(new Float[] { ipLocation.longitude, ipLocation.latitude });
            ipBranch.setPprvd(true);

            branchService.saveBranch(ipBranch);
        }

        postalCode = new PostalCode();
        postalCode.setCd(ZIPCODE);
        Float[] lc = new Float[2];
        lc[0] = LONGITUDE;
        lc[1] = LATITUDE;
        postalCode.setLc(lc);
        postalCode = postalCodeService.savePostalCode(postalCode);

        SpringSecurityHelper.unsecureChannel();
    }

    @After
    public void teardown() {
        SpringSecurityHelper.secureChannel();

        if (user != null) {
            userService.removeUser(user);
        }

        if (store != null) {
            storeService.removeStore(store.getRlnm());
            localCacheEvictionService.evictStore(store.getRlnm());
        }

        if (branch != null) {
            branchService.removeBranch(branch.getRlnm());
        }

        if (ipBranch != null) {
            branchService.removeBranch(ipBranch.getRlnm());
        }

        if (postalCode != null) {
            postalCodeService.removePostalCode(postalCode.getCd());
        }

        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testMerchantServiceException() {
        log.info("Testing exception for merchant service...");

        MerchantException e = new MerchantException();

        e = new MerchantException("test1");
        assertTrue("Error message not correct", e.getMessage().equals("test1"));

        e = new MerchantException("test2", new Throwable("cause2"));
        assertTrue("Error message not correct", e.getMessage().equals("test2"));
        assertTrue("Error cause not correct", e.getCause().getMessage().equals("cause2"));

        e = new MerchantException("test3", new Throwable("cause3"), "one3", "two3");
        assertTrue("Error message not correct", e.getMessage().equals("test3"));
        assertTrue("Error cause not correct", e.getCause().getMessage().equals("cause3"));
        String[] params = e.getParams();
        assertTrue("Error message param not correct", params[0].equals("one3"));
        assertTrue("Error message param not correct", params[1].equals("two3"));

        e = new MerchantException("test4", "one4", "two4");
        assertTrue("Error message not correct", e.getMessage().equals("test4"));
        params = e.getParams();
        assertTrue("Error message param not correct", params[0].equals("one4"));
        assertTrue("Error message param not correct", params[1].equals("two4"));

        log.info("Testing exception for merchant service complete");
    }

    /**
     * Test that a search by lat and long will return a result correctly
     */
    @Test
    public void testSearchByLatLong() {

        /*
        {
        "longitude": "-73.986171",
        "latitude": "40.748716",
        "radius": "10.0"
        }
        */
        LocationQuery query = new LocationQuery();
        query.setLongitude(LONGITUDE);
        query.setLatitude(LATITUDE);
        query.setRadius(RADIUS);

        Item item = itemService.findItemByUrlName(ITEM_URL_NAME);

        try {
            BranchSearchResults branches = retrieveBranchResults(query, item);
            assertNotNull("No branch result list", branches.getResults());
            assertTrue("No branches returned in result list", !branches.getResults().isEmpty());

            assertEquals("Incorrect branch urlname returned in lat/lon search", BRANCH_URLNAME, branches.getResults().get(0).getBranch().getRlnm());

        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test that a lat/long search will not just return a random branch
     */
    @Test
    public void testSearchByLatLongFails() {

        // Explicitly remove the branch to make sure zero results are returned
        removeBranch();

        /*
        {
        "longitude": "-73.986171",
        "latitude": "40.748716",
        "radius": "10.0"
        }
        */
        LocationQuery query = new LocationQuery();
        query.setLongitude(LONGITUDE);
        query.setLatitude(LATITUDE);
        query.setRadius(RADIUS);

        Item item = itemService.findItemByUrlName(ITEM_URL_NAME);

        try {
            BranchSearchResults branches = retrieveBranchResults(query, item);
            assertNotNull("No branch result list", branches.getResults());
            assertTrue("Branches returned in result list when it should be empty", branches.getResults().isEmpty());

        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test that zipcode search returns a result
     */
    @Test
    public void testSearchByZipcode() {

        LocationQuery query = new LocationQuery();
        query.setZipcode(ZIPCODE);
        query.setRadius(RADIUS);

        Item item = itemService.findItemByUrlName(ITEM_URL_NAME);

        try {
            BranchSearchResults branches = retrieveBranchResults(query, item);
            assertNotNull("No branch result list", branches.getResults());
            assertTrue("No branches returned in result list", !branches.getResults().isEmpty());

            assertEquals("Incorrect branch urlname returned in zipcode search", BRANCH_URLNAME, branches.getResults().get(0).getBranch().getRlnm());

        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test that zipcode search won't just return random result
     */
    @Test
    public void testSearchByZipcodeFails() {

        // Explicitly remove the branch to make sure zero results are returned
        removeBranch();

        /*
        {
        "zipcode": "10001",
        "radius": "10.0"
        }
        */
        LocationQuery query = new LocationQuery();
        query.setZipcode(ZIPCODE);
        query.setRadius(RADIUS);

        Item item = itemService.findItemByUrlName(ITEM_URL_NAME);

        try {
            BranchSearchResults branches = retrieveBranchResults(query, item);
            assertNotNull("No branch result list", branches.getResults());
            assertTrue("Branches returned in result list when it should be empty", branches.getResults().isEmpty());

        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test that search by IP address returns correct result
     */
    @Test
    public void testSearchByIpAddress() {

        // Make sure that the IP address branch was set up
        assertNotNull("IP Address Branch was not set up", ipBranch);

        /*
        {
        "ipAddress": "10001",
        "radius": "10.0"
        }
        */
        LocationQuery query = new LocationQuery();
        query.setIpAddress(ipAddress);
        query.setRadius(RADIUS);

        Item item = itemService.findItemByUrlName(ITEM_URL_NAME);

        try {
            BranchSearchResults branches = retrieveBranchResults(query, item);
            assertNotNull("No branch result list", branches.getResults());
            assertTrue("No branches returned in result list", !branches.getResults().isEmpty());

            assertEquals("Incorrect branch urlname returned in zipcode search", ipBranch.getRlnm(), branches.getResults().get(0).getBranch().getRlnm());

        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test that search by ip address (Localhost) returns zero branches
     */
    @Test
    public void testSearchByIpAddressFindsZeroBranches() {

        // Make sure that the IP address branch was set up
        assertNotNull("IP Address Branch was not set up", ipBranch);

        /*
        {
        "ipAddress": "10001",
        "radius": "10.0"
        }
        */
        LocationQuery query = new LocationQuery();
        query.setIpAddress(LOCALHOST);
        query.setRadius(RADIUS);

        Item item = itemService.findItemByUrlName(ITEM_URL_NAME);

        try {
            BranchSearchResults branches = retrieveBranchResults(query, item);
            assertNotNull("No branch result list", branches.getResults());
            assertTrue("Branches returned in result list when it should be empty", branches.getResults().isEmpty());
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test that if there are zero Online stores, no local results will be returned, but that local results
     * WOULD have been returned if there were online stores
     */
    @Test
    public void testSearchZeroResultsWithZeroOnlineStores() {

        /*
        {
        "longitude": "-73.986171",
        "latitude": "40.748716",
        "radius": "10.0"
        }
        */
        LocationQuery query = new LocationQuery();
        query.setLongitude(LONGITUDE);
        query.setLatitude(LATITUDE);
        query.setRadius(RADIUS);

        Item item = itemService.findItemByUrlName(ITEM_URL_NAME);

        try {

            //
            // Ensure that the correct result is returned when a store is provided
            //
            BranchSearchResults branches = retrieveBranchResults(query, item);
            assertNotNull("No branch result list", branches.getResults());
            assertTrue("No branches returned in result list", !branches.getResults().isEmpty());

            assertEquals("Incorrect branch urlname returned in lat/lon search", BRANCH_URLNAME, branches.getResults().get(0).getBranch().getRlnm());

            //
            // Ensure that zero stores are returned when no results are provided
            //
            branches = merchantService.findLocalStoresForItem(item, query, new ArrayList<AvailableInStore>());
            assertNotNull("No branch result list", branches.getResults());
            assertTrue("Branches returned in result list when it should be empty", branches.getResults().isEmpty());
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test that if there are zero Online stores, the latitude and longitude are returned for ip address
     */
    @Test
    public void testIpLookupForZeroOnlineStores() {

        // Make sure that the IP address location was determined
        assertNotNull("IP Address Location was not set up", ipLocation);

        LocationQuery query = new LocationQuery();
        query.setIpAddress(ipAddress);
        query.setRadius(RADIUS);

        Item item = itemService.findItemByUrlName(ITEM_URL_NAME);

        try {

            //
            // Ensure that lat and long are returned even though no online stores
            //
            BranchSearchResults branches = merchantService.findLocalStoresForItem(item, query, new ArrayList<AvailableInStore>());
            assertNotNull("No branch result list", branches.getResults());
            assertTrue("Branches returned in result list when it should be empty", branches.getResults().isEmpty());
            
            assertEquals("Incorrect latitude", (Object)ipLocation.latitude, (Object)branches.getLatitude());
            assertEquals("Incorrect longitude", (Object)ipLocation.longitude, (Object)branches.getLongitude());
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test that search with no arguments will return zero results
     */
    @Test
    public void testSearchWithNoArguments() {

        LocationQuery query = new LocationQuery();
        Item item = itemService.findItemByUrlName(ITEM_URL_NAME);

        try {

            //
            // Ensure that the correct result is returned when a store is provided
            //
            BranchSearchResults branches = retrieveBranchResults(query, item);
            assertNotNull("No branch result list", branches.getResults());
            assertTrue("Branches returned in result list when it should be empty", branches.getResults().isEmpty());

        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
    }

    private BranchSearchResults retrieveBranchResults(LocationQuery query, Item item) {
        AvailableInStore inStore = new AvailableInStore();
        inStore.populateStore(store);
        inStore.setLdt(new Date());

        List<AvailableInStore> inStores = new ArrayList<AvailableInStore>();
        inStores.add(inStore);

        return merchantService.findLocalStoresForItem(item, query, inStores);
    }

    private void removeBranch() {
        // Explicitly remove the branch in the zipcode
        SpringSecurityHelper.secureChannel();
        branchService.removeBranch(branch.getRlnm());
        SpringSecurityHelper.unsecureChannel();
    }

    private String findServerIpAddress() {
        try {
            InetAddress addr = InetAddress.getByName(SERVER_HOSTNAME);
            return addr.getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException("Could not find IP Address of server", e);
        }
    }
}
