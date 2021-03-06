// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.web;

import com.lela.data.domain.entity.Category;
import com.lela.data.domain.entity.CategorySubdepartment;
import com.lela.data.domain.entity.Subdepartment;
import com.lela.data.web.CategorySubdepartmentController;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
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

privileged aspect CategorySubdepartmentController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String CategorySubdepartmentController.create(@Valid CategorySubdepartment categorySubdepartment, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, categorySubdepartment);
            return "crud/categorysubdepartments/create";
        }
        uiModel.asMap().clear();
        categorySubdepartment.persist();
        return "redirect:/crud/categorysubdepartments/" + encodeUrlPathSegment(categorySubdepartment.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String CategorySubdepartmentController.createForm(Model uiModel) {
        populateEditForm(uiModel, new CategorySubdepartment());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (Category.countCategorys() == 0) {
            dependencies.add(new String[] { "category", "crud/categorys" });
        }
        if (Subdepartment.countSubdepartments() == 0) {
            dependencies.add(new String[] { "subdepartment", "crud/subdepartments" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "crud/categorysubdepartments/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String CategorySubdepartmentController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("categorysubdepartment", CategorySubdepartment.findCategorySubdepartment(id));
        uiModel.addAttribute("itemId", id);
        return "crud/categorysubdepartments/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String CategorySubdepartmentController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("categorysubdepartments", CategorySubdepartment.findCategorySubdepartmentEntries(firstResult, sizeNo));
            float nrOfPages = (float) CategorySubdepartment.countCategorySubdepartments() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("categorysubdepartments", CategorySubdepartment.findAllCategorySubdepartments());
        }
        addDateTimeFormatPatterns(uiModel);
        return "crud/categorysubdepartments/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String CategorySubdepartmentController.update(@Valid CategorySubdepartment categorySubdepartment, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, categorySubdepartment);
            return "crud/categorysubdepartments/update";
        }
        uiModel.asMap().clear();
        categorySubdepartment.merge();
        return "redirect:/crud/categorysubdepartments/" + encodeUrlPathSegment(categorySubdepartment.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String CategorySubdepartmentController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, CategorySubdepartment.findCategorySubdepartment(id));
        return "crud/categorysubdepartments/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String CategorySubdepartmentController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        CategorySubdepartment categorySubdepartment = CategorySubdepartment.findCategorySubdepartment(id);
        categorySubdepartment.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/crud/categorysubdepartments";
    }
    
    void CategorySubdepartmentController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("categorySubdepartment_datecreated_date_format", "yyy-MM-dd hh:mm:ss a");
        uiModel.addAttribute("categorySubdepartment_datemodified_date_format", "yyy-MM-dd hh:mm:ss a");
    }
    
    void CategorySubdepartmentController.populateEditForm(Model uiModel, CategorySubdepartment categorySubdepartment) {
        uiModel.addAttribute("categorySubdepartment", categorySubdepartment);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("categorys", Category.findAllCategorys());
        uiModel.addAttribute("subdepartments", Subdepartment.findAllSubdepartments());
    }
    
    String CategorySubdepartmentController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
