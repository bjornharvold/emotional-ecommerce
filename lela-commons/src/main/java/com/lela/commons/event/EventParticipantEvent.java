/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.event;

import com.lela.domain.document.Event;
import com.lela.domain.document.User;
import com.lela.domain.enums.RegistrationType;

/**
 * User: Chris Tallent
 * Date: 10/18/12
 * Time: 9:00 PM
 */
public class EventParticipantEvent extends AbstractEvent {
    private Event event;
    private String userCode;

    public EventParticipantEvent(String userCode, Event event) {
        this.userCode = userCode;
        this.event = event;
    }

    public String getUserCode() {
        return userCode;
    }

    public Event getEvent() {
        return event;
    }
}
