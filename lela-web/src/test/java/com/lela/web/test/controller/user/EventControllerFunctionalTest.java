/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.test.controller.user;

import com.lela.commons.service.EventService;
import com.lela.commons.service.ProfileService;
import com.lela.domain.document.Event;
import com.lela.domain.document.User;
import com.lela.domain.document.UserEvent;
import com.lela.domain.dto.Principal;
import com.lela.commons.service.UserService;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.web.test.AbstractFunctionalTest;
import com.lela.web.web.controller.user.EventController;
import com.lela.commons.web.utils.WebConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class EventControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(EventControllerFunctionalTest.class);

    private static final String VALIDEVENT = "validevent";
    private static final String INVALIDEVENT = "invalidevent";
    private static final String INVALIDEVENT2 = "invalidevent2";
    private static final String NA = "na";

    private User user;
    private Date beforeNow;
    private Date afterNow;
    private Event validEvent;
    private Event invalidEvent;
    private Event invalidEvent2;

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;
    
    @Autowired
    private EventController eventController;

    @Autowired
    private ProfileService profileService;

    @Before
    public void setup() {
        SpringSecurityHelper.secureChannel();
        
        user = createRandomUser(true);
        user = profileService.registerTestUser(user);

        assertNotNull("User is missing", user);
        assertNotNull("User is missing an id", user.getId());

        Calendar beforeNowCal = Calendar.getInstance();
        Calendar afterNowCal = Calendar.getInstance();

        beforeNowCal.set(Calendar.MONTH, 0);
        beforeNowCal.set(Calendar.DAY_OF_MONTH, 1);
        beforeNowCal.set(Calendar.HOUR, 1);
        beforeNowCal.set(Calendar.MINUTE, 0);
        beforeNowCal.set(Calendar.MILLISECOND, 0);
        beforeNowCal.set(Calendar.YEAR, 1900);
        
        beforeNow = beforeNowCal.getTime();

        afterNowCal.set(Calendar.MONTH, 11);
        afterNowCal.set(Calendar.DAY_OF_MONTH, 1);
        afterNowCal.set(Calendar.HOUR, 1);
        afterNowCal.set(Calendar.MINUTE, 0);
        afterNowCal.set(Calendar.MILLISECOND, 0);
        afterNowCal.set(Calendar.YEAR, 2050);
        
        afterNow = afterNowCal.getTime();
        
        SpringSecurityHelper.unsecureChannel();
    }

    @After
    public void teardown() {
        SpringSecurityHelper.secureChannel();

        if (user != null) {
            userService.removeUser(user);
        }
        
        validEvent = eventService.findEventByUrlName(VALIDEVENT);
        invalidEvent = eventService.findEventByUrlName(INVALIDEVENT);
        invalidEvent2 = eventService.findEventByUrlName(INVALIDEVENT2);

        eventService.removeEvent(validEvent.getRlnm());
        eventService.removeEvent(invalidEvent.getRlnm());
        eventService.removeEvent(invalidEvent2.getRlnm());

        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testEventWorkflow() {
        
        String view = null;

        log.info("First we need to create a few events");
        
        validEvent = new Event();
        validEvent.setNm(VALIDEVENT);
        validEvent.setRlnm(VALIDEVENT);
        validEvent.setStrtdt(beforeNow);
        validEvent.setNddt(afterNow);
        
        invalidEvent = new Event();
        invalidEvent.setNm(INVALIDEVENT);
        invalidEvent.setRlnm(INVALIDEVENT);
        invalidEvent.setStrtdt(afterNow);
        invalidEvent.setNddt(afterNow);

        invalidEvent2 = new Event();
        invalidEvent2.setNm(INVALIDEVENT2);
        invalidEvent2.setRlnm(INVALIDEVENT2);
        invalidEvent2.setStrtdt(beforeNow);
        invalidEvent2.setNddt(beforeNow);

        SpringSecurityHelper.secureChannel();
        validEvent = eventService.saveEvent(validEvent);
        invalidEvent = eventService.saveEvent(invalidEvent);
        invalidEvent2 = eventService.saveEvent(invalidEvent2);
        SpringSecurityHelper.unsecureChannel();

        SpringSecurityHelper.secureChannel(new Principal(user));

        try {
            log.info("First we try to retrieve a non-existent event");
            Model model = new BindingAwareModelMap();
            view = eventController.showEvent(NA, model);
            assertEquals("View is incorrect", "resourceNotFound", view);
            
            log.info("Then we try to retrieve some existing but invalid events");
            model = new BindingAwareModelMap();
            view = eventController.showEvent(INVALIDEVENT, model);
            assertEquals("View is incorrect", "user.event.expired", view);
            
            model = new BindingAwareModelMap();
            view = eventController.showEvent(INVALIDEVENT2, model);
            assertEquals("View is incorrect", "user.event.expired", view);
            
            log.info("Finally, we retrieve a valid event");
            model = new BindingAwareModelMap();
            view = eventController.showEvent(VALIDEVENT, model);
            assertEquals("View is incorrect", "user.event", view);
            assertNotNull("Event is null", model.asMap().get(WebConstants.EVENT));
            
            log.info("Then we try to sign up a user to some invalid events");
            //event attributes
            Map<String, Object> attributes = new HashMap<String, Object>();
            HttpSession session = new MockHttpSession();
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setSession(session);
            MockHttpServletResponse response = new MockHttpServletResponse();

            String result = eventController.eventConfirmation(NA, attributes, request, response, session);
            assertEquals("Result is incorrect", WebConstants.FAILURE, result);
            session = new MockHttpSession();
            result = eventController.eventConfirmation(INVALIDEVENT, attributes, request, response, session);
            assertEquals("Result is incorrect", WebConstants.FAILURE, result);
            session = new MockHttpSession();
            result = eventController.eventConfirmation(INVALIDEVENT2, attributes, request, response, session);
            assertEquals("Result is incorrect", WebConstants.FAILURE, result);
            
            log.info("Then we submit a valid event");
            session = new MockHttpSession();
            result = eventController.eventConfirmation(VALIDEVENT, attributes, request, response, session);
            assertEquals("Result is incorrect", WebConstants.SUCCESS, result);
            
            log.info("Now we verify that the user has a new user event");
            List<UserEvent> events = userService.findUserEvents(user.getCd());
            assertNotNull("User events are null", events);
            assertEquals("Event size is incorrect", 1, events.size());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Finished testing event controller");
    }

}
