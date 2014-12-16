/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.commons.exception.ValidationException;
import com.lela.domain.document.Event;
import com.lela.domain.document.UserSupplement;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by Bjorn Harvold
 * Date: 6/21/11
 * Time: 2:04 PM
 * Responsibility:
 */
public interface EventService {

    Page<Event> findEvents(Integer page, Integer size);
    Event findEventByUrlName(String urlName);
    Event saveEvent(Event content);
    Event removeEvent(String rlnm);

    //@PreAuthorize("hasAnyRole('RIGHT_READ_CAMPAIGN')")
    Future<Workbook> generateEventUserDetailsReport(String eventUrlName);

    UserSupplement registerUserWithEvent(String userCode, String urlName, Map<String, Object> params) throws IllegalAccessException, ValidationException;
}
