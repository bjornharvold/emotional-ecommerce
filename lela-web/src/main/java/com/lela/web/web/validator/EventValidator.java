/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.web.validator;

import com.lela.commons.service.EventService;
import com.lela.domain.document.Event;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class EventValidator implements Validator {
    private final EventService eventService;

    @Autowired
    public EventValidator(EventService eventService) {
        this.eventService = eventService;
    }

    public boolean supports(Class<?> clazz) {
        return Event.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        Event event = (Event) target;

        if (!errors.hasErrors()) {
            // check for email availability
            Event tmp = eventService.findEventByUrlName(event.getRlnm());

            if (tmp != null) {
                if (StringUtils.equals(tmp.getRlnm(), event.getRlnm()) && !tmp.getId().equals(event.getId())) {
                    errors.rejectValue("rlnm", "error.duplicate.event.url.name", null, "Url name is already taken");
                }
            }
        }
    }
}