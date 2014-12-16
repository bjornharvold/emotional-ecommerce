// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.web;

import com.lela.data.domain.entity.AccessoryType;
import com.lela.data.domain.entity.AttributeType;
import com.lela.data.domain.entity.AttributeTypeSource;
import com.lela.data.domain.entity.CleanseRule;
import com.lela.data.domain.entity.ValidationType;
import com.lela.data.web.AttributeTypeController;
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

privileged aspect AttributeTypeController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String AttributeTypeController.create(@Valid AttributeType attributeType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, attributeType);
            return "crud/attributetypes/create";
        }
        uiModel.asMap().clear();
        attributeType.persist();
        return "redirect:/crud/attributetypes/" + encodeUrlPathSegment(attributeType.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String AttributeTypeController.createForm(Model uiModel) {
        populateEditForm(uiModel, new AttributeType());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (AccessoryType.countAccessoryTypes() == 0) {
            dependencies.add(new String[] { "accessorytype", "accessorytypes" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "crud/attributetypes/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String AttributeTypeController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("attributetype", AttributeType.findAttributeType(id));
        uiModel.addAttribute("itemId", id);
        return "crud/attributetypes/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String AttributeTypeController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("attributetypes", AttributeType.findAttributeTypeEntries(firstResult, sizeNo));
            float nrOfPages = (float) AttributeType.countAttributeTypes() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("attributetypes", AttributeType.findAllAttributeTypes());
        }
        addDateTimeFormatPatterns(uiModel);
        return "crud/attributetypes/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String AttributeTypeController.update(@Valid AttributeType attributeType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, attributeType);
            return "crud/attributetypes/update";
        }
        uiModel.asMap().clear();
        attributeType.merge();
        return "redirect:/crud/attributetypes/" + encodeUrlPathSegment(attributeType.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String AttributeTypeController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, AttributeType.findAttributeType(id));
        return "crud/attributetypes/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String AttributeTypeController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        AttributeType attributeType = AttributeType.findAttributeType(id);
        attributeType.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/crud/attributetypes";
    }
    
    void AttributeTypeController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("attributeType_datecreated_date_format", "yyy-MM-dd hh:mm:ss a");
        uiModel.addAttribute("attributeType_datemodified_date_format", "yyy-MM-dd hh:mm:ss a");
    }
    
    void AttributeTypeController.populateEditForm(Model uiModel, AttributeType attributeType) {
        uiModel.addAttribute("attributeType", attributeType);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("accessorytypes", AccessoryType.findAllAccessoryTypes());
        uiModel.addAttribute("attributetypesources", AttributeTypeSource.findAllAttributeTypeSources());
        uiModel.addAttribute("cleanserules", CleanseRule.findAllCleanseRules());
        uiModel.addAttribute("validationtypes", ValidationType.findAllValidationTypes());
    }
    
    String AttributeTypeController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
