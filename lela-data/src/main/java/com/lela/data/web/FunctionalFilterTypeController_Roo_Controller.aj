// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.web;

import com.lela.data.domain.entity.FunctionalFilter;
import com.lela.data.domain.entity.FunctionalFilterType;
import com.lela.data.web.FunctionalFilterTypeController;
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

privileged aspect FunctionalFilterTypeController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String FunctionalFilterTypeController.create(@Valid FunctionalFilterType functionalFilterType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, functionalFilterType);
            return "crud/functionalfiltertypes/create";
        }
        uiModel.asMap().clear();
        functionalFilterType.persist();
        return "redirect:/crud/functionalfiltertypes/" + encodeUrlPathSegment(functionalFilterType.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String FunctionalFilterTypeController.createForm(Model uiModel) {
        populateEditForm(uiModel, new FunctionalFilterType());
        return "crud/functionalfiltertypes/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String FunctionalFilterTypeController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("functionalfiltertype", FunctionalFilterType.findFunctionalFilterType(id));
        uiModel.addAttribute("itemId", id);
        return "crud/functionalfiltertypes/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String FunctionalFilterTypeController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("functionalfiltertypes", FunctionalFilterType.findFunctionalFilterTypeEntries(firstResult, sizeNo));
            float nrOfPages = (float) FunctionalFilterType.countFunctionalFilterTypes() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("functionalfiltertypes", FunctionalFilterType.findAllFunctionalFilterTypes());
        }
        addDateTimeFormatPatterns(uiModel);
        return "crud/functionalfiltertypes/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String FunctionalFilterTypeController.update(@Valid FunctionalFilterType functionalFilterType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, functionalFilterType);
            return "crud/functionalfiltertypes/update";
        }
        uiModel.asMap().clear();
        functionalFilterType.merge();
        return "redirect:/crud/functionalfiltertypes/" + encodeUrlPathSegment(functionalFilterType.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String FunctionalFilterTypeController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, FunctionalFilterType.findFunctionalFilterType(id));
        return "crud/functionalfiltertypes/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String FunctionalFilterTypeController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        FunctionalFilterType functionalFilterType = FunctionalFilterType.findFunctionalFilterType(id);
        functionalFilterType.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/crud/functionalfiltertypes";
    }
    
    void FunctionalFilterTypeController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("functionalFilterType_datecreated_date_format", "yyy-MM-dd hh:mm:ss a");
        uiModel.addAttribute("functionalFilterType_datemodified_date_format", "yyy-MM-dd hh:mm:ss a");
    }
    
    void FunctionalFilterTypeController.populateEditForm(Model uiModel, FunctionalFilterType functionalFilterType) {
        uiModel.addAttribute("functionalFilterType", functionalFilterType);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("functionalfilters", FunctionalFilter.findAllFunctionalFilters());
    }
    
    String FunctionalFilterTypeController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
