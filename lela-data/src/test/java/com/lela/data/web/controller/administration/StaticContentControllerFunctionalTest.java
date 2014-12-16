/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.data.web.controller.administration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.lela.commons.service.LocalCacheEvictionService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.data.web.AbstractFunctionalTest;
import com.lela.domain.document.StaticContent;
import com.lela.domain.dto.staticcontent.StaticContentEntry;

/**
 * Created by Bjorn Harvold
 * Date: 8/3/11
 * Time: 10:05 AM
 * Responsibility:
 */
@SuppressWarnings("unchecked")
public class StaticContentControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(StaticContentControllerFunctionalTest.class);
    private RedirectAttributes redirectAttributes;
    private Locale locale;
    
    StaticContent sc;
    @Autowired
    private StaticContentController staticContentController;

    @Autowired
    private LocalCacheEvictionService localCacheEvictionService;

    @Before
    public void setUp() {
        SpringSecurityHelper.secureChannel();
        this.redirectAttributes = new RedirectAttributesModelMap();
        this.locale = Locale.US;
    }

    @After
    public void tearDown() {
        SpringSecurityHelper.unsecureChannel();
    }


    @Test
    public void testStaticContent() {
        log.info("Testing static content controller...");
        Model model = new BindingAwareModelMap();

        try {
            log.info("First we want to save a piece of static content");
            sc = new StaticContent();
            sc.setBdy("blah");
            sc.setNm("Blah");
            sc.setRlnm("blah");
            
            StaticContentEntry sce = new StaticContentEntry(sc);

            BindingResult errors = new BindException(sc, "staticContent");
            String view = staticContentController.saveStaticContent(new StaticContentEntry(sc), errors, model, redirectAttributes, locale);
            assertEquals("Tile view is incorrect", "redirect:/administration/static/content?urlName=" + sc.getRlnm(), view);

            log.info("Then we want to retrieve a paginated list of static content items");
            view = staticContentController.showStaticContents(0, 10, model);
            assertEquals("Tile view is incorrect", "static.content.list", view);
            assertNotNull("Static content list is null", model.asMap().get(WebConstants.STATIC_CONTENTS));
            Page<StaticContent> page = (Page<StaticContent>) model.asMap().get(WebConstants.STATIC_CONTENTS);

            log.info("Then we want to retrieve the original static content we created");
            model = new BindingAwareModelMap();
            view = staticContentController.showStaticContent(sc.getRlnm(), model);
            assertEquals("Tile view is incorrect", "static.content.form", view);
            assertNotNull("Static content is null", model.asMap().get(WebConstants.STATIC_CONTENT));

            log.info("Then we want to update some attributes on this static content");
            sce = (StaticContentEntry) model.asMap().get(WebConstants.STATIC_CONTENT);
            sce.getStaticContent().setNm("blah2");

            view = staticContentController.saveStaticContent(new StaticContentEntry(sce.getStaticContent()), errors, model, redirectAttributes, locale);
            assertEquals("Tile view is incorrect", "redirect:/administration/static/content?urlName=" + sce.getStaticContent().getRlnm(), view);

            log.info("Then we want to retrieve it again and check that the attributes have indeed been updated");
            model = new BindingAwareModelMap();
            view = staticContentController.showStaticContent(sce.getStaticContent().getRlnm(), model);
            assertEquals("Tile view is incorrect", "static.content.form", view);
            assertNotNull("Static content is null", model.asMap().get(WebConstants.STATIC_CONTENT));

            sce = (StaticContentEntry) model.asMap().get(WebConstants.STATIC_CONTENT);
            assertEquals("Name is incorrect", "blah2", sce.getStaticContent().getNm());

            log.info("Then we want to delete the static content completely");
            view = staticContentController.deleteStaticContent(sce.getStaticContent().getRlnm());
            assertEquals("Tile view is incorrect", "redirect:/administration/static/content/list", view);

            localCacheEvictionService.evictStaticContent(sce.getStaticContent().getRlnm());

            log.info("Then we want to verify that the static content has been removed and instead replaced by a new instance of the object but with the url name specified");
            model = new BindingAwareModelMap();
            view = staticContentController.showStaticContent(sce.getStaticContent().getRlnm(), model);
            assertEquals("Tile view is incorrect", "static.content.form", view);
            assertNotNull("Static content is null", model.asMap().get(WebConstants.STATIC_CONTENT));

            sce = (StaticContentEntry) model.asMap().get(WebConstants.STATIC_CONTENT);
            assertEquals("Property is not null", "blah", sce.getStaticContent().getRlnm());

            log.info("Finally, we want to verify that the static content has been removed and instead replaced by a new instance of the object");
            model = new BindingAwareModelMap();
            view = staticContentController.showStaticContent(null, model);
            assertEquals("Tile view is incorrect", "static.content.form", view);
            assertNotNull("Static content is null", model.asMap().get(WebConstants.STATIC_CONTENT));

            sce = (StaticContentEntry) model.asMap().get(WebConstants.STATIC_CONTENT);
            assertNull("Property is not null", sce.getStaticContent().getRlnm());

        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        } finally {
        	try {
        		//Delete any URL we may have inserted
        		if (!StringUtils.isEmpty(sc.getRlnm())){
        			staticContentController.deleteStaticContent(sc.getRlnm());
        		}
        	}catch (Exception e){
        		e.printStackTrace();
        	}
        }

        log.info("Testing static content controller complete");
    }
    
   

}
