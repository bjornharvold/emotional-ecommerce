// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.web;

import com.lela.data.domain.entity.AttributeTypeSource;
import com.lela.data.web.AttributeTypeSourceController;
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

privileged aspect AttributeTypeSourceController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String AttributeTypeSourceController.create(@Valid AttributeTypeSource attributeTypeSource, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, attributeTypeSource);
            return "crud/attributetypesources/create";
        }
        uiModel.asMap().clear();
        attributeTypeSource.persist();
        return "redirect:/crud/attributetypesources/" + encodeUrlPathSegment(attributeTypeSource.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String AttributeTypeSourceController.createForm(Model uiModel) {
        populateEditForm(uiModel, new AttributeTypeSource());
        return "crud/attributetypesources/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String AttributeTypeSourceController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("attributetypesource", AttributeTypeSource.findAttributeTypeSource(id));
        uiModel.addAttribute("itemId", id);
        return "crud/attributetypesources/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String AttributeTypeSourceController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("attributetypesources", AttributeTypeSource.findAttributeTypeSourceEntries(firstResult, sizeNo));
            float nrOfPages = (float) AttributeTypeSource.countAttributeTypeSources() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("attributetypesources", AttributeTypeSource.findAllAttributeTypeSources());
        }
        addDateTimeFormatPatterns(uiModel);
        return "crud/attributetypesources/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String AttributeTypeSourceController.update(@Valid AttributeTypeSource attributeTypeSource, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, attributeTypeSource);
            return "crud/attributetypesources/update";
        }
        uiModel.asMap().clear();
        attributeTypeSource.merge();
        return "redirect:/crud/attributetypesources/" + encodeUrlPathSegment(attributeTypeSource.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String AttributeTypeSourceController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, AttributeTypeSource.findAttributeTypeSource(id));
        return "crud/attributetypesources/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String AttributeTypeSourceController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        AttributeTypeSource attributeTypeSource = AttributeTypeSource.findAttributeTypeSource(id);
        attributeTypeSource.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/crud/attributetypesources";
    }
    
    void AttributeTypeSourceController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("attributeTypeSource_datecreated_date_format", "yyy-MM-dd hh:mm:ss a");
        uiModel.addAttribute("attributeTypeSource_datemodified_date_format", "yyy-MM-dd hh:mm:ss a");
    }
    
    void AttributeTypeSourceController.populateEditForm(Model uiModel, AttributeTypeSource attributeTypeSource) {
        uiModel.addAttribute("attributeTypeSource", attributeTypeSource);
        addDateTimeFormatPatterns(uiModel);
    }
    
    String AttributeTypeSourceController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
