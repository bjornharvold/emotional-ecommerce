/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */


package com.lela.data.web.controller.administration;
/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Future;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.lela.commons.service.AffiliateService;
import com.lela.commons.service.CampaignService;
import com.lela.commons.service.EventService;
import com.lela.commons.service.UserService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.data.web.validator.EventValidator;
import com.lela.domain.ApplicationConstants;
import com.lela.domain.document.Event;

/**
 * <p>Title: UserController</p>
 * <p>Description: User homepage.</p>
 *
 * @author Bjorn Harvold
 */
@Controller("adminEventController")
@SessionAttributes(types = Event.class)
public class EventController {

	private final static Logger log = LoggerFactory.getLogger(EventController.class);
	
    private final AffiliateService affiliateService;
    private final CampaignService campaignService;
    private final EventService eventService;
    private final UserService userService;
    private final EventValidator eventValidator;
    private final MessageSource messageSource;
    
    @Value ("${numberOfSecondsToWaitForReportToGenerate:5}")
    private int numberOfSecondsToWaitForReportToGenerate;

    @Value("${preview.url:http://www.lela.com}")
    private String previewUrl;
    
    private String EVENT_USER_REPORT = "event-user-report.xlsx";
    
    @Autowired
    public EventController(AffiliateService affiliateService, CampaignService campaignService, EventService eventService,
                           UserService userService,
                           EventValidator eventValidator,
                           MessageSource messageSource
    		) {
        this.affiliateService = affiliateService;
        this.campaignService = campaignService;
        this.eventService = eventService;
        this.userService = userService;
        this.eventValidator = eventValidator;
        this.messageSource = messageSource;
    }

    //~--- methods ------------------------------------------------------------

    @RequestMapping(value = "/administration/event/list", method = RequestMethod.GET)
    public String showEvents(@RequestParam(required = false, defaultValue = "0") Integer page,
                             @RequestParam(required = false, defaultValue = "10") Integer size,
                             Model model) throws Exception {
        model.addAttribute(WebConstants.EVENTS, eventService.findEvents(page, size));
        model.addAttribute(WebConstants.PREVIEW_URL, previewUrl);
        
        return "event.list";
    }

    @RequestMapping(value = "/administration/event", method = RequestMethod.GET)
    public String showEvent(@RequestParam(value = "urlName", required = false) String urlName,
                            Model model) throws Exception {
        if (StringUtils.isNotBlank(urlName)) {
            Event sc = eventService.findEventByUrlName(urlName);

            if (sc != null) {
                model.addAttribute(WebConstants.EVENT, sc);

                // this is an existing event so we want to retrieve the users that have signed up
                model.addAttribute(WebConstants.USERS, userService.findUsersByEventUrlName(urlName));
            } else {
                sc = new Event();
                sc.setRlnm(urlName);
                model.addAttribute(WebConstants.EVENT, sc);
            }
        } else {
            model.addAttribute(WebConstants.EVENT, new Event());
        }

        populateReferenceData(model);

        return "event.form";
    }

    @RequestMapping(value = "/administration/event", method = RequestMethod.POST)
    public String saveEvent(@Valid Event event, BindingResult errors,
                            Model model) throws Exception {
        String view;

        if (errors.hasErrors()) {
            model.addAttribute(WebConstants.EVENT, event);
            view = "event.form";
        }

        eventValidator.validate(event, errors);

        if (errors.hasErrors()) {
            model.addAttribute(WebConstants.EVENT, event);
            populateReferenceData(model);
            view = "event.form";
        } else {
            event = eventService.saveEvent(event);
            view = "redirect:/administration/event?urlName=" + event.getRlnm();
        }


        return view;
    }

    @RequestMapping(value = "/administration/event", method = RequestMethod.DELETE)
    public String deleteEvent(@RequestParam(value = "urlName", required = false) String urlName) throws Exception {
        Event eventContent = eventService.findEventByUrlName(urlName);

        if (eventContent != null) {
            eventService.removeEvent(eventContent.getRlnm());
        }

        return "redirect:/administration/event/list";
    }
    
    @RequestMapping(value = "/administration/event/users/report/generate", method = RequestMethod.GET)
    public String generateEventUsersReport(@RequestParam(value = "urlName", required = true) String urlName,
    								HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

    	String reportId = ApplicationConstants.REPORT_ID_PREFIX + System.currentTimeMillis(); 
    	Future<Workbook> workbookResultFuture = eventService.generateEventUserDetailsReport(urlName); 
    	
    	//wait for report to finish within configured time or bail
    	Workbook workbook = null;
    	boolean generated = false;
    	long start = System.currentTimeMillis();
    	while (((System.currentTimeMillis() - start)/1000) < numberOfSecondsToWaitForReportToGenerate) {
    		Thread.sleep(1000L);
        	if (workbookResultFuture != null && workbookResultFuture.isDone()) {
        		workbook = workbookResultFuture.get();
        		generated = true;
        		break;
        	}
    	}
        if (generated){
            log.info("Report generated with reportId: " + reportId);
    		
    		returnInResponse(EVENT_USER_REPORT, response, workbook);
    	} else {
    		log.debug("ReportId with id: " + reportId + " will taking longer than " + numberOfSecondsToWaitForReportToGenerate + " seconds");
    		//Also place the async task handle in session keyed off the unique reportId
    		request.getSession(true).setAttribute(ApplicationConstants.REPORT_TASK_PREFIX + reportId, workbookResultFuture);
    		request.setAttribute("reportFeedback", true);
    		request.setAttribute("reportId", reportId);
    		request.setAttribute("downloadLink", "/administration/event/users/report/download");
            String message = messageSource.getMessage("long.running.report.message", new String[]{String.valueOf(numberOfSecondsToWaitForReportToGenerate)}, locale);
            request.setAttribute("reportStatus", message);
    	}
    	return "admin.report.status";
    	

    }
    @RequestMapping(value = "/administration/event/users/report/download", method = RequestMethod.GET)
    public String downloadEventUsersReport(@RequestParam(value = "reportId", required = true) String reportId,
    		HttpServletRequest request, HttpServletResponse response,  Locale locale) throws Exception {

    	//Pull the async task handle form session
		@SuppressWarnings("unchecked")
		Future<Workbook> workbookResultFuture = (Future<Workbook>)request.getSession(true).getAttribute(ApplicationConstants.REPORT_TASK_PREFIX + reportId);
		if (workbookResultFuture != null && workbookResultFuture.isDone()){
            //Cleanup
			request.getSession().removeAttribute(reportId);
			request.getSession().removeAttribute(ApplicationConstants.REPORT_TASK_PREFIX);
			
			Workbook workbook = workbookResultFuture.get();
			returnInResponse(EVENT_USER_REPORT, response, workbook);
		} else {
			log.debug("ReportId with id: " + reportId + " still generating!");
			request.setAttribute("reportFeedback", true);
			request.setAttribute("reportId", reportId);
			request.setAttribute("downloadLink", "/administration/event/users/report/download");
            String message = messageSource.getMessage("long.running.report.message.inprogress", null, locale);
            request.setAttribute("reportStatus", message);
		}
		return "admin.report.status";
    }
    

    private void populateReferenceData(Model model) {
        List<String> fields = new ArrayList<String>(3);
        fields.add("nm");
        fields.add("rlnm");
        model.addAttribute(WebConstants.AFFILIATE_ACCOUNTS, affiliateService.findAffiliateAccounts(fields));
        model.addAttribute(WebConstants.CAMPAIGNS, campaignService.findActiveCampaigns());
    }
    
	private void returnInResponse(String fileName, HttpServletResponse response,
			Workbook workbook) throws IOException {
		//Place report in response
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		response.setContentType("application/vnd.ms-excel.12");
		ServletOutputStream out = response.getOutputStream();
		workbook.write(out);
    
		response.flushBuffer();
	}
}
