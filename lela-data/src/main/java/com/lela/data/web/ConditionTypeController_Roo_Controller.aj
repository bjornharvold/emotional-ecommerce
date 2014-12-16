// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.web;

import com.lela.data.domain.entity.ConditionType;
import com.lela.data.web.ConditionTypeController;
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

privileged aspect ConditionTypeController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String ConditionTypeController.create(@Valid ConditionType conditionType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, conditionType);
            return "crud/conditiontypes/create";
        }
        uiModel.asMap().clear();
        conditionType.persist();
        return "redirect:/crud/conditiontypes/" + encodeUrlPathSegment(conditionType.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String ConditionTypeController.createForm(Model uiModel) {
        populateEditForm(uiModel, new ConditionType());
        return "crud/conditiontypes/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String ConditionTypeController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("conditiontype", ConditionType.findConditionType(id));
        uiModel.addAttribute("itemId", id);
        return "crud/conditiontypes/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String ConditionTypeController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("conditiontypes", ConditionType.findConditionTypeEntries(firstResult, sizeNo));
            float nrOfPages = (float) ConditionType.countConditionTypes() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("conditiontypes", ConditionType.findAllConditionTypes());
        }
        addDateTimeFormatPatterns(uiModel);
        return "crud/conditiontypes/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String ConditionTypeController.update(@Valid ConditionType conditionType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, conditionType);
            return "crud/conditiontypes/update";
        }
        uiModel.asMap().clear();
        conditionType.merge();
        return "redirect:/crud/conditiontypes/" + encodeUrlPathSegment(conditionType.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String ConditionTypeController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ConditionType.findConditionType(id));
        return "crud/conditiontypes/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String ConditionTypeController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ConditionType conditionType = ConditionType.findConditionType(id);
        conditionType.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/crud/conditiontypes";
    }
    
    void ConditionTypeController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("conditionType_datecreated_date_format", "yyy-MM-dd hh:mm:ss a");
        uiModel.addAttribute("conditionType_datemodified_date_format", "yyy-MM-dd hh:mm:ss a");
    }
    
    void ConditionTypeController.populateEditForm(Model uiModel, ConditionType conditionType) {
        uiModel.addAttribute("conditionType", conditionType);
        addDateTimeFormatPatterns(uiModel);
    }
    
    String ConditionTypeController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
