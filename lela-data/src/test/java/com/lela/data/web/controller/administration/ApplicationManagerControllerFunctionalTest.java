/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.data.web.controller.administration;

import com.lela.commons.service.ApplicationService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.Application;
import com.lela.domain.enums.ApplicationType;
import com.lela.data.web.AbstractFunctionalTest;
import com.lela.data.web.controller.administration.application.ApplicationManagerController;
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

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Bjorn Harvold
 * Date: 7/5/12
 * Time: 2:42 PM
 * Responsibility:
 */
public class ApplicationManagerControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(ApplicationManagerControllerFunctionalTest.class);

    @Autowired
    private ApplicationManagerController applicationManagerController;

    @Autowired
    private ApplicationService applicationService;

    @Test
    public void testApplicationManagerController() {
        log.info("Testing applicationManagerController...");
        String view = null;
        Model model = null;
        BindingResult errors = null;
        RedirectAttributes redirectAttributes;
        Locale locale = Locale.US;
        Application application = null;

        log.info("First we need to secure the context because these are admin-only methods");
        SpringSecurityHelper.secureChannel();
        try {
            log.info("Listing all existing applications...");
            model = new BindingAwareModelMap();
            view = applicationManagerController.showApplications(0, model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.application.list", view);
            log.info("Listing all existing applications complete. We don't really care if this methods contains a list or not. We will be creating a application shortly.");

            log.info("Go to the application form page and get ready to create a application...");
            model = new BindingAwareModelMap();
            view = applicationManagerController.showApplicationForm(null, model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.application.form", view);
            assertNotNull("application object is null", model.asMap().get(WebConstants.APPLICATION));
            application = (Application) model.asMap().get(WebConstants.APPLICATION);
            log.info("Go to the application form page and get ready to create a application complete");

            log.info("Fill out application form object and submit form...");
            application.setTp(ApplicationType.QUIZ);
            application.setNm(ApplicationManagerControllerFunctionalTest.class.getSimpleName());
            application.setRlnm(ApplicationManagerControllerFunctionalTest.class.getSimpleName());
            application.setDsc("A description of the application");
            application.setPblshd(false);
            application.setRlnm("some-application-url-name");
            application.setTrlnm("this-is-the-quiz-url-name");

            errors = new BindException(application, WebConstants.APPLICATION);
            redirectAttributes = new RedirectAttributesModelMap();
            model = new BindingAwareModelMap();
            view = applicationManagerController.submitApplicationForm(application, errors, redirectAttributes, locale, model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "redirect:/administration/application/" + application.getRlnm(), view);
            assertTrue("Redirect attributes is empty", redirectAttributes.getFlashAttributes().containsKey(WebConstants.MESSAGE));
            log.info("Fill out application form object and submit form complete");

            log.info("Verify that the application exists");
            log.info("Show the application page...");
            model = new BindingAwareModelMap();
            view = applicationManagerController.showApplication(application.getRlnm(), model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.application", view);
            assertNotNull("application object is null", model.asMap().get(WebConstants.APPLICATION));
            application = (Application) model.asMap().get(WebConstants.APPLICATION);
            log.info("Show the application page complete");

            log.info("Delete the application object...");
            redirectAttributes = new RedirectAttributesModelMap();
            view = applicationManagerController.deleteApplication(application.getRlnm(), redirectAttributes, locale);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "redirect:/administration/application/list", view);
            assertTrue("Redirect attributes is empty", redirectAttributes.getFlashAttributes().containsKey(WebConstants.MESSAGE));
            log.info("Delete the application object complete");

            log.info("Verify that the application has been deleted");
            log.info("Show the application page...");
            model = new BindingAwareModelMap();
            view = applicationManagerController.showApplication(application.getRlnm(), model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.application", view);
            assertNull("application object is not null", model.asMap().get(WebConstants.APPLICATION));
            log.info("Show the application page complete");
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            fail(ex.getMessage());
        } finally {
            if (application != null) {
                applicationService.removeApplication(application.getRlnm());
            }
        }

        SpringSecurityHelper.unsecureChannel();
        log.info("Testing applicationManagerController...");
    }
}
