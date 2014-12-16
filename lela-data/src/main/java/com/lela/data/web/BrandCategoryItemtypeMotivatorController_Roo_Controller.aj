// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.web;

import com.lela.data.domain.entity.Brand;
import com.lela.data.domain.entity.BrandCategoryItemtypeMotivator;
import com.lela.data.domain.entity.Category;
import com.lela.data.web.BrandCategoryItemtypeMotivatorController;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect BrandCategoryItemtypeMotivatorController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String BrandCategoryItemtypeMotivatorController.create(@Valid BrandCategoryItemtypeMotivator brandCategoryItemtypeMotivator, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, brandCategoryItemtypeMotivator);
            return "crud/brandcategoryitemtypemotivators/create";
        }
        uiModel.asMap().clear();
        brandCategoryItemtypeMotivator.persist();
        return "redirect:/crud/brandcategoryitemtypemotivators/" + encodeUrlPathSegment(brandCategoryItemtypeMotivator.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String BrandCategoryItemtypeMotivatorController.createForm(Model uiModel) {
        populateEditForm(uiModel, new BrandCategoryItemtypeMotivator());
        return "crud/brandcategoryitemtypemotivators/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String BrandCategoryItemtypeMotivatorController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("brandcategoryitemtypemotivator", BrandCategoryItemtypeMotivator.findBrandCategoryItemtypeMotivator(id));
        uiModel.addAttribute("itemId", id);
        return "crud/brandcategoryitemtypemotivators/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String BrandCategoryItemtypeMotivatorController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("brandcategoryitemtypemotivators", BrandCategoryItemtypeMotivator.findBrandCategoryItemtypeMotivatorEntries(firstResult, sizeNo));
            float nrOfPages = (float) BrandCategoryItemtypeMotivator.countBrandCategoryItemtypeMotivators() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("brandcategoryitemtypemotivators", BrandCategoryItemtypeMotivator.findAllBrandCategoryItemtypeMotivators());
        }
        addDateTimeFormatPatterns(uiModel);
        return "crud/brandcategoryitemtypemotivators/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String BrandCategoryItemtypeMotivatorController.update(@Valid BrandCategoryItemtypeMotivator brandCategoryItemtypeMotivator, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, brandCategoryItemtypeMotivator);
            return "crud/brandcategoryitemtypemotivators/update";
        }
        uiModel.asMap().clear();
        brandCategoryItemtypeMotivator.merge();
        return "redirect:/crud/brandcategoryitemtypemotivators/" + encodeUrlPathSegment(brandCategoryItemtypeMotivator.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String BrandCategoryItemtypeMotivatorController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, BrandCategoryItemtypeMotivator.findBrandCategoryItemtypeMotivator(id));
        return "crud/brandcategoryitemtypemotivators/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String BrandCategoryItemtypeMotivatorController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        BrandCategoryItemtypeMotivator brandCategoryItemtypeMotivator = BrandCategoryItemtypeMotivator.findBrandCategoryItemtypeMotivator(id);
        brandCategoryItemtypeMotivator.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/crud/brandcategoryitemtypemotivators";
    }
    
    void BrandCategoryItemtypeMotivatorController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("brandCategoryItemtypeMotivator_datecreated_date_format", "yyy-MM-dd hh:mm:ss a");
        uiModel.addAttribute("brandCategoryItemtypeMotivator_datemodified_date_format", "yyy-MM-dd hh:mm:ss a");
    }
    
    void BrandCategoryItemtypeMotivatorController.populateEditForm(Model uiModel, BrandCategoryItemtypeMotivator brandCategoryItemtypeMotivator) {
        uiModel.addAttribute("brandCategoryItemtypeMotivator", brandCategoryItemtypeMotivator);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("brands", Brand.findAllBrands());
        uiModel.addAttribute("categorys", Category.findAllCategorys());
    }
    
    String BrandCategoryItemtypeMotivatorController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
    
}