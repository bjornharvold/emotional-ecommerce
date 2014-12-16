/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.data.web.controller.administration.affiliate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Future;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.lela.commons.service.CssStyleService;
import com.lela.commons.service.StoreService;
import com.lela.domain.document.AffiliateAccountCssStyle;
import com.lela.domain.document.AffiliateAccountStore;
import com.lela.domain.document.Store;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lela.commons.service.AffiliateService;
import com.lela.commons.service.ApplicationService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.data.web.validator.AffiliateAccountAndImageValidator;
import com.lela.domain.ApplicationConstants;
import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.AffiliateAccountApplication;
import com.lela.domain.document.Application;
import com.lela.domain.dto.AffiliateAccountAndImage;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorn Harvold
 */

@Controller
@SessionAttributes(types = {AffiliateAccountAndImage.class, AffiliateAccountCssStyle.class})
public class AffiliateManagerController  {

    private final static Logger log = LoggerFactory.getLogger(AffiliateManagerController.class);

    private final AffiliateService affiliateService;
    private final ApplicationService applicationService;
    private final CssStyleService cssStyleService;
    private final StoreService storeService;
    private final AffiliateAccountAndImageValidator affiliateAccountAndImageValidator;
    private final MessageSource messageSource;
    
    private static final Integer MAX_RESULTS = 12;
    
    @Value("${preview.url:http://www.lela.com}")
    private String previewUrl;

    @Value ("${numberOfSecondsToWaitForReportToGenerate:5}")
    private int numberOfSecondsToWaitForReportToGenerate;
    
    @Autowired
    public AffiliateManagerController(AffiliateService affiliateService,
                                      ApplicationService applicationService,
                                      CssStyleService cssStyleService,
                                      StoreService storeService,
                                      AffiliateAccountAndImageValidator affiliateAccountAndImageValidator,
                                      MessageSource messageSource) {
        this.affiliateService = affiliateService;
        this.applicationService = applicationService;
        this.cssStyleService = cssStyleService;
        this.storeService = storeService;
        this.messageSource = messageSource;
        this.affiliateAccountAndImageValidator = affiliateAccountAndImageValidator;
    }
    /**
     * List all affiliate accounts
     *
     * @param page  page
     * @param model model
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/affiliateaccount/list", method = RequestMethod.GET)
    public String showAffiliateAccounts(@RequestParam(required = false, defaultValue = "0") Integer page,
                                        @RequestParam(required = false, defaultValue = "false") boolean includeInactive,
                                        Model model) throws Exception {

        model.addAttribute("includeInactive", includeInactive);
        model.addAttribute(WebConstants.AFFILIATE_ACCOUNTS, affiliateService.findAffiliateAccounts(includeInactive, page, MAX_RESULTS));
        
        return "admin.affiliate.account.list";
    }

    /**
     * Show specified affiliate account
     *
     * @param urlName urlName
     * @param model   model
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/affiliateaccount/{urlName}", method = RequestMethod.GET)
    public String showAffiliateAccount(@PathVariable("urlName") String urlName,
                                       Model model) throws Exception {

        AffiliateAccount affiliateAccount = affiliateService.findAffiliateAccountByUrlName(urlName);
        AffiliateAccountAndImage dto = new AffiliateAccountAndImage(affiliateAccount);
        model.addAttribute(WebConstants.AFFILIATE_ACCOUNT, dto);
        model.addAttribute(WebConstants.PREVIEW_URL, previewUrl);
        return "admin.affiliate.account";
    }

    /**
     * Show affiliate account form
     *
     * @param urlName urlName
     * @param model   model
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/affiliateaccount/form", method = RequestMethod.GET)
    public String showAffiliateAccountForm(@RequestParam(value = "urlName", required = false) String urlName,
                                           Model model) throws Exception {
        if (StringUtils.isNotBlank(urlName)) {
            AffiliateAccount affiliateAccount = affiliateService.findAffiliateAccountByUrlName(urlName);
            AffiliateAccountAndImage dto = new AffiliateAccountAndImage(affiliateAccount);
            model.addAttribute(WebConstants.AFFILIATE_ACCOUNT, dto);
        } else {
            model.addAttribute(WebConstants.AFFILIATE_ACCOUNT, new AffiliateAccountAndImage());
        }

        return "admin.affiliate.account.form";
    }

    /**
     * Save an affiliat account
     *
     * @param affiliateAccount account and image
     * @param errors  errors
     * @param model   model
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/affiliateaccount/form", method = RequestMethod.POST)
    public String submitAffiliateAccount(@ModelAttribute(WebConstants.AFFILIATE_ACCOUNT) @Valid AffiliateAccountAndImage affiliateAccount, BindingResult errors,
                                         RedirectAttributes redirectAttributes,
                                         Locale locale, Model model) throws Exception {
        String view;

        affiliateAccountAndImageValidator.validate(affiliateAccount, errors);

        if (errors.hasErrors()) {
            model.addAttribute(WebConstants.AFFILIATE_ACCOUNT, affiliateAccount);
            view = "admin.affiliate.account.form";
        } else {
            affiliateAccount = affiliateService.saveAffiliateAccountAndImage(affiliateAccount);
            view = "redirect:/administration/affiliateaccount/" + affiliateAccount.getAffiliate().getRlnm();

            // add a success message that can be seen on the redirected page
            String message = messageSource.getMessage("affiliate.account.saved.successfully", new String[]{affiliateAccount.getAffiliate().getRlnm()}, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        }

        return view;
    }

    /**
     * Delete an affiliate account
     *
     * @param urlName urlName
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/affiliateaccount/{urlName}", method = RequestMethod.DELETE)
    public String deleteAffiliateAccount(@PathVariable(value = "urlName") String urlName,
                                         RedirectAttributes redirectAttributes,
                                         Locale locale) throws Exception {

        affiliateService.removeAffiliateAccount(urlName);

        // add a success message that can be seen on the redirected page
        String message = messageSource.getMessage("affiliate.account.deleted.successfully", new String[]{urlName}, locale);
        redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);

        return "redirect:/administration/affiliateaccount/list";
    }

    /**
     * Show affiliate account application form
     *
     * @param model model
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/affiliateaccount/{urlName}/application/form", method = RequestMethod.GET)
    public String showAffiliateAccountApplicationForm(@PathVariable(value = "urlName") String urlName,
                                                      Model model) throws Exception {

        model.addAttribute(WebConstants.AFFILIATE_ACCOUNT_APPLICATION, new AffiliateAccountApplication(urlName));
        populateAccountAffiliateApplicationReferenceData(model);

        return "admin.affiliate.account.application.form";
    }

    /**
     * Saves affiliate account application
     *
     * @param urlName     urlName
     * @param application application
     * @param errors      errors
     * @param model       model
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/affiliateaccount/{urlName}/application/form", method = RequestMethod.POST)
    public String submitAffiliateAccountApplication(@PathVariable(value = "urlName") String urlName,
                                                    @Valid AffiliateAccountApplication application,
                                                    BindingResult errors,
                                                    RedirectAttributes redirectAttributes,
                                                    Locale locale,
                                                    Model model) throws Exception {
        String view;

        if (errors.hasErrors()) {
            model.addAttribute(WebConstants.AFFILIATE_ACCOUNT_APPLICATION, application);
            populateAccountAffiliateApplicationReferenceData(model);
            view = "admin.affiliate.account.form";
        } else {
            // Add a little extra knowledge to the AffiliateAccountApplication object
            Application app = applicationService.findApplicationByUrlName(application.getRlnm());
            application.setTp(app.getTp());
            affiliateService.saveAffiliateAccountApplication(application);
            view = "redirect:/administration/affiliateaccount/" + urlName;

            // add a success message that can be seen on the redirected page
            String message = messageSource.getMessage("affiliate.account.application.saved.successfully", new String[]{application.getRlnm()}, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        }

        return view;
    }

    /**
     * Deletes affiliate account application
     *
     * @param urlName            urlName
     * @param applicationUrlName applicationUrlName
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/affiliateaccount/{urlName}/application/{applicationUrlName}", method = RequestMethod.DELETE)
    public String deleteAffiliateAccountApplication(@PathVariable(value = "urlName") String urlName,
                                                    @PathVariable(value = "applicationUrlName") String applicationUrlName,
                                                    RedirectAttributes redirectAttributes,
                                                    Locale locale) throws Exception {

        affiliateService.removeAffiliateAccountApplication(urlName, applicationUrlName);

        // add a success message that can be seen on the redirected page
        String message = messageSource.getMessage("affiliate.account.application.deleted.successfully", new String[]{applicationUrlName}, locale);
        redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);

        return "redirect:/administration/affiliateaccount/" + urlName;
    }

    /**
     * Show affiliate account style form
     *
     * @param model model
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/affiliateaccount/{urlName}/style/form", method = RequestMethod.GET)
    public String editAffiliateAccountCssStyleForm(@PathVariable(value = "urlName") String urlName,
                                                   @RequestParam(value = "styleUrlName", required = false) String styleUrlName,
                                                   Model model) throws Exception {

        if (StringUtils.isNotBlank(styleUrlName)) {
            AffiliateAccount affiliateAccount = affiliateService.findAffiliateAccountByUrlName(urlName);
            AffiliateAccountCssStyle style = affiliateAccount.findCssStyle(styleUrlName);
            style.setAffiliateAccountUrlName(urlName);
            model.addAttribute(WebConstants.AFFILIATE_ACCOUNT_CSS_STYLE, style);
        } else {
            model.addAttribute(WebConstants.AFFILIATE_ACCOUNT_CSS_STYLE, new AffiliateAccountCssStyle(urlName));
        }

        populateAccountAffiliateCssStyleReferenceData(model);

        return "admin.affiliate.account.style.form";
    }

    /**
     * Saves affiliate account style
     *
     * @param urlName     urlName
     * @param style application
     * @param errors      errors
     * @param model       model
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/affiliateaccount/{urlName}/style/form", method = RequestMethod.POST)
    public String submitAffiliateAccountCssStyle(@PathVariable(value = "urlName") String urlName,
                                                    @Valid AffiliateAccountCssStyle style,
                                                    BindingResult errors,
                                                    RedirectAttributes redirectAttributes,
                                                    Locale locale,
                                                    Model model) throws Exception {
        String view;

        if (errors.hasErrors()) {
            model.addAttribute(WebConstants.AFFILIATE_ACCOUNT_CSS_STYLE, style);
            populateAccountAffiliateCssStyleReferenceData(model);
            view = "admin.affiliate.account.form";
        } else {
//            style.setAffiliateAccountUrlName(urlName);
            affiliateService.saveAffiliateAccountCssStyle(style);
            view = "redirect:/administration/affiliateaccount/" + urlName;

            // add a success message that can be seen on the redirected page
            String message = messageSource.getMessage("affiliate.account.style.saved.successfully", new String[]{style.getRlnm()}, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        }

        return view;
    }

    /**
     * Deletes affiliate account application
     *
     * @param urlName            urlName
     * @param styleUrlName styleUrlName
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/affiliateaccount/{urlName}/style/{styleUrlName}", method = RequestMethod.DELETE)
    public String deleteAffiliateAccountCssStyle(@PathVariable(value = "urlName") String urlName,
                                                 @PathVariable(value = "styleUrlName") String styleUrlName,
                                                 RedirectAttributes redirectAttributes,
                                                 Locale locale) throws Exception {

        affiliateService.removeAffiliateAccountCssStyle(urlName, styleUrlName);

        // add a success message that can be seen on the redirected page
        String message = messageSource.getMessage("affiliate.account.style.deleted.successfully", new String[]{styleUrlName}, locale);
        redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);

        return "redirect:/administration/affiliateaccount/" + urlName;
    }

    /**
     * Show affiliate account application form
     *
     * @param model model
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/affiliateaccount/{urlName}/store/form", method = RequestMethod.GET)
    public String showAffiliateAccountStoreForm(@PathVariable(value = "urlName") String urlName,
                                                      Model model) throws Exception {

        model.addAttribute(WebConstants.AFFILIATE_ACCOUNT_STORE, new AffiliateAccountStore(urlName));
        populateAccountAffiliateStoreReferenceData(model);

        return "admin.affiliate.account.store.form";
    }

    /**
     * Saves affiliate account application
     *
     * @param urlName     urlName
     * @param dto application
     * @param errors      errors
     * @param model       model
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/affiliateaccount/{urlName}/store/form", method = RequestMethod.POST)
    public String submitAffiliateAccountStore(@PathVariable(value = "urlName") String urlName,
                                                @Valid AffiliateAccountStore dto,
                                                BindingResult errors,
                                                RedirectAttributes redirectAttributes,
                                                Locale locale,
                                                Model model) throws Exception {
        String view;

        if (errors.hasErrors()) {
            model.addAttribute(WebConstants.AFFILIATE_ACCOUNT_STORE, dto);
            populateAccountAffiliateStoreReferenceData(model);
            view = "admin.affiliate.account.store.form";
        } else {
            Store store = storeService.findStoreByUrlName(dto.getRlnm());
            dto.setNm(store.getNm());
            dto.setAffiliateAccountUrlName(urlName);
            affiliateService.saveAffiliateAccountStore(dto);
            view = "redirect:/administration/affiliateaccount/" + urlName;

            // add a success message that can be seen on the redirected page
            String message = messageSource.getMessage("affiliate.account.store.saved.successfully", new String[]{store.getRlnm()}, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        }

        return view;
    }

    /**
     * Deletes affiliate account application
     *
     * @param urlName            urlName
     * @param storeUrlName applicationUrlName
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/affiliateaccount/{urlName}/store/{storeUrlName}", method = RequestMethod.DELETE)
    public String deleteAffiliateAccountStore(@PathVariable(value = "urlName") String urlName,
                                                    @PathVariable(value = "storeUrlName") String storeUrlName,
                                                    RedirectAttributes redirectAttributes,
                                                    Locale locale) throws Exception {

        affiliateService.removeAffiliateAccountStore(urlName, storeUrlName);

        // add a success message that can be seen on the redirected page
        String message = messageSource.getMessage("affiliate.account.store.deleted.successfully", new String[]{storeUrlName}, locale);
        redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);

        return "redirect:/administration/affiliateaccount/" + urlName;
    }

    @RequestMapping(value = "/administration/affiliateaccount/users/generate", method = RequestMethod.GET)
    public String generateAffiliateUsersReport(@RequestParam(value = "urlName", required = true) String urlName,
    		RedirectAttributes redirectAttributes,
    		HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

    	String reportId = ApplicationConstants.REPORT_ID_PREFIX + System.currentTimeMillis(); 
    	Future<Workbook> workbookResultFuture = affiliateService.generateAffiliateUserDetailsReport(urlName);
    	
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
    		returnInResponse(urlName + "-affiliate-users.xlsx", response, workbook);
    	} else {
    		log.debug("ReportId with id: " + reportId + " will taking longer than " + numberOfSecondsToWaitForReportToGenerate + " seconds");
    		//Also place the async task handle in session keyed off the unique reportId
    		request.getSession(true).setAttribute(ApplicationConstants.REPORT_TASK_PREFIX + reportId, workbookResultFuture);
    		redirectAttributes.addFlashAttribute("usersReportFeedback", true);
    		redirectAttributes.addFlashAttribute("reportId", reportId);
            String message = messageSource.getMessage("long.running.report.message", new String[]{String.valueOf(numberOfSecondsToWaitForReportToGenerate)}, locale);
            redirectAttributes.addFlashAttribute("reportStatus", message);
    	}
    	return "redirect:/administration/affiliateaccount/" + urlName;
    	

    }

    @RequestMapping(value = "/administration/affiliateaccount/users/download", method = RequestMethod.GET)
    public String downloadAffiliateUsersReport(@RequestParam(value = "reportId", required = true) String reportId,
    		@RequestParam(value = "urlName", required = true) String urlName,
    		RedirectAttributes redirectAttributes,
    		HttpServletRequest request, HttpServletResponse response,  Locale locale) throws Exception {

    	//Pull the async task handle form session
		@SuppressWarnings("unchecked")
		Future<Workbook> workbookResultFuture = (Future<Workbook>)request.getSession(true).getAttribute(ApplicationConstants.REPORT_TASK_PREFIX + reportId);
		if (workbookResultFuture != null && workbookResultFuture.isDone()){
		     
            //Cleanup
			request.getSession().removeAttribute(reportId);
			request.getSession().removeAttribute(ApplicationConstants.REPORT_TASK_PREFIX);
			
			Workbook workbook = workbookResultFuture.get();
			returnInResponse(urlName + "-affiliate-users.xlsx", response, workbook);
		} else {
			log.debug("ReportId with id: " + reportId + " still generating!");
			redirectAttributes.addFlashAttribute("usersReportFeedback", true);
    		redirectAttributes.addFlashAttribute("reportId", reportId);
            String message = messageSource.getMessage("long.running.report.message.inprogress", null, locale);
            redirectAttributes.addFlashAttribute("reportStatus", message);
		}
		return "redirect:/administration/affiliateaccount/" + urlName;
    }
    
    
    @RequestMapping(value = "/administration/affiliateaccount/trackingreport/generate", method = RequestMethod.GET)
    public String generateTrackingReport(@RequestParam(value = "urlName", required = true) String urlName,
    		RedirectAttributes redirectAttributes,
    		HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

    	String reportId = ApplicationConstants.REPORT_ID_PREFIX + System.currentTimeMillis(); 
    	Future<Workbook> workbookResultFuture = affiliateService.generateAffiliateUserTrackingReport(urlName); 
    	
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
    		
    		returnInResponse(urlName + "-affiliate-user-tracking.xlsx", response, workbook);
    	} else {
    		log.debug("ReportId with id: " + reportId + " will taking longer than " + numberOfSecondsToWaitForReportToGenerate + " seconds");
    		//Also place the async task handle in session keyed off the unique reportId
    		request.getSession(true).setAttribute(ApplicationConstants.REPORT_TASK_PREFIX + reportId, workbookResultFuture);
    		redirectAttributes.addFlashAttribute("trackingReportFeedback", true);
    		redirectAttributes.addFlashAttribute("reportId", reportId);
			redirectAttributes.addFlashAttribute("downloadLink", "/administration/affiliateaccount/trackingreport/download"); 
            String message = messageSource.getMessage("long.running.report.message", new String[]{String.valueOf(numberOfSecondsToWaitForReportToGenerate)}, locale);
            redirectAttributes.addFlashAttribute("reportStatus", message);
    	}
    	return "redirect:/administration/affiliateaccount/" + urlName;
    	

    }


    @RequestMapping(value = "/administration/affiliateaccount/trackingreport/download", method = RequestMethod.GET)
    public String downloadTrackingReport(@RequestParam(value = "reportId", required = true) String reportId,
    		@RequestParam(value = "urlName", required = true) String urlName,
    		RedirectAttributes redirectAttributes,
    		HttpServletRequest request, HttpServletResponse response,  Locale locale) throws Exception {

    	//Pull the async task handle form session
		@SuppressWarnings("unchecked")
		Future<Workbook> workbookResultFuture = (Future<Workbook>)request.getSession(true).getAttribute(ApplicationConstants.REPORT_TASK_PREFIX + reportId);
		if (workbookResultFuture != null && workbookResultFuture.isDone()){
            //Cleanup
			request.getSession().removeAttribute(reportId);
			request.getSession().removeAttribute(ApplicationConstants.REPORT_TASK_PREFIX);
			
			Workbook workbook = workbookResultFuture.get();
			returnInResponse(urlName + "-affiliate-user-tracking.xlsx", response, workbook);
		} else {
			log.debug("ReportId with id: " + reportId + " still generating!");
			redirectAttributes.addFlashAttribute("trackingReportFeedback", true);
    		redirectAttributes.addFlashAttribute("reportId", reportId);
    		redirectAttributes.addFlashAttribute("downloadLink", "/administration/affiliateaccount/trackingreport/download"); 
            String message = messageSource.getMessage("long.running.report.message.inprogress", null, locale);
            redirectAttributes.addFlashAttribute("reportStatus", message);
		}
		return "redirect:/administration/affiliateaccount/" + urlName;
    }
    
    @RequestMapping(value = "/administration/affiliateaccount/report/registrationsbyaffiliate/generate", method = RequestMethod.GET)
    public String generateRegistrationsByAffiliateReport(HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

    	String reportId = ApplicationConstants.REPORT_ID_PREFIX + System.currentTimeMillis(); 
    	Future<Workbook> workbookResultFuture = affiliateService.generateRegistrationsByAffiliateReport();  
    	
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
    		
    		returnInResponse("registrations-by-affiliate-user-tracking.xlsx", response, workbook);
    	} else {
    		log.debug("ReportId with id: " + reportId + " will taking longer than " + numberOfSecondsToWaitForReportToGenerate + " seconds");
    		//Also place the async task handle in session keyed off the unique reportId
    		request.getSession(true).setAttribute(ApplicationConstants.REPORT_TASK_PREFIX + reportId, workbookResultFuture);
    		request.setAttribute("reportFeedback", true);
    		request.setAttribute("reportId", reportId);
    		request.setAttribute("downloadLink", "/administration/affiliateaccount/report/registrationsbyaffiliate/download"); 
            String message = messageSource.getMessage("long.running.report.message", new String[]{String.valueOf(numberOfSecondsToWaitForReportToGenerate)}, locale);
            request.setAttribute("reportStatus", message);
    	}
    	return "admin.report.status";
    	

    }
    @RequestMapping(value = "/administration/affiliateaccount/report/registrationsbyaffiliate/download", method = RequestMethod.GET)
    public String downloadRegistrationsByAffiliateReport(@RequestParam(value = "reportId", required = true) String reportId,
    		HttpServletRequest request, HttpServletResponse response,  Locale locale) throws Exception {

    	//Pull the async task handle form session
		@SuppressWarnings("unchecked")
		Future<Workbook> workbookResultFuture = (Future<Workbook>)request.getSession(true).getAttribute(ApplicationConstants.REPORT_TASK_PREFIX + reportId);
		if (workbookResultFuture != null && workbookResultFuture.isDone()){
            //Cleanup
			request.getSession().removeAttribute(reportId);
			request.getSession().removeAttribute(ApplicationConstants.REPORT_TASK_PREFIX);
			
			Workbook workbook = workbookResultFuture.get();
			returnInResponse("registrations-by-affiliate-user-tracking.xlsx", response, workbook);
		} else {
			log.debug("ReportId with id: " + reportId + " still generating!");
			request.setAttribute("reportFeedback", true);
			request.setAttribute("reportId", reportId);
			request.setAttribute("downloadLink", "/administration/affiliateaccount/report/registrationsbyaffiliate/download"); 
            String message = messageSource.getMessage("long.running.report.message.inprogress", null, locale);
            request.setAttribute("reportStatus", message);
		}
		return "admin.report.status";
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
    
    private void populateAccountAffiliateApplicationReferenceData(Model model) {
        List<String> fields = new ArrayList<String>(3);
        fields.add("nm");
        fields.add("rlnm");
        fields.add("tp");
        model.addAttribute(WebConstants.APPLICATIONS, applicationService.findApplications(fields));
    }

    private void populateAccountAffiliateCssStyleReferenceData(Model model) {
        model.addAttribute(WebConstants.STYLES, cssStyleService.findCssStyles());
    }

    private void populateAccountAffiliateStoreReferenceData(Model model) {
        List<String> fields = new ArrayList<String>(3);
        fields.add("nm");
        fields.add("rlnm");
        fields.add("mrchntd");
        model.addAttribute(WebConstants.STORES, storeService.findStores(fields));
    }

	public void setNumberOfSecondsToWaitForReportToGenerate(
			int numberOfSecondsToWaitForReportToGenerate) {
		this.numberOfSecondsToWaitForReportToGenerate = numberOfSecondsToWaitForReportToGenerate;
	}
}
