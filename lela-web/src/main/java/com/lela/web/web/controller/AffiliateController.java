/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.web.web.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lela.commons.service.AffiliateService;
import com.lela.commons.service.CampaignService;
import com.lela.commons.service.StaticContentService;
import com.lela.commons.service.UserService;
import com.lela.commons.web.ResourceNotFoundException;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.Campaign;
import com.lela.domain.document.StaticContent;
import com.lela.domain.document.User;
import com.lela.domain.dto.UserAttributes;

/**
 * Created by IntelliJ IDEA.
 * User: Chris Tallent
 */

@Controller
public class AffiliateController extends AbstractController {

    private final static Logger LOG = LoggerFactory.getLogger(AffiliateController.class);

    public final static int COOKIE_AGE_SECONDS = 60 * 60 * 24 * 365;

    public final static String Z100_CAMPAIGN = "z100";
    public final static String Z100_AFFILIATE = "z100";

    public final static String HOLIDAY_2012_AFFILIATE = "sandiegoliving";
    public final static String HOLIDAY_2012_CAMPAIGN = "2012-holidays-giveaway";

    private final UserService userService;
    private final AffiliateService affiliateService;
    private final CampaignService campaignService;
    private final StaticContentService staticContentService;
    private final MessageSource messageSource;

    @Autowired
    public AffiliateController(UserService userService,
                               AffiliateService affiliateService,
                               CampaignService campaignService,
                               StaticContentService staticContentService,
                               MessageSource messageSource) {
        this.userService = userService;
        this.affiliateService = affiliateService;
        this.campaignService = campaignService;
        this.staticContentService = staticContentService;
        this.messageSource = messageSource;
    }

    @RequestMapping(value = { "/z100", "/Z100" }, method = { RequestMethod.GET, RequestMethod.POST })
    public String z100Landing(UserAttributes userAttributes,
                              HttpSession session,
                              HttpServletRequest request,
                              HttpServletResponse response,
                              Model model, Locale locale) throws Exception {
        return landingPage(userAttributes, Z100_CAMPAIGN, Z100_AFFILIATE, session, request, response, model, locale);
    }

    @RequestMapping(value = { "/2012-holidays-giveaway", "/2012-holiday-giveaway" }, method = { RequestMethod.GET, RequestMethod.POST })
    public String holiday2012(UserAttributes userAttributes,
                              HttpSession session,
                              HttpServletRequest request,
                              HttpServletResponse response,
                              Model model, Locale locale) throws Exception {
        return landingPage(userAttributes, HOLIDAY_2012_CAMPAIGN, HOLIDAY_2012_AFFILIATE, session, request, response, model, locale);
    }

    /**
     * Used when the campaign does not need to track individual bloggers referring links and paid separately
     */
//    @RequestMapping(value = "/landing/{campaignUrlName}", method = { RequestMethod.GET, RequestMethod.POST })
//    public String landingPageNoReferrer(UserAttributes userAttributes,
//                                        @PathVariable("campaignUrlName") String campaignUrlName,
//                                        HttpSession session,
//                                        HttpServletRequest request,
//                                        HttpServletResponse response,
//                                        Model model) throws Exception {
//        return landingPage(userAttributes, campaignUrlName, session, request, response, model);
//    }

    @RequestMapping(value = "/landing/{campaignUrlName}/refer/{referrerAffiliateUrlName}", method = { RequestMethod.GET, RequestMethod.POST })
    public String landingPage(UserAttributes userAttributes,
                              @PathVariable("campaignUrlName") String campaignUrlName,
                              @PathVariable("referrerAffiliateUrlName") String referrerAffiliateUrlName,
                              HttpSession session,
                              HttpServletRequest request,
                              HttpServletResponse response,
                              Model model,
                              Locale locale                  
    		) throws Exception {

    	String view = null;
        // Get the logged in user
        User user = retrieveUserFromPrincipalOrSession(session);
        //User user = null;
        // Process user attributes
        userService.processUserAttributes(user.getCd(), userAttributes);

        // Look up the campaign
        Campaign campaign = campaignService.findCampaignByUrlName(campaignUrlName);
        if (campaign == null){
            LOG.warn(String.format("No valid, active campaign found for campaign url: %s ", campaignUrlName));
            String message = messageSource.getMessage("campaign.not.found", new String[]{campaignUrlName}, locale);
            model.addAttribute(WebConstants.MESSAGE, message);
        	view =  "campaign.not.found.view";
        } else if (!campaign.isActiveAndCurrent()){
            LOG.warn(String.format("Attempted to access inactive or expired campaign %s ", campaignUrlName));
            String message = messageSource.getMessage("campaign.not.active.or.current", new String[]{campaignUrlName}, locale);
            model.addAttribute(WebConstants.MESSAGE, message);
        	view =  "campaign.not.found.view";
        } else {
        	// The campaign valid, active and within the start and end dates

            // Determine if the affiliate is valid for the campaign
            AffiliateAccount affiliate = affiliateService.findActiveAffiliateAccountByUrlName(referrerAffiliateUrlName);
            if (affiliate != null) {
                // Track the Affiliate and Campaign to the UserTracker
                affiliateService.trackAffiliateIdentifiers((String) session.getAttribute(WebConstants.SESSION_USER_CODE), campaign.getFfltrlnm(), campaignUrlName, referrerAffiliateUrlName);

                //if static content is related to the campaign, get it.
                if(!StringUtils.isEmpty(campaign.getSttccntnt())){
                    Map<String, Object> velocityMap = buildVelocityMap(campaign, affiliate, request);
                	StaticContent sc = staticContentService.findStaticContentByUrlName(campaign.getSttccntnt(), velocityMap);               	
                	model.addAttribute(WebConstants.STATIC_CONTENT, sc);
                	view =  "static.content";
                	
                } else if (StringUtils.isNotBlank(campaign.getRdrctrl())) {
                	// If valid, redirect to campaign landing page
                    view =  "redirect:" + campaign.getRdrctrl();
                } else if (StringUtils.isNotBlank(campaign.getVwnm())) {

                    model.addAttribute(WebConstants.AFFILIATE, affiliate);
                    model.addAttribute(WebConstants.CAMPAIGN, campaign);

                    view =  campaign.getVwnm();
                } else {
                	String s = String.format("Campaign %s not configured with one of: static content, a redirect view or a view", campaignUrlName);
                	LOG.error(s);
                    throw new ResourceNotFoundException(s);
                }

            } else {
            	String s = String.format("The affilate %s is not valid ", referrerAffiliateUrlName);
                LOG.warn(s);
                String message = messageSource.getMessage("affiliate.not.found", new String[]{referrerAffiliateUrlName}, locale);
                model.addAttribute(WebConstants.MESSAGE, message);
                view =  "campaign.not.found.view";

            }

        }
        return view;
    }
    
    public Map <String, Object> buildVelocityMap(Campaign campaign, AffiliateAccount affiliate, HttpServletRequest request){
        Map<String, Object> velocityMap = new HashMap<String, Object>();
        velocityMap.put(WebConstants.CAMPAIGN, campaign);
        velocityMap.put(WebConstants.AFFILIATE, affiliate);
        
        //Loop thru the request and session and place values map keyed off "parameter"
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        if (request.getAttributeNames() != null){
        	 LOG.debug("Getting all request attributes...");
        	 @SuppressWarnings("rawtypes")
			 Enumeration en = (Enumeration)request.getAttributeNames();
        	 while (en.hasMoreElements()){
        		String n = (String)en.nextElement();
        		parameterMap.put(n, request.getAttribute(n));
        	 }
        }    
        
        if (request.getParameterNames() != null){
        	 LOG.debug("Getting all request parameters...");
        	 @SuppressWarnings("rawtypes")
			 Enumeration en = (Enumeration)request.getParameterNames();
        	 while (en.hasMoreElements()){
        		String n = (String)en.nextElement();
        		parameterMap.put(n, request.getParameter(n));
        	 }
        }
        
        HttpSession session = request.getSession();
        if (session.getAttributeNames() != null){
        	 LOG.debug("Getting all session attributes...");
        	 @SuppressWarnings("rawtypes")
			 Enumeration en = (Enumeration)session.getAttributeNames();
        	 while (en.hasMoreElements()){
        		String n = (String)en.nextElement();
        		parameterMap.put(n, session.getAttribute(n));
        	 }
        }
        velocityMap.put("parameter", parameterMap);

        return velocityMap;
    }
}
