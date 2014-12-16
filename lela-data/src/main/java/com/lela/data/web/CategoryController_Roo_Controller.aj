// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.web;

import com.lela.data.domain.entity.Category;
import com.lela.data.domain.entity.CategoryCategoryGroup;
import com.lela.data.domain.entity.DataSourceType;
import com.lela.data.web.CategoryController;
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

privileged aspect CategoryController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String CategoryController.create(@Valid Category category, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, category);
            return "crud/categorys/create";
        }
        uiModel.asMap().clear();
        category.persist();
        return "redirect:/crud/categorys/" + encodeUrlPathSegment(category.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String CategoryController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Category());
        return "crud/categorys/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String CategoryController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("category", Category.findCategory(id));
        uiModel.addAttribute("itemId", id);
        return "crud/categorys/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String CategoryController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("categorys", Category.findCategoryEntries(firstResult, sizeNo));
            float nrOfPages = (float) Category.countCategorys() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("categorys", Category.findAllCategorys());
        }
        addDateTimeFormatPatterns(uiModel);
        return "crud/categorys/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String CategoryController.update(@Valid Category category, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, category);
            return "crud/categorys/update";
        }
        uiModel.asMap().clear();
        category.merge();
        return "redirect:/crud/categorys/" + encodeUrlPathSegment(category.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String CategoryController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Category.findCategory(id));
        return "crud/categorys/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String CategoryController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Category category = Category.findCategory(id);
        category.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/crud/categorys";
    }
    
    void CategoryController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("category_datecreated_date_format", "yyy-MM-dd hh:mm:ss a");
        uiModel.addAttribute("category_datemodified_date_format", "yyy-MM-dd hh:mm:ss a");
    }
    
    void CategoryController.populateEditForm(Model uiModel, Category category) {
        uiModel.addAttribute("category", category);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("categorycategorygroups", CategoryCategoryGroup.findAllCategoryCategoryGroups());
        uiModel.addAttribute("datasourcetypes", DataSourceType.findAllDataSourceTypes());
    }
    
    String CategoryController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
