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
import com.lela.domain.dto.ApiValidationResponse;
import com.lela.web.web.controller.AbstractApiController;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.ServletContextResource;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Bjorn Harvold
 * Date: 11/5/11
 * Time: 3:16 AM
 * Responsibility:
 */
@Controller
public class ApiResourceController extends AbstractApiController implements ServletContextAware {
    private final static Logger log = LoggerFactory.getLogger(ApiResourceController.class);

    private ServletContext servletContext;

    @Autowired
    public ApiResourceController(AffiliateService affiliateService,
                                 ApplicationService applicationService,
                                 CampaignService campaignService,
                                 UserTrackerService userTrackerService,
                                 UserSessionTrackingHelper userSessionTrackingHelper,
                                 UserService userService) {
        super(userService, affiliateService, applicationService, campaignService, userTrackerService, userSessionTrackingHelper);
    }

    /**
     * This retrieves any secured file that lives behind WEB-INF if the caller has the right credentials
     *
     * @param affiliateUrlName   affiliateUrlName
     * @param applicationUrlName applicationUrlName
     * @param request            request
     * @param response           response
     * @throws Exception
     */
    @RequestMapping(value = "/api/resource/{application}/{applicationFile}.{applicationExtension}", method = RequestMethod.GET)
    public void retrieveJavascriptLibrary(@PathVariable("application") String application,
                                          @PathVariable("applicationFile") String applicationFile,
                                          @PathVariable("applicationExtension") String applicationExtension,
                                          @RequestParam("affiliateId") String affiliateUrlName,
                                          @RequestParam("applicationId") String applicationUrlName,
                                          HttpServletResponse response) throws Exception {

        // validate that the caller has the right to retrieve the requested resource
        ApiValidationResponse validationResponse = validateRequest(affiliateUrlName, applicationUrlName);

        if (validationResponse.getValid()) {
            StringBuilder file = new StringBuilder("/WEB-INF/").append(applicationExtension)
                    .append("/").append(application)
                    .append("/").append(applicationFile)
                    .append(".").append(applicationExtension);
            Resource resource = new ServletContextResource(servletContext, file.toString());

            if (resource.exists()) {
                ServletOutputStream sos = response.getOutputStream();

                // write resource to response
                IOUtils.copy(resource.getInputStream(), sos);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

}
