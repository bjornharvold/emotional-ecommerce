/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.test.service;

import com.lela.commons.service.FunctionalFilterService;
import com.lela.commons.service.LocalCacheEvictionService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.test.AbstractFunctionalTest;
import com.lela.domain.document.FunctionalFilter;
import com.lela.domain.document.FunctionalFilterOption;
import com.lela.domain.enums.FunctionalFilterDomainType;
import com.lela.domain.enums.FunctionalFilterType;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Created by Bjorn Harvold
 * Date: 6/4/12
 * Time: 7:53 PM
 * Responsibility:
 */
public class FunctionalFilterFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(FunctionalFilterFunctionalTest.class);
    private static final String SOMEFUNCTIONCALLYFILTEREDCATEGORY = "somefunctioncallyfilteredcategory";
    private static final String SOMEFILTERKEY = "somefilterkey";

    @Autowired
    private FunctionalFilterService functionalFilterService;

    @Autowired
    private LocalCacheEvictionService localCacheEvictionService;

    @Test
    public void testFunctionalFilter() {
        SpringSecurityHelper.unsecureChannel();

        log.info("Create a functional filter and save it");
        FunctionalFilter ff = new FunctionalFilter();
        ff.setId(new ObjectId());
        ff.setTp(FunctionalFilterType.DYNAMIC_RANGE);
        ff.setDtp(FunctionalFilterDomainType.CATEGORY);
        ff.setRlnm(SOMEFUNCTIONCALLYFILTEREDCATEGORY);
        ff.setKy(SOMEFILTERKEY);
        ff.setRdr(1);
        ff.setLcl("en_US");
        List<FunctionalFilterOption> values = new ArrayList<FunctionalFilterOption>();
        values.add(new FunctionalFilterOption("high", 3, 1, false));
        values.add(new FunctionalFilterOption("medium", 2, 2, false));
        values.add(new FunctionalFilterOption("low", 1, 3, false));
        ff.setPtns(values);

        try {
            functionalFilterService.saveFunctionalFilter(ff);
            fail("Functional filter should not be able to be saved here. Missing credentials.");
        } catch (Exception ex) {
            log.info("Tried to save functional filter without credentials. An exception was expected: " + ex.getMessage());
        }

        log.info("Securing channel...");
        SpringSecurityHelper.secureChannel();
        log.info("Channel secured");

        log.info("Saving a functional filter. This time with a secure channel");
        try {
            ff = functionalFilterService.saveFunctionalFilter(ff);
            assertNotNull("Functional filter is missing an id", ff.getId());
            log.info("Functional filter persisted successfully");
        } catch (Exception ex) {
            fail("Did not expect an exception here: " + ex.getMessage());
            log.info("Was not able to persist a metric within secure context", ex);
        }

        log.info("Retrieving functional filter...");
        ff = functionalFilterService.findFunctionalFilterByUrlNameAndKey(SOMEFUNCTIONCALLYFILTEREDCATEGORY, SOMEFILTERKEY);
        assertNotNull("Functional filter is missing", ff);
        assertNotNull("Functional filter is missing an id", ff.getId());
        log.info("Functional filter retrieved successfully");

        List<FunctionalFilter> ffs = functionalFilterService.findFunctionalFiltersByUrlName(SOMEFUNCTIONCALLYFILTEREDCATEGORY);
        assertNotNull("Functional filters is null", ffs);
        assertNotNull("Functional filters list is null", ffs);
        assertEquals("Functional filters list size is incorrect", 1, ffs.size(), 0);

        log.info("Deleting functional filter...");
        functionalFilterService.removeFunctionalFilter(ff.getId());

        localCacheEvictionService.evictFunctionalFilter(ff.getRlnm());

        ff = functionalFilterService.findFunctionalFilterByUrlNameAndKey(SOMEFUNCTIONCALLYFILTEREDCATEGORY, SOMEFILTERKEY);
        assertNull("Functional filter still exists", ff);
        ffs = functionalFilterService.findFunctionalFiltersByUrlName(SOMEFUNCTIONCALLYFILTEREDCATEGORY);
        assertNull("Functional filter still exists", ffs);

        log.info("Deleted functional filter successfully");
        log.info("Test complete!");
    }
}
