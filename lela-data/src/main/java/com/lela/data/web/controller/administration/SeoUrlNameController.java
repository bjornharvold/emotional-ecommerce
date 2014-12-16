/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.data.web.controller.administration;

import com.lela.commons.service.SeoUrlNameService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.data.web.validator.SeoUrlNameValidator;
import com.lela.domain.document.SeoUrlName;
import com.lela.domain.enums.SeoUrlNameType;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Locale;

/**
 * Created by Bjorn Harvold
 * Date: 4/14/12
 * Time: 1:51 AM
 * Responsibility: Creates and validates seo url names.
 */
@Controller
@SessionAttributes(types = SeoUrlName.class)
public class SeoUrlNameController {

    private final SeoUrlNameService seoUrlNameService;
    private final SeoUrlNameValidator seoUrlNameValidator;
    private final MessageSource messageSource;

    @Autowired
    public SeoUrlNameController(SeoUrlNameService seoUrlNameService,
                                SeoUrlNameValidator seoUrlNameValidator,
                                MessageSource messageSource) {
        this.seoUrlNameService = seoUrlNameService;
        this.seoUrlNameValidator = seoUrlNameValidator;
        this.messageSource = messageSource;
    }

    @RequestMapping(value = "/administration/seo/list", method = RequestMethod.GET)
    public String showSeoUrlNames(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                  @RequestParam(value = "size", required = false, defaultValue = "1000") Integer size,
                                  Model model) {

        model.addAttribute(WebConstants.SEO_URL_NAMES, seoUrlNameService.findSeoUrlNames(page, size));

        return "seo.list";
    }

    @RequestMapping(value = "/administration/seo", method = RequestMethod.GET)
    public String showSeoUrlName(@RequestParam(value = "urlName", required = false) String urlName,
                                  Model model) {

        populateReferenceData(model);

        if (StringUtils.isNotBlank(urlName)) {
            model.addAttribute(WebConstants.SEO_URL_NAME, seoUrlNameService.findSeoUrlName(urlName));
        } else {
            model.addAttribute(WebConstants.SEO_URL_NAME, new SeoUrlName());
        }

        return "seo.entry";
    }

    @RequestMapping(value = "/administration/seo/{seoUrlName}", method = RequestMethod.DELETE)
    public String deleteSeoUrlName(@PathVariable("seoUrlName") String seoUrlName, RedirectAttributes redirectAttributes, Locale locale) {

        SeoUrlName document = seoUrlNameService.findSeoUrlName(seoUrlName);

        if (document != null) {
            seoUrlNameService.removeSeoUrlName(document);
            // add a success message that can be seen on the redirected page
            String message = messageSource.getMessage("seo.deleted.successfully", new String[]{seoUrlName}, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        }

        return "redirect:/administration/seo/list";
    }

    @RequestMapping(value = "/administration/seo", method = RequestMethod.POST)
    public String saveSeoUrlName(@Valid SeoUrlName seoUrlName, BindingResult errors,
                                 RedirectAttributes redirectAttributes, Model model, Locale locale) {

        if (errors.hasErrors()) {
            model.addAttribute(WebConstants.SEO_URL_NAME, seoUrlName);
            populateReferenceData(model);
            return "seo.entry";
        }

        // validate on business rules
        seoUrlNameValidator.validate(seoUrlName, errors);

        if (errors.hasErrors()) {
            model.addAttribute(WebConstants.SEO_URL_NAME, seoUrlName);
            populateReferenceData(model);
            return "seo.entry";
        }

        seoUrlNameService.saveSeoUrlName(seoUrlName);

        // add a success message that can be seen on the redirected page
        String message = messageSource.getMessage("seo.saved.successfully", new String[]{seoUrlName.getNm()}, locale);
        redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);

        return "redirect:/administration/seo/list";
    }

    private void populateReferenceData(Model model) {
        model.addAttribute(WebConstants.SEO_URL_NAME_TYPE, SeoUrlNameType.values());
    }
}
