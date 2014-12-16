/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.data.web.controller.administration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Locale;

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

import com.lela.commons.service.NavigationBarService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.data.web.AbstractFunctionalTest;
import com.lela.data.web.controller.administration.navigationbar.NavigationBarManagerController;
import com.lela.domain.document.CategoryGroup;
import com.lela.domain.document.NavigationBar;
import com.lela.domain.enums.PublishStatus;

/**
 * Created by Bjorn Harvold
 * Date: 7/4/12
 * Time: 7:08 PM
 * Responsibility:
 */
public class NavigationBarManagerControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(NavigationBarManagerControllerFunctionalTest.class);

    @Autowired
    private NavigationBarManagerController navigationBarManagerController;

    @Autowired
    private NavigationBarService navigationBarService;

    @Test
    public void testNavigationBarManagerController() {
        NavigationBar navigationBar = null;
        CategoryGroup categoryGroup = null;
        String view = null;
        Model model = null;
        BindingResult errors = null;
        RedirectAttributes redirectAttributes;
        Locale locale = Locale.US;

        log.info("Testing navigation bar functionality...");

        log.info("First we need to secure the context because these are admin-only methods");
        SpringSecurityHelper.secureChannel();

        try {
            log.info("Listing all existing navbars...");
            model = new BindingAwareModelMap();
            view = navigationBarManagerController.showNavigationBars(0, model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.navigationbar.list", view);
            log.info("Listing all existing navbars complete. We don't really care if this methods contains a list or not. We will be creating a navbar shortly.");

            log.info("Go to the navbar form page and get ready to create a navbar...");
            model = new BindingAwareModelMap();
            view = navigationBarManagerController.showNavigationBarForm(null, model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.navigationbar.form", view);
            assertNotNull("navbar object is null", model.asMap().get(WebConstants.NAVIGATION_BAR));
            navigationBar = (NavigationBar) model.asMap().get(WebConstants.NAVIGATION_BAR);
            log.info("Go to the navbar form page and get ready to create a navbar complete");

            log.info("Fill out navbar form object and submit form...");
            navigationBar.setDflt(false);
            navigationBar.setLcl(locale);
            navigationBar.setRlnm(NavigationBarManagerControllerFunctionalTest.class.getSimpleName());

            errors = new BindException(navigationBar, WebConstants.NAVIGATION_BAR);
            redirectAttributes = new RedirectAttributesModelMap();
            model = new BindingAwareModelMap();
            view = navigationBarManagerController.submitNavigationBarForm(navigationBar, errors, redirectAttributes, locale, model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "redirect:/administration/navigationbar/" + navigationBar.getRlnm(), view);
            assertTrue("Redirect attributes is empty", redirectAttributes.getFlashAttributes().containsKey(WebConstants.MESSAGE));
            log.info("Fill out navbar form object and submit form complete");

            log.info("Show the navbar page...");
            model = new BindingAwareModelMap();
            view = navigationBarManagerController.showNavigationBar(navigationBar.getRlnm(), model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.navigationbar", view);
            assertNotNull("navbar object is null", model.asMap().get(WebConstants.NAVIGATION_BAR));
            navigationBar = (NavigationBar) model.asMap().get(WebConstants.NAVIGATION_BAR);
            log.info("Show the navbar page complete");

            log.info("Now that we have a persisted navbar, time to add a category group to it...");
            log.info("Show category group form page");
            model = new BindingAwareModelMap();
            view = navigationBarManagerController.showCategoryGroupForm(navigationBar.getRlnm(), null, model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.categorygroup.form", view);
            assertNotNull("category group object is null", model.asMap().get(WebConstants.CATEGORY_GROUP));
            categoryGroup = (CategoryGroup) model.asMap().get(WebConstants.CATEGORY_GROUP);
            log.info("Category group form loaded successfully");

            log.info("Fill out the quiz step and submit");
            categoryGroup.setNm("A Category Group");
            categoryGroup.setRlnm("a-category-group");
            categoryGroup.setRdr(1);
            categoryGroup.setStts(PublishStatus.PUBLISHED);
            categoryGroup.setNavigationBarUrlName(navigationBar.getRlnm());

            errors = new BindException(navigationBar, WebConstants.CATEGORY_GROUP);
            redirectAttributes = new RedirectAttributesModelMap();
            model = new BindingAwareModelMap();
            view = navigationBarManagerController.submitCategoryGroupForm(navigationBar.getRlnm(), categoryGroup, errors, redirectAttributes, locale, model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "redirect:/administration/navigationbar/" + navigationBar.getRlnm(), view);
            assertTrue("Redirect attributes is empty", redirectAttributes.getFlashAttributes().containsKey(WebConstants.MESSAGE));
            log.info("Added category group successfully to navbar");

            log.info("Verify that the navbar now contains a category group");
            log.info("Show the navbar page...");
            model = new BindingAwareModelMap();
            view = navigationBarManagerController.showNavigationBar(navigationBar.getRlnm(), model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.navigationbar", view);
            assertNotNull("quiz object is null", model.asMap().get(WebConstants.NAVIGATION_BAR));
            navigationBar = (NavigationBar) model.asMap().get(WebConstants.NAVIGATION_BAR);
            assertEquals("Navbar object is missing category group", 1, navigationBar.getGrps().size(), 0);
            log.info("Show the navbar page complete");

            log.info("Let's remove the entries in reverse order starting with the category group entry");
            redirectAttributes = new RedirectAttributesModelMap();
            view = navigationBarManagerController.deleteCategoryGroup(navigationBar.getRlnm(), categoryGroup.getD(), redirectAttributes, locale);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "redirect:/administration/navigationbar/" + navigationBar.getRlnm(), view);
            assertTrue("Redirect attributes is empty", redirectAttributes.getFlashAttributes().containsKey(WebConstants.MESSAGE));

            log.info("Verify that the navbar now no longer contains a category group");
            log.info("Show the navbar page...");
            model = new BindingAwareModelMap();
            view = navigationBarManagerController.showNavigationBar(navigationBar.getRlnm(), model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.navigationbar", view);
            assertNotNull("quiz object is null", model.asMap().get(WebConstants.NAVIGATION_BAR));
            navigationBar = (NavigationBar) model.asMap().get(WebConstants.NAVIGATION_BAR);
            assertEquals("Navbar object has an extra category group", 0, navigationBar.getGrps().size(), 0);
            log.info("Show the navbar page complete");

            log.info("Delete the navbar object...");
            redirectAttributes = new RedirectAttributesModelMap();
            view = navigationBarManagerController.deleteNavigationBar(navigationBar.getRlnm(), redirectAttributes, locale);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "redirect:/administration/navigationbar/list", view);
            assertTrue("Redirect attributes is empty", redirectAttributes.getFlashAttributes().containsKey(WebConstants.MESSAGE));
            log.info("Delete the navbar object complete");

            log.info("Verify that the navbar has been removed");
            log.info("Show the navbar page...");
            model = new BindingAwareModelMap();
            view = navigationBarManagerController.showNavigationBar(navigationBar.getRlnm(), model);
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "admin.navigationbar", view);
            assertNull("navbar object is not null", model.asMap().get(WebConstants.NAVIGATION_BAR));
            log.info("Show the navbar page complete");
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            fail(ex.getMessage());
        } finally {
            if (navigationBar != null) {
                navigationBarService.removeNavigationBar(navigationBar.getRlnm());
            }
        }

        SpringSecurityHelper.unsecureChannel();
        log.info("Testing navbar functionality in complete");
    }
}
