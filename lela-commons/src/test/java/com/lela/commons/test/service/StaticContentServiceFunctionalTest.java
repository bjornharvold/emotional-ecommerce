/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.test.service;

import com.lela.commons.service.StaticContentService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.test.AbstractFunctionalTest;
import com.lela.domain.document.StaticContent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Bjorn Harvold
 * Date: 6/14/12
 * Time: 7:52 PM
 * Responsibility: Tests the analytics service
 */
public class StaticContentServiceFunctionalTest extends AbstractFunctionalTest {
    private final static Logger log = LoggerFactory.getLogger(StaticContentServiceFunctionalTest.class);
    private static final String THIS_IS_A_BIG_TEST = "This is a big test. ${o.nm}";
    private static final String THIS_IS_A_BIG_TEST_2 = "This is a big test. " + StaticContentServiceFunctionalTest.class.getSimpleName();

    @Autowired
    private StaticContentService staticContentService;

    private StaticContent staticContent;

    @Before
    public void setUp() {
        SpringSecurityHelper.secureChannel();

        staticContent = new StaticContent();
        staticContent.setNm(StaticContentServiceFunctionalTest.class.getSimpleName());
        staticContent.setRlnm(StaticContentServiceFunctionalTest.class.getSimpleName());
        staticContent.setSrlnm(StaticContentServiceFunctionalTest.class.getSimpleName());
        staticContent.setBdy(THIS_IS_A_BIG_TEST);
        staticContent.setVlctytmplt(true);

        staticContent = staticContentService.saveStaticContent(staticContent);
        assertNotNull("Static content ID is null", staticContent.getId());

        SpringSecurityHelper.unsecureChannel();
    }

    @After
    public void tearDown() {
        SpringSecurityHelper.secureChannel();
        if (staticContent != null) {
            staticContentService.removeStaticContent(staticContent.getRlnm());
        }
        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testDynamicContentGeneration() {
        log.info("Testing velocity integration with static content...");

        log.info("First we retrieve the static content object statically...");
        StaticContent sc1 = staticContentService.findStaticContentByUrlName(staticContent.getRlnm());
        assertNotNull("sc1 is null", sc1);
        assertEquals("sc1.bdy is incorrect", THIS_IS_A_BIG_TEST, sc1.getBdy());
        log.info("First we retrieve the static content object statically complete");

        log.info("Then we retrieve the static content object dynamically...");
        StaticContent sc2 = staticContentService.findStaticContentByUrlName(staticContent.getRlnm(), staticContent);
        assertNotNull("sc2 is null", sc2);
        assertEquals("sc1.bdy is incorrect", THIS_IS_A_BIG_TEST_2, sc2.getBdy());
        log.info("Then we retrieve the static content object dynamically complete");

        log.info("Testing velocity integration with static content complete");
    }
}
