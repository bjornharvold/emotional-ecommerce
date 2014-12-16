/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.data.web.controller.administration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.lela.commons.service.SeoUrlNameService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.data.web.AbstractFunctionalTest;
import com.lela.domain.document.SeoUrlName;
import com.lela.domain.enums.SeoUrlNameType;

/**
 * Created by Bjorn Harvold
 * Date: 8/3/11
 * Time: 10:05 AM
 * Responsibility:
 */
@SuppressWarnings("unchecked")
public class SeoUrlNameControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(SeoUrlNameControllerFunctionalTest.class);
    private static final String SEO_URL_NAME = "SeoUrlNameControllerFunctionalTest";

    private SeoUrlName seoUrlName = null;

    @Autowired
    private SeoUrlNameController seoUrlNameController;

    @Autowired
    private SeoUrlNameService seoUrlNameService;

    @Before
    public void setUp() {
        SpringSecurityHelper.secureChannel();
    }

    @After
    public void tearDown() {

        if (seoUrlName != null && seoUrlName.getId() != null) {
            seoUrlNameService.removeSeoUrlName(seoUrlName);
        }

        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testBlogController() {
        log.info("Testing seo url name controller...");
        Model model = new BindingAwareModelMap();

        try {
            log.info("First we retrieve all seo url names. There should be none but we won't fail the test if there is.");
            String view = seoUrlNameController.showSeoUrlNames(0, 12, model);
            assertEquals("Tile view is incorrect", "seo.list", view);
           
            log.info("Now we get ready to start creating a new seo url name by going to the seo url name form page");
            model = new BindingAwareModelMap();
            view = seoUrlNameController.showSeoUrlName(null, model);
            assertEquals("Tile view is incorrect", "seo.entry", view);
            assertNotNull("No seo url name object found", model.asMap().get(WebConstants.SEO_URL_NAME));
            seoUrlName = (SeoUrlName) model.asMap().get(WebConstants.SEO_URL_NAME);
            assertNull("SeoUrlName ID is not null", seoUrlName.getId());
            
            log.info("Ok, the seo object was created successfully. Let's populate the object.");
            seoUrlName.setNm(SEO_URL_NAME);
            seoUrlName.setSrlnm(SEO_URL_NAME);
            seoUrlName.setTp(SeoUrlNameType.FUNCTIONAL_FILTER_PRESET);
            seoUrlName.setDsc("dsc");
            seoUrlName.setHdr("hdr");
            seoUrlName.setNtr("ntr");

            model = new BindingAwareModelMap();
            BindingResult errors = new BindException(seoUrlName, "seoUrlName");
            RedirectAttributes ra = new RedirectAttributesModelMap();

            log.info("Let's save the seo url name");
            view = seoUrlNameController.saveSeoUrlName(seoUrlName, errors, ra, model, Locale.US);
            assertEquals("Tile view is incorrect", "redirect:/administration/seo/list", view);
            assertNotNull("Redirect flash attributes are empty", ra.getFlashAttributes());
            assertEquals("Redirect flash attributes are empty", 1, ra.getFlashAttributes().size());
            assertNotNull("Redirect flash attribute is empty", ra.getFlashAttributes().get(WebConstants.MESSAGE));
            
            log.info("Now we load up this seo again");
            model = new BindingAwareModelMap();
            view = seoUrlNameController.showSeoUrlName(seoUrlName.getSrlnm(), model);
            assertEquals("Tile view is incorrect", "seo.entry", view);
            assertNotNull("No seo object found", model.asMap().get(WebConstants.SEO_URL_NAME));
            seoUrlName = (SeoUrlName) model.asMap().get(WebConstants.SEO_URL_NAME);
            assertNotNull("SeoUrlName ID is null", seoUrlName.getId());

            log.info("Time to delete this seo url name and verify that it's been deleted");
            ra = new RedirectAttributesModelMap();
            view = seoUrlNameController.deleteSeoUrlName(seoUrlName.getSrlnm(), ra, Locale.US);
            assertEquals("Tile view is incorrect", "redirect:/administration/seo/list", view);
            assertNotNull("Redirect flash attributes are empty", ra.getFlashAttributes());
            assertNotNull("Redirect flash attribute is empty", ra.getFlashAttributes().get(WebConstants.MESSAGE));
            
            seoUrlName = seoUrlNameService.findSeoUrlName(seoUrlName.getSrlnm());
            assertNull("Seo url name didn't get deleted", seoUrlName);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        log.info("Testing admin blog controller complete");
    }

}
