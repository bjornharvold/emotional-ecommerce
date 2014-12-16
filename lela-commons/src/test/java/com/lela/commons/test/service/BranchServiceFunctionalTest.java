/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.test.service;

import com.lela.commons.service.BranchService;
import com.lela.commons.service.StoreService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.test.AbstractFunctionalTest;
import com.lela.domain.document.Branch;
import com.lela.domain.document.Store;
import com.lela.domain.dto.BranchSearchResult;
import com.lela.domain.dto.BranchSearchResults;
import com.lela.domain.dto.LocationQuery;
import com.lela.domain.dto.store.BranchDistance;
import com.lela.domain.dto.store.LocalStoresSearchResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Bjorn Harvold
 * Date: 6/23/11
 * Time: 11:16 AM
 * Responsibility:
 */
public class BranchServiceFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(BranchServiceFunctionalTest.class);
    private static final String BRANCH_URL_NAME = "BranchServiceFunctionalTest";
    private static final String BRANCH_LOCAL_CODE = "BranchServiceFunctionalTestLocalCode";
    private static final String BRANCH_AFFILIATE_URL_NAME = "BranchServiceFunctionalTestAffiliate";
    private static final String BRANCH_MERCHANT_ID = "BranchServiceFunctionalTestMerchant";
    private static final Float LATITUDE = 0f;
    private static final Float LONGITUDE = 0f;

    private static final String BRANCH_ZIP_CODE = "888888888";

    @Autowired
    private BranchService branchService;

    @Autowired
    private StoreService storeService;
    
    private Branch branch;
    private Store store;

    @Before
    public void setUp() {
        log.info("Setting up " + this.getClass().getSimpleName() + "...");

        SpringSecurityHelper.secureChannel();

        branch = new Branch();
        branch.setMrchntd(BRANCH_MERCHANT_ID);
        branch.setMrchntnm(RandomStringUtils.randomAlphabetic(10));
        branch.setLclnly(true);
        branch.setStrnmbr(1L);
        branch.setPprvd(true);
        branch.setNm(RandomStringUtils.randomAlphabetic(10));
        branch.setRlnm(BRANCH_URL_NAME);
        branch.setDdrss(RandomStringUtils.randomAlphabetic(10));
        branch.setCty(RandomStringUtils.randomAlphabetic(10));
        branch.setSt(RandomStringUtils.randomAlphabetic(10));
        branch.setZp(BRANCH_ZIP_CODE);
        branch.setLclcd(BRANCH_LOCAL_CODE);
        branch.setFfltrlnm(BRANCH_AFFILIATE_URL_NAME);

        Float[] lc = new Float[2];
        lc[0] = LATITUDE;
        lc[1] = LONGITUDE;

        branch.setLc(lc);

        branch = branchService.saveBranch(branch);

        store = new Store(BRANCH_MERCHANT_ID);
        store.setRlnm(BRANCH_URL_NAME);
        store = storeService.saveStore(store);
        
        SpringSecurityHelper.unsecureChannel();

        log.info("Set up complete");
    }

    @After
    public void tearDown() {
        log.info("Tearing down...");
        SpringSecurityHelper.secureChannel();

        // remove branch
        if (branch != null) {
            branchService.removeBranch(BRANCH_URL_NAME);

            branch = branchService.findBranchByUrlName(BRANCH_URL_NAME);

            assertNull("Branch did not get deleted", branch);
        }

        // remove store
        if (store != null) {
            storeService.removeStore(BRANCH_URL_NAME);

            store = storeService.findStoreByUrlName(BRANCH_URL_NAME);

            assertNull("Store did not get deleted", store);
        }
        
        

        SpringSecurityHelper.unsecureChannel();
        log.info("Tear down complete");
    }

    @Test
    public void testFindBranchByUrlName() {
        log.info("Finding branch by url name");

        Branch branch1 = branchService.findBranchByUrlName(BRANCH_URL_NAME);
        assertNotNull("Branch is null", branch1);

        log.info("Finding branch by url name complete.");
    }

    @Test
    public void testFindBranchByLocationCode() {
        log.info("Finding branch by local code");

        Branch branch1 = branchService.findBranchByLocalCode(BRANCH_LOCAL_CODE);
        assertNotNull("Branch is null", branch1);

        log.info("Finding branch by local code complete.");
    }

    @Test
    public void testFindBranchesByAffiliateUrlName() {
        log.info("Finding branches by affiliate url name");

        List<Branch> branches = branchService.findBranchesByAffiliateUrlName(BRANCH_AFFILIATE_URL_NAME);
        assertNotNull("Branches is null", branches);
        assertEquals("Branch result size incorrect", 1, branches.size(), 0);

        log.info("Finding branches by affiliate url name complete.");
    }

    @Test
    public void testFindBranchesByMerchantId() {
        log.info("Finding branches by merchant id");

        List<Branch> branches = branchService.findBranchesByMerchantId(BRANCH_MERCHANT_ID);
        assertNotNull("Branches is null", branches);
        assertEquals("Branch result size incorrect", 1, branches.size(), 0);

        log.info("Finding branches by merchant id complete.");
    }

    @Test
    public void testFindNearest() {
        log.info("Finding branches by coordinates");

        List<BranchDistance> branches = branchService.findNearest(LONGITUDE, LATITUDE, 1f);
        assertNotNull("Branches is null", branches);
        assertEquals("Branch result size incorrect", 1, branches.size(), 0);

        Set<String> merchantIds = new HashSet<String>();
        merchantIds.add(BRANCH_MERCHANT_ID);
        List<BranchSearchResult> results = branchService.findNearest(LONGITUDE, LATITUDE, 1f, merchantIds);
        assertNotNull("Branches is null", results);
        assertEquals("Branch result size incorrect", 1, results.size(), 0);

        log.info("Finding branches by coordinates.");
    }

    /*
    TODO this requires a bunch more objects to be set up
    @Test
    public void testFindLocalStoreAggregateDetails() {
        log.info("Finding local store aggregate details");

        LocationQuery locationQuery = new LocationQuery();
        locationQuery.setLongitude(LONGITUDE);
        locationQuery.setLatitude(LATITUDE);
        locationQuery.setZipcode(BRANCH_ZIP_CODE);

        LocalStoresSearchResult lresults = branchService.findLocalStoreAggregateDetails(locationQuery);
        assertNotNull("Branches is null", lresults);
        assertNotNull("Branches is null", lresults.getStores());
        assertEquals("Branch result size incorrect", 1, lresults.getStores().size(), 0);

        log.info("Finding local store aggregate details complete");
    }
    */
}
