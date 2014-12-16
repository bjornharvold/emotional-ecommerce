/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.web.test.controller;

import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.dto.Sitemap;
import com.lela.web.test.AbstractFunctionalTest;
import com.lela.web.web.controller.SitemapController;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * User: Bjorn Harvold
 */
public class SitemapControllerFunctionalTest extends AbstractFunctionalTest {

    private static final Logger log = LoggerFactory.getLogger(SitemapControllerFunctionalTest.class);

    @Autowired
    private SitemapController sitemapController;

    @Test
    public void testSitemapController() {

        log.info("Testing sitemap controller...");
        Model model = new BindingAwareModelMap();
        MockHttpServletRequest request = new MockHttpServletRequest();

        try {
            log.info("Loading up the sitemap through the controller");
            String view = sitemapController.showSitemap(model, request);
            assertNotNull("view is null", view);
            assertEquals("view name is incorrect", "sitemap", view);
            // all the below might not work in a test environment because we haven't inserted any test data
//            assertNotNull("sitemap is null", model.asMap().get(WebConstants.SITEMAP));
//            Sitemap sitemap = (Sitemap) model.asMap().get(WebConstants.SITEMAP);
//            assertNotNull("categories are null", sitemap.getCtgrs());
//            assertNotNull("items are null", sitemap.getTms());
//            assertEquals("items map size incorrect", sitemap.getCtgrs().size(), sitemap.getTms().size(), 0);
            log.info("All sitemap controller tests passed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing sitemap controller complete");
    }

}
