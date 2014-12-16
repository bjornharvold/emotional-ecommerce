// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.web;

import com.lela.data.domain.entity.Brand;
import com.lela.data.domain.entity.BrandCategory;
import com.lela.data.domain.entity.Category;
import com.lela.data.web.BrandCategoryController;
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

privileged aspect BrandCategoryController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String BrandCategoryController.create(@Valid BrandCategory brandCategory, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, brandCategory);
            return "crud/brandcategorys/create";
        }
        uiModel.asMap().clear();
        brandCategory.persist();
        return "redirect:/crud/brandcategorys/" + encodeUrlPathSegment(brandCategory.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String BrandCategoryController.createForm(Model uiModel) {
        populateEditForm(uiModel, new BrandCategory());
        return "crud/brandcategorys/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String BrandCategoryController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("brandcategory", BrandCategory.findBrandCategory(id));
        uiModel.addAttribute("itemId", id);
        return "crud/brandcategorys/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String BrandCategoryController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("brandcategorys", BrandCategory.findBrandCategoryEntries(firstResult, sizeNo));
            float nrOfPages = (float) BrandCategory.countBrandCategorys() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("brandcategorys", BrandCategory.findAllBrandCategorys());
        }
        addDateTimeFormatPatterns(uiModel);
        return "crud/brandcategorys/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String BrandCategoryController.update(@Valid BrandCategory brandCategory, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, brandCategory);
            return "crud/brandcategorys/update";
        }
        uiModel.asMap().clear();
        brandCategory.merge();
        return "redirect:/crud/brandcategorys/" + encodeUrlPathSegment(brandCategory.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String BrandCategoryController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, BrandCategory.findBrandCategory(id));
        return "crud/brandcategorys/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String BrandCategoryController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        BrandCategory brandCategory = BrandCategory.findBrandCategory(id);
        brandCategory.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/crud/brandcategorys";
    }
    
    void BrandCategoryController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("brandCategory_datecreated_date_format", "yyy-MM-dd hh:mm:ss a");
        uiModel.addAttribute("brandCategory_datemodified_date_format", "yyy-MM-dd hh:mm:ss a");
    }
    
    void BrandCategoryController.populateEditForm(Model uiModel, BrandCategory brandCategory) {
        uiModel.addAttribute("brandCategory", brandCategory);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("brands", Brand.findAllBrands());
        uiModel.addAttribute("categorys", Category.findAllCategorys());
    }
    
    String BrandCategoryController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
