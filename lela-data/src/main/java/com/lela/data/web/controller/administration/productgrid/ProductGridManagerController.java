/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.data.web.controller.administration.productgrid;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lela.commons.comparator.LocaleComparator;
import com.lela.commons.service.CategoryService;
import com.lela.commons.service.FunctionalFilterService;
import com.lela.commons.service.ProductGridService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.data.web.validator.ProductGridValidator;
import com.lela.domain.document.ProductGrid;
import com.lela.domain.dto.ProductGridFilters;
import com.lela.domain.enums.FunctionalSortType;

/**
 * Created by Bjorn Harvold
 * Date: 8/21/12
 * Time: 2:02 PM
 * Responsibility:
 */
@Controller
@SessionAttributes(types = {ProductGrid.class})
public class ProductGridManagerController {
    private static final Integer MAX_RESULTS = 12;

    private final ProductGridService productGridService;
    private final MessageSource messageSource;
    private final ProductGridValidator productGridValidator;
    private final CategoryService categoryService;
    private final FunctionalFilterService functionalFilterService;

    @Autowired
    public ProductGridManagerController(ProductGridService productGridService,
                                        MessageSource messageSource,
                                        ProductGridValidator validator,
                                        CategoryService categoryService, FunctionalFilterService functionalFilterService) {
        this.productGridService = productGridService;
        this.messageSource = messageSource;
        this.productGridValidator = validator;
        this.categoryService = categoryService;
        this.functionalFilterService = functionalFilterService;
    }

    @RequestMapping(value = "/administration/productgrid/list", method = RequestMethod.GET)
    public String showProductGrids(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                   Model model) throws Exception {
        model.addAttribute(WebConstants.PRODUCT_GRIDS, productGridService.findProductGrids(page, MAX_RESULTS));

        return "admin.product.grid.list";
    }

    @RequestMapping(value = "/administration/productgrid/{urlName}", method = RequestMethod.GET)
    public String showProductGrid(@PathVariable("urlName") String urlName,
                                  Model model) throws Exception {
        model.addAttribute(WebConstants.PRODUCT_GRID, productGridService.findProductGridByUrlName(urlName));

        return "admin.product.grid";
    }

    @RequestMapping(value = "/administration/productgrid/form", method = RequestMethod.GET)
    public String showProductGridForm(@RequestParam(value = "urlName", required = false) String urlName,
                                      Model model) throws Exception {

        if (StringUtils.isBlank(urlName)) {
            model.addAttribute(WebConstants.PRODUCT_GRID, new ProductGrid());
        } else {
            model.addAttribute(WebConstants.PRODUCT_GRID, productGridService.findProductGridByUrlName(urlName));
        }

        populateReferenceData(model);

        return "admin.product.grid.form";
    }

    @RequestMapping(value = "/administration/productgrid/{urlName}", method = RequestMethod.DELETE)
    public String deleteProductGrid(@PathVariable("urlName") String urlName,
                                    RedirectAttributes redirectAttributes, Locale locale) throws Exception {

        productGridService.removeProductGrid(urlName);

        // add a success message that can be seen on the redirected page
        String message = messageSource.getMessage("product.grid.deleted.successfully", new String[]{urlName}, locale);
        redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);

        return "redirect:/administration/productgrid/list";
    }

    @RequestMapping(value = "/administration/productgrid/form", method = RequestMethod.POST)
    public String submitProductGrid(@Valid ProductGrid pg, BindingResult errors,
                                    RedirectAttributes redirectAttributes,
                                    Model model, Locale locale, SessionStatus status) throws Exception {
        // validate on business rules
        productGridValidator.validate(pg, errors);

        if (errors.hasErrors()) {
            model.addAttribute(WebConstants.PRODUCT_GRID, pg);
            populateReferenceData(model);
            return "admin.product.grid.form";
        }

        pg = productGridService.saveProductGrid(pg);

        // add a success message that can be seen on the redirected page
        String message = messageSource.getMessage("product.grid.saved.successfully", new String[]{pg.getRlnm()}, locale);
        redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);

        status.setComplete();

        return "redirect:/administration/productgrid/" + pg.getRlnm();
    }

    @RequestMapping(value = "/administration/productgrid/{urlName}/filter", method = RequestMethod.GET)
    public String showProductGridFilterForm(@PathVariable("urlName") String urlName,
                                            @RequestParam(value = "categoryUrlName", required = false) String categoryUrlName,
                                            Model model) throws Exception {

        model.addAttribute(WebConstants.PRODUCT_GRID_FILTERS, new ProductGridFilters());
        populateFilterReferenceData(categoryUrlName, model);

        return "admin.product.grid.filter.form";
    }

    @RequestMapping(value = "/administration/productgrid/{urlName}/filter", method = RequestMethod.POST)
    public String submitProductGridFilter(@PathVariable("urlName") String urlName, @ModelAttribute ProductGridFilters filters,
                                    RedirectAttributes redirectAttributes,
                                    Locale locale) throws Exception {

        productGridService.saveProductGridFilters(urlName, filters.getFilters());

        // add a success message that can be seen on the redirected page
        String message = messageSource.getMessage("product.grid.filters.saved.successfully", null, locale);
        redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);

        return "redirect:/administration/productgrid/" + urlName;
    }

    @RequestMapping(value = "/administration/productgrid/{urlName}/filter", method = RequestMethod.DELETE)
    public String deleteProductGridFilter(@PathVariable("urlName") String urlName,
                                          @RequestParam(value = "key", required = true) String key,
                                          RedirectAttributes redirectAttributes, Locale locale) throws Exception {

        productGridService.removeProductGridFilter(urlName, key);

        // add a success message that can be seen on the redirected page
        String message = messageSource.getMessage("product.grid.filters.deleted.successfully", null, locale);
        redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);

        return "redirect:/administration/productgrid/" + urlName;
    }

    private void populateFilterReferenceData(String urlName, Model model) {
        model.addAttribute(WebConstants.FUNCTIONAL_FILTERS, functionalFilterService.findFunctionalFiltersByUrlName(urlName));
    }

    private void populateReferenceData(Model model) {

        FunctionalSortType[] types = FunctionalSortType.values();
        List<FunctionalSortType> typesList = new ArrayList<FunctionalSortType>();
        for (FunctionalSortType type : types) {
            // we want to remove recommended from the default sort options
            if (!type.equals(FunctionalSortType.RECOMMENDED)) {
                typesList.add(type);
            }
        }
        model.addAttribute(WebConstants.FUNCTIONAL_SORT_TYPES, typesList);

        Locale[] locales = Locale.getAvailableLocales();
        Arrays.sort(locales, new LocaleComparator());
        model.addAttribute(WebConstants.LOCALES, locales);

        model.addAttribute(WebConstants.CATEGORIES, categoryService.findCategories());
    }
}
