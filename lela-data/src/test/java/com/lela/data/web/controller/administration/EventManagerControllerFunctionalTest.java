/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.data.web.controller.administration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lela.commons.service.ProfileService;
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

import com.lela.commons.service.EventService;
import com.lela.commons.service.LocalCacheEvictionService;
import com.lela.commons.service.UserService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.data.web.AbstractFunctionalTest;
import com.lela.domain.document.Event;
import com.lela.domain.document.EventField;
import com.lela.domain.document.User;
import com.lela.domain.document.UserEvent;
import com.lela.domain.document.UserSupplement;

/**
 * Created by Bjorn Harvold
 * Date: 8/3/11
 * Time: 10:05 AM
 * Responsibility:
 */
@SuppressWarnings("unchecked")
public class EventManagerControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(EventManagerControllerFunctionalTest.class);
    private static final String EMAIL = "testuser5kijmkhj@yopmail.com";
    private static final String CRAZY = "crazy";

    @Autowired
    private EventController eventController;

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @Autowired
    private LocalCacheEvictionService localCacheEvictionService;

    @Autowired
    private ProfileService profileService;

    private User user = null;
    private Event sc = null;

    @Before
    public void setUp() {

        // create a temp user that we can add to the event
        user = createRandomUser(true);
        user = profileService.registerTestUser(user);
        
        assertNotNull("User hasn't been persisted", user.getId());
    }

    @After
    public void tearDown() {
        SpringSecurityHelper.secureChannel();

        if (user != null) {
            userService.removeUser(user);
        }
        
        sc = eventService.findEventByUrlName(CRAZY);
        
        if (sc != null) {
            eventService.removeEvent(sc.getRlnm());
        }

        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testEvent() {
        SpringSecurityHelper.secureChannel();
        log.info("Testing event controller...");
        Model model = new BindingAwareModelMap();

        try {
            log.info("First we want to save a piece of event");
            sc = new Event();
            Calendar startDate = Calendar.getInstance();
            startDate.add(Calendar.MONTH, -1);
            sc.setStrtdt(startDate.getTime());

            Calendar endDate = Calendar.getInstance();
            endDate.add(Calendar.MONTH, 1);
            sc.setNddt(endDate.getTime());

            sc.setNm("Some crazy give-away");
            sc.setRlnm(CRAZY);

            BindingResult errors = new BindException(sc, "event");
            String view = eventController.saveEvent(sc, errors, model);
            assertEquals("Tile view is incorrect", "redirect:/administration/event?urlName=" + sc.getRlnm(), view);

            log.info("Then we want to retrieve a paginated list of event items");
            view = eventController.showEvents(0, 10, model);
            assertEquals("Tile view is incorrect", "event.list", view);
            assertNotNull("Static content list is null", model.asMap().get(WebConstants.EVENTS));
            Page<Event> page = (Page<Event>) model.asMap().get(WebConstants.EVENTS);

            log.info("Then we want to retrieve the original event we created");
            model = new BindingAwareModelMap();
            view = eventController.showEvent(sc.getRlnm(), model);
            assertEquals("Tile view is incorrect", "event.form", view);
            assertNotNull("Static content is null", model.asMap().get(WebConstants.EVENT));

            log.info("Then we want to update some attributes on this event");
            sc = (Event) model.asMap().get(WebConstants.EVENT);
            sc.setNm("blah2");

            view = eventController.saveEvent(sc, errors, model);
            assertEquals("Tile view is incorrect", "redirect:/administration/event?urlName=" + sc.getRlnm(), view);

            log.info("Then we want to retrieve it again and check that the attributes have indeed been updated");
            model = new BindingAwareModelMap();
            view = eventController.showEvent(sc.getRlnm(), model);
            assertEquals("Tile view is incorrect", "event.form", view);
            assertNotNull("Static content is null", model.asMap().get(WebConstants.EVENT));

            sc = (Event) model.asMap().get(WebConstants.EVENT);
            assertEquals("Name is incorrect", "blah2", sc.getNm());

            List<EventField> eventFields = new ArrayList<EventField>();
            EventField eventField = new EventField();
            eventField.setFldNm("NAME");
            eventField.setRqrd(true);
            eventFields.add(eventField);
            sc.setVntFlds(eventFields);
            log.info("Then we want to add a user to the event");
            Map params = new HashMap();
            params.put("NAME", "VALUE");
            UserSupplement us = eventService.registerUserWithEvent(user.getCd(), sc.getRlnm(), params);
            assertNotNull("User is null", us);
            List<UserEvent> events = userService.findUserEvents(us.getCd());
            assertNotNull("User events is null", events);
            assertEquals("User has params with one key", 1, events.get(0).getAttrs().size());
            assertEquals("User event size is incorrect", 1, events.size(), 0);
            assertEquals("User event name is incorrect", sc.getRlnm(), events.get(0).getRlnm());

            log.info("We also want to make sure the user shows up in the list of users");
            model = new BindingAwareModelMap();
            eventController.showEvent(sc.getRlnm(), model);
            assertNotNull("User list is null", model.asMap().get(WebConstants.USERS));
            List<UserSupplement> users = (List<UserSupplement>) model.asMap().get(WebConstants.USERS);
            assertEquals("User list size is incorrect", 1, users.size());
            assertEquals("User name is incorrect", us.getFnm(), users.get(0).getFnm());

            log.info("Then we want to delete the event completely");
            view = eventController.deleteEvent(sc.getRlnm());
            assertEquals("Tile view is incorrect", "redirect:/administration/event/list", view);

            localCacheEvictionService.evictEvent(sc.getRlnm());

            log.info("Then we want to verify that the event has been removed and instead replaced by a new instance of the object but with the url name specified");
            model = new BindingAwareModelMap();
            view = eventController.showEvent(sc.getRlnm(), model);
            assertEquals("Tile view is incorrect", "event.form", view);
            assertNotNull("Static content is null", model.asMap().get(WebConstants.EVENT));

            sc = (Event) model.asMap().get(WebConstants.EVENT);
            assertEquals("Property is not null", "crazy", sc.getRlnm());

            log.info("Finally, we want to verify that the event has been removed and instead replaced by a new instance of the object");
            model = new BindingAwareModelMap();
            view = eventController.showEvent(null, model);
            assertEquals("Tile view is incorrect", "event.form", view);
            assertNotNull("Static content is null", model.asMap().get(WebConstants.EVENT));

            sc = (Event) model.asMap().get(WebConstants.EVENT);
            assertNull("Property is not null", sc.getRlnm());

        } catch (Exception e) {
            log.info(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        } finally {
            SpringSecurityHelper.unsecureChannel();
        }

        log.info("Testing event controller complete");
    }

}
