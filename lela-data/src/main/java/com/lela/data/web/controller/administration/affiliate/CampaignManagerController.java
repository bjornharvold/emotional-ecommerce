/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.data.web.controller.administration.affiliate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lela.commons.service.AffiliateService;
import com.lela.commons.service.ApplicationService;
import com.lela.commons.service.CampaignService;
import com.lela.commons.service.StaticContentService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.data.web.validator.CampaignValidator;
import com.lela.domain.document.Campaign;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorn Harvold
 */

@Controller
public class CampaignManagerController {

    //private final static Logger log = LoggerFactory.getLogger(CampaignManagerController.class);

    private final AffiliateService affiliateService;
    private final CampaignService campaignService;
    private final StaticContentService staticContentService;
    private final Validator campaignValidator;
    private final MessageSource messageSource;
    private static final String MAX_RESULTS = "12";

    @Autowired
    public CampaignManagerController(AffiliateService affiliateService,
                                     CampaignService campaignService, ApplicationService applicationService, 
                                     CampaignValidator campaignValidator,
                                     MessageSource messageSource, StaticContentService staticContentService) {
        this.affiliateService = affiliateService;
        this.campaignService = campaignService;
        this.campaignValidator = campaignValidator;
        this.messageSource = messageSource;
        this.staticContentService = staticContentService;
    }

    @RequestMapping(value = "/administration/campaign/list", method = RequestMethod.GET)
    public String showCampaigns(@RequestParam(required = false, defaultValue = "0") Integer page,
                                @RequestParam(required = false, defaultValue = MAX_RESULTS) Integer size,
                                Model model) throws Exception {

        model.addAttribute(WebConstants.CAMPAIGNS, campaignService.findCampaigns(page, size));
        return "campaign.list";
    }

    @RequestMapping(value = "/administration/campaign/form/view", method = RequestMethod.GET)
    public String showCampaign(@RequestParam(value = "urlName", required = true) String urlName,
                               Model model) throws Exception {
        Campaign campaign = campaignService.findCampaignByUrlName(urlName);
        if (campaign != null) {
            model.addAttribute(WebConstants.CAMPAIGN, campaign);
        } 
        populateReferenceData(model);
        return "campaign.form.view";
    }

    @RequestMapping(value = "/administration/campaign/form/edit", method = RequestMethod.GET)
    public String editCampaign(@RequestParam(value = "urlName", required = false) String urlName,
                               Model model) throws Exception {
        if (StringUtils.isNotBlank(urlName)) {
            Campaign campaign = campaignService.findCampaignByUrlName(urlName);
            model.addAttribute(WebConstants.CAMPAIGN, campaign);
        } else {
            model.addAttribute(WebConstants.CAMPAIGN, new Campaign());
        }
        populateReferenceData(model);
        return "campaign.form.edit";
    }
    
    /**
     * Save a campaign
     *
     * @param campaign Campaign
     * @param errors  errors
     * @param model   model
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/campaign/form", method = RequestMethod.POST)
    public String saveCampaign(@Valid Campaign campaign, BindingResult errors,
                                         RedirectAttributes redirectAttributes, 
                                         Locale locale, Model model) throws Exception {


    	campaignValidator.validate(campaign, errors);
        String view = null;
        if (errors.hasErrors()) {
        	model.addAttribute(WebConstants.CAMPAIGN, campaign);
        	populateReferenceData(model);
        	view = "campaign.form.edit";
        } else {
            campaign = campaignService.saveCampaign(campaign);
            view = "redirect:/administration/campaign/form/edit?urlName=" + campaign.getRlnm();
            // add a success message that can be seen on the redirected page
            String message = messageSource.getMessage("campaign.saved.successfully", new String[]{campaign.getRlnm()}, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        }

        return view;
    }
    

    /**
     * Delete a campaign
     *
     * @param urlName urlName
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/campaign/{urlName}", method = RequestMethod.DELETE)
    public String deleteCampaign(@PathVariable(value = "urlName") String urlName,
                                         RedirectAttributes redirectAttributes,
                                         Locale locale) throws Exception {

        campaignService.removeCampaign(urlName);

        // add a success message that can be seen on the redirected page
        String message = messageSource.getMessage("campaign.deleted.successfully", new String[]{urlName}, locale);
        redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);

        return "redirect:/administration/campaign/list";
    }    
    
    
    @RequestMapping(value = "/administration/campaign/users/download", method = RequestMethod.GET)
    public void downloadCampaignUsers(@RequestParam(value = "urlName", required = true) String urlName,
                                      HttpServletResponse response) throws IOException {
        Workbook workbook = affiliateService.generateCampaignUserDetailsReport(urlName);

        response.setHeader("Content-Disposition", "attachment; filename=" + urlName + "-campaign-users.xlsx");
        response.setContentType("application/vnd.ms-excel.12");

        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);

        response.flushBuffer();
    }

    @RequestMapping(value = "/administration/campaign/breakdown/download", method = RequestMethod.GET)
    public void downloadCampaignBreakdown(@RequestParam(value = "urlName", required = true) String urlName,
                                          HttpServletResponse response) throws IOException {
        Workbook workbook = affiliateService.generateCampaignDailyBreakdown(urlName);

        response.setHeader("Content-Disposition", "attachment; filename=" + urlName + "-campaign-daily-breakdown.xlsx");
        response.setContentType("application/vnd.ms-excel.12");

        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);

        response.flushBuffer();
    }

    @RequestMapping(value = "/administration/campaign/trackingreport/download", method = RequestMethod.GET)
    public void downloadTrackingReport(@RequestParam(value = "urlName", required = true) String urlName,
                                       HttpServletResponse response) throws IOException {
        Workbook workbook = affiliateService.generateCampaignUserTrackingReport(urlName);

        response.setHeader("Content-Disposition", "attachment; filename=" + urlName + "-campaign-user-tracking.xlsx");
        response.setContentType("application/vnd.ms-excel.12");

        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);

        response.flushBuffer();
    }

    private void populateReferenceData(Model model) {
        List<String> fields = new ArrayList<String>(3);
        fields.add("nm");
        fields.add("rlnm");
        model.addAttribute(WebConstants.AFFILIATE_ACCOUNTS, affiliateService.findAffiliateAccounts(fields));
        model.addAttribute(WebConstants.STATIC_CONTENTS, staticContentService.findStaticContents(fields));
    }     
}
