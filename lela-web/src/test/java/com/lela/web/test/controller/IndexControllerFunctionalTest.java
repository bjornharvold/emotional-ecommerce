/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.test.controller;

import com.lela.commons.service.StaticContentService;
import com.lela.commons.spring.mobile.MockDevice;
import com.lela.commons.spring.mobile.WurflDevice;
import com.lela.domain.document.StaticContent;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.web.test.AbstractFunctionalTest;
import com.lela.web.web.controller.IndexController;
import com.lela.commons.web.utils.WebConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Bjorn Harvold
 * Date: 8/3/11
 * Time: 10:05 AM
 * Responsibility:
 */
@SuppressWarnings("unchecked")
public class IndexControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(IndexControllerFunctionalTest.class);
    private static final String URL_NAME = "kjljlkjlkljljlkj";

    private MockDevice mockDevice = new MockDevice(MockDevice.DEVICE_TYPE.NORMAL);
    private Device device = new WurflDevice(mockDevice);

    @Autowired
    private IndexController indexController;

    @Autowired
    private StaticContentService staticContentService;

    private StaticContent sc = null;
    
    @Before
    public void setUp() {
        SpringSecurityHelper.secureChannel();
        sc = new StaticContent();
        sc.setNm(URL_NAME);
        sc.setRlnm(URL_NAME);
        
        sc = staticContentService.saveStaticContent(sc);
        
        assertNotNull("Static content did not get persisted", sc.getId());
    }
    
    @After
    public void tearDown() {
        if (sc != null) {
            staticContentService.removeStaticContent(sc.getRlnm());
        }
        SpringSecurityHelper.unsecureChannel();
    }
    
    @Test
    public void testIndex() {
        log.info("Testing index controller...");
        MockHttpServletRequest request = new MockHttpServletRequest();
        HttpSession session = new MockHttpSession();
        request.setSession(session);

        try {
            log.info("First we want to test index page with a user who has no motivators. A recent Lela list should be returned.");
            Model model = new BindingAwareModelMap();
            String view = indexController.showIndex(model, device, request, session);
            assertEquals("Tile view is incorrect", "index", view);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        log.info("Testing index controller complete");
    }

    /*
    @Test
    public void testDispatcher() {
        log.info("Testing dispatcher controller...");

        try {
            String view = "test";
            view = indexController.dispatch(view);
            assertEquals("Tile view is incorrect", "test", view);
        } catch (Exception e) {
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        log.info("Testing dispatching controller complete");
    }
    */

    @Test
    public void testFacebook() {
        log.info("Testing facebook controller...");

        try {
            String view = indexController.facebook();
            assertEquals("Tile view is incorrect", "facebook.page", view);
        } catch (Exception e) {
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        log.info("Testing static page controller complete");
    }

    @Test
    public void testStaticPage() {
        log.info("Testing static page controller...");

        try {
            String view = "test";
            view = indexController.dispatchStatic(view);
            assertEquals("Tile view is incorrect", "static.page", view);
        } catch (Exception e) {
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        log.info("Testing static page controller complete");
    }

    @Test
    public void testDispatchStatic() {
        log.info("Testing dispatch static page controller...");

        try {
            String view = "test";
            Model model = new BindingAwareModelMap();
            
            log.info("We are expecting an exception here because we don't have this static page");
            indexController.dispatchStatic(view, model);
        } catch (Exception e) {
            assertNotNull("Method did not throw expected exception: " + e.getMessage());
        }

        try {
            Model model = new BindingAwareModelMap();
            
            log.info("We are expecting an exception here because we don't have this static page");
            String view = indexController.dispatchStatic(URL_NAME, model);
            assertEquals("Tile view is incorrect", "static.content", view);
            assertNotNull("Static content is null", model.asMap().get(WebConstants.STATIC_CONTENT));
        } catch (Exception e) {
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        
        
        log.info("Testing dispatch static page controller complete");
    }

    @Test
    public void testAccessDenied() {
        log.info("Testing access denied...");

        try {
            String view = indexController.accessDenied();
            assertEquals("Tile view is incorrect", "403", view);

        } catch (Exception e) {
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        log.info("Testing access denied complete");
    }
}
