/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.data.web.controller.administration.affiliate;

import com.lela.commons.service.CssStyleService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.data.web.validator.CssStyleValidator;
import com.lela.domain.document.CssStyle;
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
 * User: Chris Tallent
 * Date: 12/4/12
 * Time: 5:13 PM
 */
@Controller
@SessionAttributes(types = CssStyle.class)
public class CssStyleManagerController  {
    private final CssStyleService cssStyleService;
    private final CssStyleValidator cssStyleValidator;
    private final MessageSource messageSource;
    private static final Integer MAX_RESULTS = 25;

    @Autowired
    public CssStyleManagerController(CssStyleService cssStyleService,
                                     CssStyleValidator cssStyleValidator,
                                     MessageSource messageSource) {
        this.cssStyleService = cssStyleService;
        this.cssStyleValidator = cssStyleValidator;
        this.messageSource = messageSource;
    }

    /**
     * Shows a list of applications
     * @param page page
     * @param model model
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/style/list", method = RequestMethod.GET)
    public String showApplications(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                   Model model) throws Exception {
        String view = "admin.style.list";

        model.addAttribute(WebConstants.STYLES, cssStyleService.findStyles(page, MAX_RESULTS));
        return view;
    }

    /**
     * Shows one application
     * @param urlName urlName
     * @param model model
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/style/{urlName}", method = RequestMethod.GET)
    public String showApplication(@PathVariable("urlName") String urlName,
                                  Model model) throws Exception {
        String view = "admin.style";

        model.addAttribute(WebConstants.STYLE, cssStyleService.findStyleByUrlName(urlName));

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
    @RequestMapping(value = "/administration/style/{urlName}", method = RequestMethod.DELETE)
    public String deleteApplication(@PathVariable("urlName") String urlName,
                                    RedirectAttributes redirectAttributes, Locale locale) throws Exception {
        String view = "redirect:/administration/style/list";

        cssStyleService.removeStyle(urlName);

        // add a success message that can be seen on the redirected page
        String message = messageSource.getMessage("style.deleted.successfully", new String[]{urlName}, locale);
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
    @RequestMapping(value = "/administration/style/form", method = RequestMethod.GET)
    public String showApplicationForm(@RequestParam(value = "urlName", required = false) String urlName,
                                      Model model) throws Exception {
        String view = "admin.style.form";

        populateReferenceData(model);

        if (StringUtils.isNotBlank(urlName)) {
            model.addAttribute(WebConstants.STYLE, cssStyleService.findStyleByUrlName(urlName));
        } else {
            model.addAttribute(WebConstants.STYLE, new CssStyle());
        }

        return view;
    }

    /**
     * Save an application
     * @param style application
     * @param errors errors
     * @param redirectAttributes redirectAttributes
     * @param locale locale
     * @param model model
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/style/form", method = RequestMethod.POST)
    public String submitApplicationForm(@Valid CssStyle style, BindingResult errors,
                                        RedirectAttributes redirectAttributes, Locale locale,
                                        Model model) throws Exception {
        String view;

        // check for errors
        cssStyleValidator.validate(style, errors);

        if (errors.hasErrors()) {
            view = "admin.style.form";
            populateReferenceData(model);
            model.addAttribute(WebConstants.STYLE, style);
        } else {
            style = cssStyleService.saveStyle(style);
            view = "redirect:/administration/style/" + style.getRlnm();

            // add a success message that can be seen on the redirected page
            String message = messageSource.getMessage("style.saved.successfully", new String[]{style.getNm()}, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        }

        return view;
    }

    private void populateReferenceData(Model model) {
    }
}

