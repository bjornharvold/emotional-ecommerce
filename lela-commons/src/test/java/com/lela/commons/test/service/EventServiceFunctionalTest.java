/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.test.service;

import com.lela.domain.document.Event;
import com.lela.domain.enums.EventType;
import com.lela.commons.service.EventService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.test.AbstractFunctionalTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Created by Bjorn Harvold
 * Date: 6/23/11
 * Time: 11:16 AM
 * Responsibility:
 */
public class EventServiceFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(EventServiceFunctionalTest.class);
    private static final String EVENT_SERVICE_FUNCTIONAL_TEST = "EventServiceFunctionalTest";

    @Autowired
    private EventService eventService;

    @Test
    public void testEvent() {
        SpringSecurityHelper.unsecureChannel();

        log.info("Create a event and save it");

        Event event = new Event();
        event.setNm(EVENT_SERVICE_FUNCTIONAL_TEST);
        event.setRlnm(EVENT_SERVICE_FUNCTIONAL_TEST);
        event.setTp(EventType.GIVEAWAY);

        try {
            eventService.saveEvent(event);
            fail("Event should not be able to be saved here. Missing credentials.");
        } catch (Exception ex) {
            log.info("Tried to save event without credentials. An exception was expected: " + ex.getMessage());
        }

        log.info("Securing channel...");
        SpringSecurityHelper.secureChannel();
        log.info("Channel secured");

        log.info("Saving a event. This time with a secure channel");
        try {
            event = eventService.saveEvent(event);
            assertNotNull("Event is missing an id", event.getId());
            log.info("Event persisted successfully");
        } catch (Exception ex) {
            fail("Did not expect an exception here: " + ex.getMessage());
            log.info("Was not able to persist a event within secure context", ex);
        }

        log.info("Retrieving event...");
        event = eventService.findEventByUrlName(EVENT_SERVICE_FUNCTIONAL_TEST);
        assertNotNull("Event is missing", event);
        assertNotNull("Event is missing an id", event.getId());
        log.info("Event retrieved successfully");

        log.info("Deleting event...");
        eventService.removeEvent(event.getRlnm());

        event = eventService.findEventByUrlName(EVENT_SERVICE_FUNCTIONAL_TEST);
        assertNull("Event still exists", event);

        log.info("Deleted event successfully");
        log.info("Test complete!");
    }


}
