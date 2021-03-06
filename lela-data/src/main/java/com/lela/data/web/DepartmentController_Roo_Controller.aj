// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.web;

import com.lela.data.domain.entity.Department;
import com.lela.data.domain.entity.Navbar;
import com.lela.data.web.DepartmentController;
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

privileged aspect DepartmentController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String DepartmentController.create(@Valid Department department, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, department);
            return "crud/departments/create";
        }
        uiModel.asMap().clear();
        department.persist();
        return "redirect:/crud/departments/" + encodeUrlPathSegment(department.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String DepartmentController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Department());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (Navbar.countNavbars() == 0) {
            dependencies.add(new String[] { "navbar", "crud/navbars" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "crud/departments/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String DepartmentController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("department", Department.findDepartment(id));
        uiModel.addAttribute("itemId", id);
        return "crud/departments/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String DepartmentController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("departments", Department.findDepartmentEntries(firstResult, sizeNo));
            float nrOfPages = (float) Department.countDepartments() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("departments", Department.findAllDepartments());
        }
        addDateTimeFormatPatterns(uiModel);
        return "crud/departments/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String DepartmentController.update(@Valid Department department, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, department);
            return "crud/departments/update";
        }
        uiModel.asMap().clear();
        department.merge();
        return "redirect:/crud/departments/" + encodeUrlPathSegment(department.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String DepartmentController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Department.findDepartment(id));
        return "crud/departments/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String DepartmentController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Department department = Department.findDepartment(id);
        department.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/crud/departments";
    }
    
    void DepartmentController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("department_datecreated_date_format", "yyy-MM-dd hh:mm:ss a");
        uiModel.addAttribute("department_datemodified_date_format", "yyy-MM-dd hh:mm:ss a");
    }
    
    void DepartmentController.populateEditForm(Model uiModel, Department department) {
        uiModel.addAttribute("department", department);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("navbars", Navbar.findAllNavbars());
    }
    
    String DepartmentController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
