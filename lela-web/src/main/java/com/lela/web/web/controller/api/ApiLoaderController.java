/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.web.web.controller.api;

import com.lela.commons.service.AffiliateService;
import com.lela.commons.service.ApplicationService;
import com.lela.commons.service.CampaignService;
import com.lela.commons.service.UserService;
import com.lela.commons.service.UserTrackerService;
import com.lela.commons.utilities.UserSessionTrackingHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.dto.ApiValidationResponse;
import com.lela.domain.enums.ApplicationType;
import com.lela.web.web.controller.AbstractApiController;
import nl.bitwalker.useragentutils.Browser;
import nl.bitwalker.useragentutils.UserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Chris Tallent
 * Date: 10/1/12
 * Time: 1:30 PM
 */
@Controller
public class ApiLoaderController extends AbstractApiController {
    private static final Logger log = LoggerFactory.getLogger(ApiLoaderController.class);

    @Autowired
    public ApiLoaderController(UserService userService,
                               AffiliateService affiliateService,
                               ApplicationService applicationService,
                               CampaignService campaignService,
                               UserTrackerService userTrackerService,
                               UserSessionTrackingHelper userSessionTrackingHelper) {
        super(userService, affiliateService, applicationService, campaignService, userTrackerService, userSessionTrackingHelper);
    }

    @RequestMapping(value = "/api/resource/quiz/quiz-loader.js", method = RequestMethod.GET)
    public String getQuizLoaderJavascript(@RequestParam("affiliateId") String affiliateUrlName,
                                          @RequestParam("applicationId") String applicationUrlName,
                                          @RequestParam(value = "campaignId", required = false) String campaignId,
                                          HttpServletRequest request,
                                          HttpServletResponse response,
                                          HttpSession session,
                                          Model model) {
        // validate that the caller has the right to retrieve the requested resource
        ApiValidationResponse validationResponse = validateRequest(affiliateUrlName, applicationUrlName);

        if (validationResponse.getValid()) {
            model.addAttribute("affiliate", validationResponse.getAffiliateAccount());
            model.addAttribute("application", validationResponse.getApplication());
            model.addAttribute("baseUrl", getBaseUrl(request));

            return "api.loader.quiz.js";
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        return null;
    }

    @RequestMapping(value = "/api/resource/productgrid/productgrid.js", method = RequestMethod.GET)
    public String getGridLoaderJavascript(@RequestParam("affiliateId") String affiliateUrlName,
                                          @RequestParam("applicationId") String applicationUrlName,
                                          HttpServletRequest request,
                                          HttpServletResponse response,
                                          HttpSession session,
                                          Model model) {
        // validate that the caller has the right to retrieve the requested resource
        ApiValidationResponse validationResponse = validateRequest(affiliateUrlName, applicationUrlName);

        if (validationResponse.getValid()) {
            model.addAttribute("userCode", session.getAttribute(WebConstants.SESSION_USER_CODE));
            model.addAttribute("affiliate", validationResponse.getAffiliateAccount());
            model.addAttribute("application", validationResponse.getApplication());
            model.addAttribute("baseUrl", getBaseUrl(request));
            return "api.loader.productgrid.js";
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        return null;
    }

    @RequestMapping(value = "/api/v2/widget.js", method = RequestMethod.GET)
    public String getLoaderJavascript(@RequestParam("_aid") String affiliateUrlName,
                                      @RequestParam(value = "preload", required = false) String preloads,
                                      HttpServletRequest request,
                                      HttpServletResponse response,
                                      HttpSession session,
                                      Model model) {
        // validate that the caller has the right to retrieve the requested resource
        AffiliateAccount affiliate = affiliateService.findActiveAffiliateAccountByUrlName(affiliateUrlName);
        if (affiliate != null) {
            UserAgent ua = new UserAgent(request.getHeader(WebConstants.USER_AGENT));
            model.addAttribute("requireXdr", Browser.IE8.equals(ua.getBrowser()) || Browser.IE9.equals(ua.getBrowser()));

            model.addAttribute("userCode", session.getAttribute(WebConstants.SESSION_USER_CODE));
            model.addAttribute("affiliate", affiliate);
            model.addAttribute("baseUrl", getBaseUrl(request));

            Map<String, Boolean> preload = new HashMap<String, Boolean>();
            if (preloads != null) {
                for (String widget : preloads.split(",")) {
                    ApplicationType type = ApplicationType.getByRlnm(widget);
                    if (type != null) {
                        preload.put(widget, true);
                    } else {
                        log.error("Invalid application type requested for preload: " + widget);
                    }
                }
            }
            model.addAttribute("preload", preload);

            return "api.loader.js";
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        return null;
    }

    private String getBaseUrl(HttpServletRequest request) {
        String url = request.getScheme() + "://" + request.getServerName();
        if (request.getServerPort() != 80) {
            url += ":" + request.getServerPort();
        }

        return url;
    }
}
