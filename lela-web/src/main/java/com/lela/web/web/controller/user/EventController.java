/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.web.controller.user;

import com.lela.commons.exception.ValidationException;
import com.lela.commons.service.AffiliateService;
import com.lela.commons.service.EventService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.Event;
import com.lela.domain.document.User;
import com.lela.web.web.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lela.commons.exception.ValidationException;
import com.lela.commons.service.EventService;
import com.lela.commons.service.UserService;
import com.lela.commons.service.UserTrackerService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.Event;
import com.lela.domain.document.User;
import com.lela.web.web.controller.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;

@Controller("eventController")
public class EventController extends AbstractController {

    private final EventService eventService;
    private final AffiliateService affiliateService;
    

    
    @Autowired
    public EventController(EventService eventService, AffiliateService affiliateService) {
        this.eventService = eventService;
        this.affiliateService = affiliateService;
    }

    /**
     * Get user data
     *
     * @param urlName urlName
     * @param model   model
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/event/{urlName}", method = RequestMethod.GET)
    public String showEvent(@PathVariable("urlName") String urlName, Model model) throws Exception {
        String view;

        Event event = eventService.findEventByUrlName(urlName);

        if (event != null) {
            Date now = new Date();
            if (event.getNddt().after(now) && event.getStrtdt().before(now)) {
                if (StringUtils.hasText(event.getVwnm())) {
                    view = event.getVwnm();
                } else {
                    view = "user.event";
                }
            } else {
                // we don't want to display the event page if the event has expired
                if (StringUtils.hasText(event.getXprdvwnm())) {
                    view = event.getXprdvwnm();
                } else {
                    view = "user.event.expired";
                }
            }
            
            model.addAttribute(WebConstants.EVENT, event);

        } else {
            // we don't want to display the event if the event doesn't exist
            view = "resourceNotFound";
        }

        return view;
    }

    @RequestMapping(value = "/event/{urlName}/agree", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String eventConfirmation(@PathVariable("urlName") String urlName,
                                    @RequestBody Map<String, Object> attrs,
                                    HttpServletRequest request,
                                    HttpServletResponse response,
                                    HttpSession session) throws Exception {
        User user = retrieveUserFromPrincipalOrSession(session);
        String result = WebConstants.FAILURE;

        try {
            Event event = eventService.findEventByUrlName(urlName);
            if (event != null) {
                eventService.registerUserWithEvent(user.getCd(), urlName, attrs);

                // Track the Affiliate and Campaign to the UserTracker
                if (event.getFfltccntrlnm() != null || event.getCmpgnrlnm() != null) {
                    affiliateService.trackAffiliateIdentifiers((String) session.getAttribute(WebConstants.SESSION_USER_CODE), event.getFfltccntrlnm(), event.getCmpgnrlnm(), null);
                }

                result = WebConstants.SUCCESS;
            }
        } catch (IllegalAccessException ex) {
            // something unexpected happened here
            result = WebConstants.FAILURE;
        } catch (ValidationException ex) {
            // validation did not pass, this should be enforced on the client
            result = WebConstants.FAILURE;
        }

        return result;
    }

}
