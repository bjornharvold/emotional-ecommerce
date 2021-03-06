// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.web;

import com.lela.data.domain.entity.Department;
import com.lela.data.domain.entity.Subdepartment;
import com.lela.data.domain.entity.SubdepartmentDepartment;
import com.lela.data.web.SubdepartmentDepartmentController;
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

privileged aspect SubdepartmentDepartmentController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String SubdepartmentDepartmentController.create(@Valid SubdepartmentDepartment subdepartmentDepartment, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, subdepartmentDepartment);
            return "crud/subdepartmentdepartments/create";
        }
        uiModel.asMap().clear();
        subdepartmentDepartment.persist();
        return "redirect:/crud/subdepartmentdepartments/" + encodeUrlPathSegment(subdepartmentDepartment.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String SubdepartmentDepartmentController.createForm(Model uiModel) {
        populateEditForm(uiModel, new SubdepartmentDepartment());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (Subdepartment.countSubdepartments() == 0) {
            dependencies.add(new String[] { "subdepartment", "crud/subdepartments" });
        }
        if (Department.countDepartments() == 0) {
            dependencies.add(new String[] { "department", "crud/departments" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "crud/subdepartmentdepartments/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String SubdepartmentDepartmentController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("subdepartmentdepartment", SubdepartmentDepartment.findSubdepartmentDepartment(id));
        uiModel.addAttribute("itemId", id);
        return "crud/subdepartmentdepartments/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String SubdepartmentDepartmentController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("subdepartmentdepartments", SubdepartmentDepartment.findSubdepartmentDepartmentEntries(firstResult, sizeNo));
            float nrOfPages = (float) SubdepartmentDepartment.countSubdepartmentDepartments() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("subdepartmentdepartments", SubdepartmentDepartment.findAllSubdepartmentDepartments());
        }
        addDateTimeFormatPatterns(uiModel);
        return "crud/subdepartmentdepartments/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String SubdepartmentDepartmentController.update(@Valid SubdepartmentDepartment subdepartmentDepartment, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, subdepartmentDepartment);
            return "crud/subdepartmentdepartments/update";
        }
        uiModel.asMap().clear();
        subdepartmentDepartment.merge();
        return "redirect:/crud/subdepartmentdepartments/" + encodeUrlPathSegment(subdepartmentDepartment.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String SubdepartmentDepartmentController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, SubdepartmentDepartment.findSubdepartmentDepartment(id));
        return "crud/subdepartmentdepartments/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String SubdepartmentDepartmentController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        SubdepartmentDepartment subdepartmentDepartment = SubdepartmentDepartment.findSubdepartmentDepartment(id);
        subdepartmentDepartment.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/crud/subdepartmentdepartments";
    }
    
    void SubdepartmentDepartmentController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("subdepartmentDepartment_datecreated_date_format", "yyy-MM-dd hh:mm:ss a");
        uiModel.addAttribute("subdepartmentDepartment_datemodified_date_format", "yyy-MM-dd hh:mm:ss a");
    }
    
    void SubdepartmentDepartmentController.populateEditForm(Model uiModel, SubdepartmentDepartment subdepartmentDepartment) {
        uiModel.addAttribute("subdepartmentDepartment", subdepartmentDepartment);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("departments", Department.findAllDepartments());
        uiModel.addAttribute("subdepartments", Subdepartment.findAllSubdepartments());
    }
    
    String SubdepartmentDepartmentController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
