/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.web.test.controller;

import com.lela.commons.event.EventHelper;
import com.lela.commons.event.RegistrationEvent;
import com.lela.commons.event.SubscribeEvent;
import com.lela.commons.event.ViewedDepartmentEvent;
import com.lela.commons.service.DepartmentService;
import com.lela.commons.service.NavigationBarService;
import com.lela.commons.spring.mobile.MockDevice;
import com.lela.commons.spring.mobile.WurflDevice;
import com.lela.commons.utilities.UserSessionTrackingHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.CategoryGroup;
import com.lela.domain.document.NavigationBar;
import com.lela.domain.document.User;
import com.lela.domain.dto.department.DepartmentLandingPage;
import com.lela.domain.dto.department.DepartmentLandingPageData;
import com.lela.domain.dto.department.DepartmentQuery;
import com.lela.web.web.controller.DepartmentController;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mobile.device.Device;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EventHelper.class)
public class DepartmentControllerUnitTests {
    private final static Logger log = LoggerFactory.getLogger(DepartmentControllerUnitTests.class);

    private static final String CATEGORY_GROUP_URL_NAME_WITH_NO_FILTERS = "nofiltersgroup";
    private static final String CATEGORY_GROUP_URL_NAME_WITH_FILTERS = "filtersgroup";

    @Mock
    private DepartmentService departmentService;

    @Mock
    private NavigationBarService navigationBarService;

    @Mock
    private UserSessionTrackingHelper userSessionTrackingHelper;

    @InjectMocks
    private DepartmentController departmentController;

    private MockDevice mockDevice = new MockDevice(MockDevice.DEVICE_TYPE.NORMAL);
    private MockDevice mockMobileDevice = new MockDevice(MockDevice.DEVICE_TYPE.MOBILE);
    private Device device = new WurflDevice(mockDevice);
    private Device mobileDevice = new WurflDevice(mockMobileDevice);
    private Model model;
    private User user;
    private MockHttpServletRequest request = new MockHttpServletRequest();
    private MockHttpServletResponse response = new MockHttpServletResponse();

    @Before
	public void beforeEach(){
        departmentController.setUserSessionTrackingHelper(userSessionTrackingHelper);
        departmentController.setMobileEnabledInEnvironment(true);
        departmentController.setMobileResolutionRequirement(480);
        model = new BindingAwareModelMap();
        user = new User();
	}

	@Test
	public void testShowSEOFriendlyDepartmentLandingPageWithoutFunctionalFilters() throws Exception{
        log.info("Testing showSEOFriendlyDepartmentLandingPage()...");

        log.info("Testing with no mobile device...");

        MockHttpSession session = new MockHttpSession();
        // configure mocks
		when(departmentService.findFunctionalFilterCountByCategoryGroupUrlName(CATEGORY_GROUP_URL_NAME_WITH_NO_FILTERS)).thenReturn(0L);
        when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(user);

        ArgumentCaptor<Object> trackEvent = (ArgumentCaptor)ArgumentCaptor.forClass(Object.class);
        mockStatic(EventHelper.class);

        // execute controller method
		String view = departmentController.showSEOFriendlyDepartmentLandingPage(CATEGORY_GROUP_URL_NAME_WITH_NO_FILTERS, CATEGORY_GROUP_URL_NAME_WITH_NO_FILTERS, device, request, response, session, model);

        verifyStatic(times(1));
        EventHelper.post(trackEvent.capture());

        Assert.assertTrue("ViewedDepartmentEvent not posted", trackEvent.getAllValues().get(0) instanceof ViewedDepartmentEvent);

        // verify
		assertTrue("Model missing object", model.containsAttribute(WebConstants.CURRENT_DEPARTMENT));
		assertEquals("Incorrect view", "category.departments", view);
//        verify(departmentService, times(1)).findFunctionalFilterCountByCategoryGroupUrlName(CATEGORY_GROUP_URL_NAME_WITH_NO_FILTERS);
        log.info("Testing with no mobile device complete");

        trackEvent = (ArgumentCaptor)ArgumentCaptor.forClass(Object.class);
        mockStatic(EventHelper.class);

        log.info("Testing with mobile device...");

        // execute controller method
        view = departmentController.showSEOFriendlyDepartmentLandingPage(CATEGORY_GROUP_URL_NAME_WITH_NO_FILTERS, CATEGORY_GROUP_URL_NAME_WITH_NO_FILTERS, mobileDevice, request, response, session, model);

        verifyStatic(times(1));
        EventHelper.post(trackEvent.capture());

        Assert.assertTrue("ViewedDepartmentEvent not posted", trackEvent.getAllValues().get(0) instanceof ViewedDepartmentEvent);

        // verify
        assertTrue("Model missing object", model.containsAttribute(WebConstants.CURRENT_DEPARTMENT));
        assertEquals("Incorrect view", "category.departments.mobile", view);
        log.info("Testing with mobile device complete");

        log.info("Testing showSEOFriendlyDepartmentLandingPage() complete");
	}

	@Test
	public void testShowSEOFriendlyDepartmentLandingPageWithFunctionalFilters() throws Exception{
        log.info("Testing showSEOFriendlyDepartmentLandingPage()...");

        log.info("Testing with no mobile device...");

        // set up object
        MockHttpSession session = new MockHttpSession();
        DepartmentLandingPage dlp = new DepartmentLandingPage();

        // configure mocks
		when(departmentService.findFunctionalFilterCountByCategoryGroupUrlName(CATEGORY_GROUP_URL_NAME_WITH_FILTERS)).thenReturn(1L);
		when(departmentService.findDepartmentLandingPage(CATEGORY_GROUP_URL_NAME_WITH_FILTERS, null)).thenReturn(dlp);
        when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(user);

        ArgumentCaptor trackEvent = (ArgumentCaptor)ArgumentCaptor.forClass(Object.class);
        mockStatic(EventHelper.class);

        // execute controller method
		String view = departmentController.showSEOFriendlyDepartmentLandingPage(CATEGORY_GROUP_URL_NAME_WITH_FILTERS, CATEGORY_GROUP_URL_NAME_WITH_FILTERS, device, request, response, session, model);

        verifyStatic(times(1));
        EventHelper.post(trackEvent.capture());

        Assert.assertTrue("ViewedDepartmentEvent not posted", trackEvent.getAllValues().get(0) instanceof ViewedDepartmentEvent);

        // verify
		assertTrue("Model missing object", model.containsAttribute(WebConstants.CURRENT_DEPARTMENT));
        // TODO comment back in later
//		assertTrue("Model missing object", model.containsAttribute(WebConstants.DEPARTMENT_LANDING_PAGE));
//		assertEquals("Incorrect view", "category.departments.filters", view);
//        verify(departmentService, times(1)).findFunctionalFilterCountByCategoryGroupUrlName(CATEGORY_GROUP_URL_NAME_WITH_FILTERS);
//        verify(departmentService, times(1)).findDepartmentLandingPage(CATEGORY_GROUP_URL_NAME_WITH_FILTERS);
		assertEquals("Incorrect view", "category.departments", view);
        log.info("Testing with no mobile device complete");

        log.info("Testing with mobile device...");

        trackEvent = (ArgumentCaptor)ArgumentCaptor.forClass(Object.class);
        mockStatic(EventHelper.class);

        // execute controller method
        view = departmentController.showSEOFriendlyDepartmentLandingPage(CATEGORY_GROUP_URL_NAME_WITH_FILTERS, CATEGORY_GROUP_URL_NAME_WITH_FILTERS, mobileDevice, request, response, session, model);

        verifyStatic(times(1));
        EventHelper.post(trackEvent.capture());

        Assert.assertTrue("ViewedDepartmentEvent not posted", trackEvent.getAllValues().get(0) instanceof ViewedDepartmentEvent);

        // verify
        assertTrue("Model missing object", model.containsAttribute(WebConstants.CURRENT_DEPARTMENT));

        // TODO Comment back in
//        assertTrue("Model missing object", model.containsAttribute(WebConstants.DEPARTMENT_LANDING_PAGE));
//        assertEquals("Incorrect view", "category.departments.filters.mobile", view);
        log.info("Testing with mobile device complete");

        log.info("Testing showSEOFriendlyDepartmentLandingPage() complete");
	}

    @Test
    public void testShowSEOFriendlyDepartmentLandingPageData() throws Exception {
        log.info("Testing showSEOFriendlyDepartmentLandingPageData()...");

        // set up objects
        MockHttpSession session = new MockHttpSession();
        DepartmentQuery query = new DepartmentQuery();
        DepartmentLandingPageData dlpd = new DepartmentLandingPageData(query);

        // configure mocks
        when(departmentService.findDepartmentLandingPageData(query)).thenReturn(dlpd);

        ArgumentCaptor trackEvent = (ArgumentCaptor)ArgumentCaptor.forClass(Object.class);
        mockStatic(EventHelper.class);

        // execute controller method
        String view = departmentController.showSEOFriendlyDepartmentLandingPageData(CATEGORY_GROUP_URL_NAME_WITH_FILTERS,
                CATEGORY_GROUP_URL_NAME_WITH_FILTERS, query, session, model, device);

        // TODO this doesn't work
//        verifyStatic(times(1));
//        EventHelper.post(trackEvent.capture());

//        Assert.assertTrue("ViewedDepartmentEvent not posted", trackEvent.getAllValues().get(0) instanceof ViewedDepartmentEvent);

        // verify
        assertNotNull("View is null", view);
        assertEquals("View is incorrect", "category.departments.filters.data", view);
        assertTrue("Result is incorrect", model.containsAttribute(WebConstants.DEPARTMENT_LANDING_PAGE_DATA));
        verify(departmentService, times(1)).findDepartmentLandingPageData(query);

        log.info("Testing showSEOFriendlyDepartmentLandingPageData() complete");
    }

	@Test
	public void testShowDepartment() throws Exception{
        log.info("Testing showDepartment()...");

        // set up object
        NavigationBar nb = new NavigationBar();
        nb.setRlnm("nb");
        CategoryGroup cg = new CategoryGroup(nb.getRlnm());
        cg.setRlnm(CATEGORY_GROUP_URL_NAME_WITH_FILTERS);
        cg.setSrlnm(CATEGORY_GROUP_URL_NAME_WITH_FILTERS);
        nb.addCategoryGroup(cg);

        // configure mocks
		when(navigationBarService.findDefaultNavigationBar(Locale.US)).thenReturn(nb);

        // execute controller method
		ModelAndView view = departmentController.showDepartment(CATEGORY_GROUP_URL_NAME_WITH_FILTERS, Locale.US);

        // verify
        RedirectView rView = (RedirectView) view.getView();
		assertNotNull("View is null", view);
		assertEquals("Incorrect view", "/" + CATEGORY_GROUP_URL_NAME_WITH_FILTERS + "/d?rlnm=" + CATEGORY_GROUP_URL_NAME_WITH_FILTERS, rView.getUrl());
        verify(navigationBarService, times(1)).findDefaultNavigationBar(Locale.US);

        log.info("Testing showDepartment() complete");
	}

}
