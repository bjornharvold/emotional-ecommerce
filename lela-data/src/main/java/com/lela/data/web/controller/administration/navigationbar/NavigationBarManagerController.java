/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.data.web.controller.administration.navigationbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

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

import com.lela.commons.comparator.LocaleComparator;
import com.lela.commons.service.NavigationBarService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.data.web.validator.NavigationBarValidator;
import com.lela.domain.document.CategoryGroup;
import com.lela.domain.document.NavigationBar;
import com.lela.domain.enums.PublishStatus;

/**
 * Created by Bjorn Harvold
 * Date: 7/3/12
 * Time: 5:19 PM
 * Responsibility: Manages navigationbar entries on the admin console
 */
@Controller
@SessionAttributes(types = {NavigationBar.class, CategoryGroup.class})
public class NavigationBarManagerController  {

    private final NavigationBarService navigationBarService;
    private final NavigationBarValidator navigationBarValidator;
    private final MessageSource messageSource;
    private static final Integer MAX_RESULTS = 12;

    @Autowired
    public NavigationBarManagerController(NavigationBarService navigationBarService,
                                          NavigationBarValidator navigationBarValidator,
                                          MessageSource messageSource) {
        this.navigationBarService = navigationBarService;
        this.navigationBarValidator = navigationBarValidator;
        this.messageSource = messageSource;
    }

    /**
     * Shows a list of navigationbars
     *
     * @param page  page
     * @param model model
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/navigationbar/list", method = RequestMethod.GET)
    public String showNavigationBars(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                     Model model) throws Exception {
        String view = "admin.navigationbar.list";

        model.addAttribute(WebConstants.NAVIGATION_BARS, navigationBarService.findNavigationBars(page, MAX_RESULTS));

        return view;
    }

    /**
     * Shows one navigationbar
     *
     * @param urlName urlName
     * @param model   model
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/navigationbar/{urlName}", method = RequestMethod.GET)
    public String showNavigationBar(@PathVariable("urlName") String urlName,
                                    Model model) throws Exception {
        String view = "admin.navigationbar";

        model.addAttribute(WebConstants.NAVIGATION_BAR, navigationBarService.findNavigationBarByUrlName(urlName));

        return view;
    }

    /**
     * Deletes an navigationbar
     *
     * @param urlName            urlName
     * @param redirectAttributes redirectAttributes
     * @param locale             locale
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/navigationbar/{urlName}", method = RequestMethod.DELETE)
    public String deleteNavigationBar(@PathVariable("urlName") String urlName,
                                      RedirectAttributes redirectAttributes, Locale locale) throws Exception {
        String view = "redirect:/administration/navigationbar/list";

        navigationBarService.removeNavigationBar(urlName);

        // add a success message that can be seen on the redirected page
        String message = messageSource.getMessage("navigationbar.deleted.successfully", new String[]{urlName}, locale);
        redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);

        return view;
    }

    /**
     * Shows navigationbar form
     *
     * @param urlName urlName
     * @param model   model
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/navigationbar/form", method = RequestMethod.GET)
    public String showNavigationBarForm(@RequestParam(value = "urlName", required = false) String urlName,
                                        Model model) throws Exception {
        String view = "admin.navigationbar.form";

        populateReferenceData(model);

        if (StringUtils.isNotBlank(urlName)) {
            model.addAttribute(WebConstants.NAVIGATION_BAR, navigationBarService.findNavigationBarByUrlName(urlName));
        } else {
            model.addAttribute(WebConstants.NAVIGATION_BAR, new NavigationBar());
        }

        return view;
    }

    /**
     * Save an navigationbar
     *
     * @param navigationbar      navigationbar
     * @param errors             errors
     * @param redirectAttributes redirectAttributes
     * @param locale             locale
     * @param model              model
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/navigationbar/form", method = RequestMethod.POST)
    public String submitNavigationBarForm(@Valid NavigationBar navigationbar, BindingResult errors,
                                          RedirectAttributes redirectAttributes, Locale locale,
                                          Model model) throws Exception {
        String view;

        // check for errors
        navigationBarValidator.validate(navigationbar, errors);

        if (errors.hasErrors()) {
            view = "admin.navigationbar.form";
            populateReferenceData(model);
            model.addAttribute(WebConstants.NAVIGATION_BAR, navigationbar);
        } else {
            navigationbar = navigationBarService.saveNavigationBar(navigationbar);
            view = "redirect:/administration/navigationbar/" + navigationbar.getRlnm();

            // add a success message that can be seen on the redirected page
            String message = messageSource.getMessage("navigationbar.saved.successfully", new String[]{navigationbar.getRlnm()}, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        }

        return view;
    }

    /**
     * Show category group form
     * @param urlName urlName
     * @param categoryGroupId categoryGroupId
     * @param model model
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/navigationbar/{urlName}/categorygroup/form", method = RequestMethod.GET)
    public String showCategoryGroupForm(@PathVariable("urlName") String urlName,
                                        @RequestParam(value = "categoryGroupId", required = false) String categoryGroupId,
                                        Model model) throws Exception {
        String view = "admin.categorygroup.form";

        populateCategoryGroupReferenceData(urlName, categoryGroupId, model);

        if (StringUtils.isNotBlank(categoryGroupId)) {
            model.addAttribute(WebConstants.CATEGORY_GROUP, navigationBarService.findCategoryGroup(urlName, categoryGroupId));
        } else {
            model.addAttribute(WebConstants.CATEGORY_GROUP, new CategoryGroup(urlName));
        }

        return view;
    }

    /**
     * Saves category group
     * @param navigationBarUrlName navigationBarUrlName
     * @param categoryGroup categoryGroup
     * @param errors errors
     * @param redirectAttributes redirectAttributes
     * @param locale locale
     * @param model model
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "/administration/navigationbar/{urlName}/categorygroup/form", method = RequestMethod.POST)
    public String submitCategoryGroupForm(@PathVariable("urlName") String navigationBarUrlName,
                                          @Valid CategoryGroup categoryGroup, BindingResult errors,
                                          RedirectAttributes redirectAttributes, Locale locale,
                                          Model model) throws Exception {
        String view;

        if (errors.hasErrors()) {
            view = "admin.categorygroup.form";
            populateCategoryGroupReferenceData(navigationBarUrlName, categoryGroup.getD(), model);
            model.addAttribute(WebConstants.CATEGORY_GROUP, categoryGroup);
        } else {
            navigationBarService.saveCategoryGroup(navigationBarUrlName, categoryGroup);
            view = "redirect:/administration/navigationbar/" + navigationBarUrlName;

            // add a success message that can be seen on the redirected page
            String message = messageSource.getMessage("categorygroup.saved.successfully", new String[]{categoryGroup.getNm()}, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        }

        return view;
    }

    @RequestMapping(value = "/administration/navigationbar/{urlName}/categorygroup/{categoryGroupId}", method = RequestMethod.DELETE)
    public String deleteCategoryGroup(@PathVariable("urlName") String urlName,
                                      @PathVariable("categoryGroupId") String categoryGroupId,
                                      RedirectAttributes redirectAttributes, Locale locale) throws Exception {
        String view = "redirect:/administration/navigationbar/" + urlName;

        navigationBarService.removeCategoryGroup(urlName, categoryGroupId);

        // add a success message that can be seen on the redirected page
        String message = messageSource.getMessage("categorygroup.deleted.successfully", new String[]{urlName}, locale);
        redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);

        return view;
    }

    private void populateReferenceData(Model model) {
        Locale[] locales = Locale.getAvailableLocales();
        Arrays.sort(locales, new LocaleComparator());
        model.addAttribute(WebConstants.LOCALES, locales);
    }

    private void populateCategoryGroupReferenceData(String navigationBarUrlName, String categoryGroupId, Model model) {
        // status list
        model.addAttribute(WebConstants.PUBLISH_STATUSES, PublishStatus.values());

        // list of all category group names minus the one we are currently editing
        NavigationBar nb = navigationBarService.findNavigationBarByUrlName(navigationBarUrlName);
        if (nb != null && nb.getGrps() != null && !nb.getGrps().isEmpty()) {
            List<CategoryGroup> list = new ArrayList<CategoryGroup>();

            // flatten group
            addGroupsToList(nb.getGrps(), list, categoryGroupId);

            model.addAttribute(WebConstants.CATEGORY_GROUP_PARENTS, list);
        }
    }

    private void addGroupsToList(List<CategoryGroup> groups, List<CategoryGroup> list, String categoryGroupId) {
        for (CategoryGroup group : groups) {
            if (!StringUtils.equals(group.getD(), categoryGroupId)) {
                list.add(group);
            }
            if (group.getChldrn() != null && !group.getChldrn().isEmpty()) {
                addGroupsToList(group.getChldrn(), list, categoryGroupId);
            }
        }
    }
}
