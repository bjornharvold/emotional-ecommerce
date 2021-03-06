// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.web;

import com.lela.data.domain.entity.Category;
import com.lela.data.domain.entity.CategoryCategoryGroup;
import com.lela.data.domain.entity.CategoryGroup;
import com.lela.data.web.CategoryCategoryGroupController;
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

privileged aspect CategoryCategoryGroupController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String CategoryCategoryGroupController.create(@Valid CategoryCategoryGroup categoryCategoryGroup, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, categoryCategoryGroup);
            return "crud/categorycategorygroups/create";
        }
        uiModel.asMap().clear();
        categoryCategoryGroup.persist();
        return "redirect:/crud/categorycategorygroups/" + encodeUrlPathSegment(categoryCategoryGroup.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String CategoryCategoryGroupController.createForm(Model uiModel) {
        populateEditForm(uiModel, new CategoryCategoryGroup());
        return "crud/categorycategorygroups/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String CategoryCategoryGroupController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("categorycategorygroup", CategoryCategoryGroup.findCategoryCategoryGroup(id));
        uiModel.addAttribute("itemId", id);
        return "crud/categorycategorygroups/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String CategoryCategoryGroupController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("categorycategorygroups", CategoryCategoryGroup.findCategoryCategoryGroupEntries(firstResult, sizeNo));
            float nrOfPages = (float) CategoryCategoryGroup.countCategoryCategoryGroups() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("categorycategorygroups", CategoryCategoryGroup.findAllCategoryCategoryGroups());
        }
        addDateTimeFormatPatterns(uiModel);
        return "crud/categorycategorygroups/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String CategoryCategoryGroupController.update(@Valid CategoryCategoryGroup categoryCategoryGroup, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, categoryCategoryGroup);
            return "crud/categorycategorygroups/update";
        }
        uiModel.asMap().clear();
        categoryCategoryGroup.merge();
        return "redirect:/crud/categorycategorygroups/" + encodeUrlPathSegment(categoryCategoryGroup.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String CategoryCategoryGroupController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, CategoryCategoryGroup.findCategoryCategoryGroup(id));
        return "crud/categorycategorygroups/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String CategoryCategoryGroupController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        CategoryCategoryGroup categoryCategoryGroup = CategoryCategoryGroup.findCategoryCategoryGroup(id);
        categoryCategoryGroup.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/crud/categorycategorygroups";
    }
    
    void CategoryCategoryGroupController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("categoryCategoryGroup_datecreated_date_format", "yyy-MM-dd hh:mm:ss a");
        uiModel.addAttribute("categoryCategoryGroup_datemodified_date_format", "yyy-MM-dd hh:mm:ss a");
    }
    
    void CategoryCategoryGroupController.populateEditForm(Model uiModel, CategoryCategoryGroup categoryCategoryGroup) {
        uiModel.addAttribute("categoryCategoryGroup", categoryCategoryGroup);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("categorys", Category.findAllCategorys());
        uiModel.addAttribute("categorygroups", CategoryGroup.findAllCategoryGroups());
    }
    
    String CategoryCategoryGroupController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
