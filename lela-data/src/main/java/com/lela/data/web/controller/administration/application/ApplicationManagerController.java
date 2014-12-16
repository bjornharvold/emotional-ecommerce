/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.data.web.controller.administration.application;

import com.lela.commons.service.ApplicationService;
import com.lela.commons.service.ProductGridService;
import com.lela.commons.service.QuizService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.data.web.validator.ApplicationValidator;
import com.lela.domain.document.Application;
import com.lela.domain.enums.ApplicationType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Bjorn Harvold
 * Date: 7/3/12
 * Time: 5:19 PM
 * Responsibility: Manages application entries on the admin console
 */
@Controller
@SessionAttributes(types = Application.class)
public class ApplicationManagerController  {

    private final ApplicationService applicationService;
    private final QuizService quizService;
    private final ProductGridService productGridService;
    private final ApplicationValidator applicationValidator; 
    private final MessageSource messageSource;
    private static final Integer MAX_RESULTS = 12;

    @Autowired
    public ApplicationManagerController(ApplicationService applicationService,
                                        QuizService quizService,
                                        ProductGridService productGridService,
                                        ApplicationValidator applicationValidator,
                                        MessageSource messageSource) {
        this.applicationService = applicationService;
        this.quizService = quizService;
        this.productGridService = productGridService;
        this.applicationValidator = applicationValidator;
        this.messageSource = messageSource;
    }

    /**
     * Shows a list of applications
     * @param page page
     * @param model model
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/application/list", method = RequestMethod.GET)
    public String showApplications(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                              Model model) throws Exception {
        String view = "admin.application.list";

        model.addAttribute(WebConstants.APPLICATIONS, applicationService.findApplications(page, MAX_RESULTS));

        return view;
    }

    /**
     * Shows one application
     * @param urlName urlName
     * @param model model
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/application/{urlName}", method = RequestMethod.GET)
    public String showApplication(@PathVariable("urlName") String urlName,
                           Model model) throws Exception {
        String view = "admin.application";

        model.addAttribute(WebConstants.APPLICATION, applicationService.findApplicationByUrlName(urlName));

        return view;
    }

    /**
     * Deletes an application
     * @param urlName urlName
     * @param redirectAttributes redirectAttributes
     * @param locale locale
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/application/{urlName}", method = RequestMethod.DELETE)
    public String deleteApplication(@PathVariable("urlName") String urlName,
                             RedirectAttributes redirectAttributes, Locale locale) throws Exception {
        String view = "redirect:/administration/application/list";

        applicationService.removeApplication(urlName);

        // add a success message that can be seen on the redirected page
        String message = messageSource.getMessage("application.deleted.successfully", new String[]{urlName}, locale);
        redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);

        return view;
    }

    /**
     * Shows application form
     * @param urlName urlName
     * @param model model
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/application/form", method = RequestMethod.GET)
    public String showApplicationForm(@RequestParam(value = "urlName", required = false) String urlName,
                               Model model) throws Exception {
        String view = "admin.application.form";

        populateReferenceData(model);

        if (StringUtils.isNotBlank(urlName)) {
            model.addAttribute(WebConstants.APPLICATION, applicationService.findApplicationByUrlName(urlName));
        } else {
            model.addAttribute(WebConstants.APPLICATION, new Application());
        }

        return view;
    }

    /**
     * Save an application
     * @param application application
     * @param errors errors
     * @param redirectAttributes redirectAttributes
     * @param locale locale
     * @param model model
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/application/form", method = RequestMethod.POST)
    public String submitApplicationForm(@Valid Application application, BindingResult errors,
                                 RedirectAttributes redirectAttributes, Locale locale,
                                 Model model) throws Exception {
        String view;

        // check for errors
        applicationValidator.validate(application, errors);

        if (errors.hasErrors()) {
            view = "admin.application.form";
            populateReferenceData(model);
            model.addAttribute(WebConstants.APPLICATION, application);
        } else {
            application = applicationService.saveApplication(application);
            view = "redirect:/administration/application/" + application.getRlnm();

            // add a success message that can be seen on the redirected page
            String message = messageSource.getMessage("application.saved.successfully", new String[]{application.getNm()}, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        }

        return view;
    }

    private void populateReferenceData(Model model) {
        List<String> fields = new ArrayList<String>();
        fields.add("rlnm");
        fields.add("nm");
        model.addAttribute(WebConstants.QUIZZES, quizService.findQuizzes(fields));

        model.addAttribute(WebConstants.PRODUCT_GRIDS, productGridService.findProductGrids(fields));

        model.addAttribute(WebConstants.APPLICATION_TYPES, ApplicationType.values());
    }
}
