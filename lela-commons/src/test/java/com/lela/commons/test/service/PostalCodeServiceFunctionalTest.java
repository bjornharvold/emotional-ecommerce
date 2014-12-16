/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.test.service;

import com.lela.commons.service.PostalCodeService;
import com.lela.commons.service.SearchException;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.test.AbstractFunctionalTest;
import com.lela.domain.document.PostalCode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by Bjorn Harvold
 * Date: 6/23/11
 * Time: 11:16 AM
 * Responsibility:
 */
public class PostalCodeServiceFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(PostalCodeServiceFunctionalTest.class);
    private static final String POSTAL_CODE_URL_NAME = "PostalCodeServiceFunctionalTest";
    private static final String POSTAL_ZIP_CODE = "999999999999";
    private static final String POSTAL_CITY = "NINABURG";
    private static final String POSTAL_STATE = "NINASTATE";
    private static final Float LATITUDE = 1f;
    private static final Float LONGITUDE = 1f;

    @Autowired
    private PostalCodeService postalCodeService;

    @Autowired
    private MongoTemplate mongo;

    private PostalCode postalCode;

    @Before
    public void setUp() {

        log.info("Set up " + this.getClass().getSimpleName() + "...");
        SpringSecurityHelper.secureChannel();

        // create postal code
        postalCode = new PostalCode();
        Float[] lc = new Float[2];
        lc[0] = LATITUDE;
        lc[1] = LONGITUDE;
        postalCode.setLc(lc);
        postalCode.setCd(POSTAL_ZIP_CODE);
        postalCode.setCt(POSTAL_CITY);
        postalCode.setPpltn(1);
        postalCode.setSt(POSTAL_STATE);
        postalCode.setStnm(POSTAL_STATE);

        postalCode = postalCodeService.savePostalCode(postalCode);

        SpringSecurityHelper.unsecureChannel();

        log.info("Set up complete");
    }

    @After
    public void tearDown() {
        log.info("Starting tear down...");
        SpringSecurityHelper.secureChannel();

        // remove store
        if (postalCode != null) {
            mongo.remove(query(where("cd").is(POSTAL_ZIP_CODE)), PostalCode.class);
            postalCode = postalCodeService.findPostalCodeByCode(POSTAL_ZIP_CODE);
            assertNull("Postal code did not get deleted", postalCode);
        }

        SpringSecurityHelper.unsecureChannel();
        log.info("Tear down complete");
    }

    @Test
    public void testFindPostalCodeByCode() {
        log.info("Finding postal code by code");

        PostalCode postalCode1 = postalCodeService.findPostalCodeByCode(POSTAL_ZIP_CODE);
        assertNotNull("Postal code is null", postalCode1);

        log.info("Finding postal code by code complete.");
    }

    @Test
    public void testFindPostalCodeByCityAndStateName() {
        log.info("Finding postal code by city and state name");

        PostalCode postalCode1 = postalCodeService.findPostalCodeByCityAndStateName(POSTAL_CITY, POSTAL_STATE);
        assertNotNull("Postal code is null", postalCode1);

        log.info("Finding postal code by city and state name complete.");
    }
}
