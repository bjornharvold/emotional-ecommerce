/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */



package com.lela.commons.service.impl;

//~--- non-JDK imports --------------------------------------------------------

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.lela.commons.event.EventHelper;
import com.lela.commons.event.EventParticipantEvent;
import com.lela.commons.excel.UserDetailsReport;
import com.lela.commons.exception.ValidationException;
import com.lela.commons.service.UserService;
import com.lela.commons.service.UserTrackerService;
import com.lela.domain.ApplicationConstants;
import com.lela.commons.repository.EventRepository;
import com.lela.commons.service.CacheService;
import com.lela.commons.service.EventService;
import com.lela.domain.document.Event;
import com.lela.domain.document.EventField;
import com.lela.domain.document.QEvent;
import com.lela.domain.document.UserEvent;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.document.UserVisit;
import com.lela.domain.dto.admin.UserDetailReportRow;
import com.lela.domain.dto.report.UserUserSupplementEntry;
import com.lela.domain.enums.CacheType;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
//~--- JDK imports ------------------------------------------------------------
import java.util.concurrent.Future;

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 6/21/11
 * Time: 4:01 PM
 * Responsibility:
 */
@Service("eventService")
public class EventServiceImpl extends AbstractCacheableService implements EventService {

    /**
     * Field description
     */
    private final static Logger log = LoggerFactory.getLogger(EventServiceImpl.class);

    //~--- fields -------------------------------------------------------------

    // these are our group codes for questions

    /**
     * Field description
     */


    private final EventRepository eventRepository;
    private final UserService userService;
    private final UserTrackerService userTrackerService;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs ...
     *
     * @param cacheService cacheService
     * @param eventRepository eventRepository
     * @param userService
     * @param userTrackerService
     */
    @Autowired
    public EventServiceImpl(CacheService cacheService,
                            EventRepository eventRepository, UserService userService, UserTrackerService userTrackerService) {
        super(cacheService);
        this.eventRepository                    = eventRepository;
        this.userService = userService;
        this.userTrackerService = userTrackerService;
    }

    //~--- methods ------------------------------------------------------------



    /**
     * Method description
     *
     * @param keys keys
     * @return Return value
     */
    private String createCacheKey(List<String> keys) {
        StringBuilder sb = new StringBuilder();

        if ((keys != null) && !keys.isEmpty()) {
            for (String key : keys) {
                sb.append(key).append("/");
            }

            // remove the last slash
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }

    @Override
    public Page<Event> findEvents(Integer page, Integer size) {
        return eventRepository.findAll(new PageRequest(page, size));
    }

    @Override
    public Event findEventByUrlName(String urlName) {
        Event result = null;

        Cache.ValueWrapper wrapper = retrieveFromCache(ApplicationConstants.EVENT_CACHE, urlName);

        if (wrapper != null && wrapper.get() != null && wrapper.get() instanceof Event) {
            result = (Event) wrapper.get();
        } else {
            result = eventRepository.findOne(QEvent.event.rlnm.eq(urlName));

            if (result != null) {
                putInCache(ApplicationConstants.EVENT_CACHE, urlName, result);
            }
        }

        return result;
    }

    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST', 'RIGHT_INSERT_EVENT_AS_ADMIN')")
    @Override
    public Event saveEvent(Event event) {

        event.setVntFlds(Lists.newArrayList(Collections2.filter(event.getVntFlds(), new Predicate<EventField>() {
            @Override
            public boolean apply(@Nullable EventField eventField) {
                return !eventField.getDelete();
            }
        })));
        for(EventField eventField:event.getVntFlds())
        {
           if(eventField.getDelete())
           {
               event.getVntFlds().remove(eventField);
           }
        }
        event = eventRepository.save(event);

        // Remove from cache
        removeFromCache(CacheType.EVENT, event.getRlnm());

        return event;
    }

    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST', 'RIGHT_DELETE_EVENT_AS_ADMIN')")
    @Override
    public Event removeEvent(String rlnm) {
        Event event = eventRepository.findByUrlName(rlnm);

        if (event != null) {
            eventRepository.delete(event);

            // Remove from cache
            removeFromCache(CacheType.EVENT, event.getRlnm());
        }
        
        return event;
    }

    /**
     * Will add an event to the user that the user signed up for
     *
     * @param userCode userCode
     * @param urlName  urlName
     * @param params   data posted when the user registered for the event
     * @return User user
     */
    @Override
    public UserSupplement registerUserWithEvent(String userCode, String urlName, Map<String, Object> params) throws IllegalAccessException, ValidationException {
        UserSupplement us = userService.findUserSupplement(userCode);
        Event event = findEventByUrlName(urlName);

        if (event == null || !event.isValid(params)) {
            throw new ValidationException();
        }

        UserEvent userEvent = userService.saveUserEvent(us.getCd(), event, urlName, params);
        EventHelper.post(new EventParticipantEvent(userCode, event));

//        if (userEvent != null) {
//            sendEventRegistrationConfirmation(us, userEvent, event);
//        }

        return us;
    }

    @Async
    //@PreAuthorize("hasAnyRole('RIGHT_READ_CAMPAIGN')")
    @Override
    public Future<Workbook> generateEventUserDetailsReport(String eventUrlName) { 

        log.debug("Start generating event user report");
        DateTime start = new DateTime();

        Workbook workbook = null;

        List<UserUserSupplementEntry> entries = userService.findUsersForEventReport(eventUrlName);

        log.warn("Retrieved users for event use report in " + Seconds.secondsBetween(start, new DateTime()).getSeconds() + " sec");
        ArrayList<UserDetailReportRow> list = new ArrayList<UserDetailReportRow>();
        if (entries != null) {
            for (UserUserSupplementEntry entry : entries) {
                UserDetailReportRow row = new UserDetailReportRow(entry.getUser(), entry.getUserSupplement());
                list.add(row);

                // Check for an analytics registration event
                UserVisit visit = userTrackerService.findFirstVisit(entry.getUser());
                if (visit != null) {
                    row.setRegistrationDeviceType(visit.getDvctp());
                }

                row.setQuizComplete(userTrackerService.isQuizComplete(entry.getUser().getCd()));

                // Is a repeat visitor
                row.setRepeatUser(userTrackerService.isRepeatUser(entry.getUser()));
            }

        }

        UserDetailsReport report = new UserDetailsReport(list);
        workbook = report.generate();

        log.warn("Finish generating event user report in " + Seconds.secondsBetween(start, new DateTime()).getSeconds() + " sec");

        return new AsyncResult(workbook);
    }
}
