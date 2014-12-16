// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.web;

import com.lela.data.domain.entity.ActionType;
import com.lela.data.web.ActionTypeController;
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

privileged aspect ActionTypeController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String ActionTypeController.create(@Valid ActionType actionType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, actionType);
            return "crud/actiontypes/create";
        }
        uiModel.asMap().clear();
        actionType.persist();
        return "redirect:/crud/actiontypes/" + encodeUrlPathSegment(actionType.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String ActionTypeController.createForm(Model uiModel) {
        populateEditForm(uiModel, new ActionType());
        return "crud/actiontypes/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String ActionTypeController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("actiontype", ActionType.findActionType(id));
        uiModel.addAttribute("itemId", id);
        return "crud/actiontypes/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String ActionTypeController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("actiontypes", ActionType.findActionTypeEntries(firstResult, sizeNo));
            float nrOfPages = (float) ActionType.countActionTypes() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("actiontypes", ActionType.findAllActionTypes());
        }
        addDateTimeFormatPatterns(uiModel);
        return "crud/actiontypes/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String ActionTypeController.update(@Valid ActionType actionType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, actionType);
            return "crud/actiontypes/update";
        }
        uiModel.asMap().clear();
        actionType.merge();
        return "redirect:/crud/actiontypes/" + encodeUrlPathSegment(actionType.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String ActionTypeController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ActionType.findActionType(id));
        return "crud/actiontypes/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String ActionTypeController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ActionType actionType = ActionType.findActionType(id);
        actionType.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/crud/actiontypes";
    }
    
    void ActionTypeController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("actionType_datecreated_date_format", "yyy-MM-dd hh:mm:ss a");
        uiModel.addAttribute("actionType_datemodified_date_format", "yyy-MM-dd hh:mm:ss a");
    }
    
    void ActionTypeController.populateEditForm(Model uiModel, ActionType actionType) {
        uiModel.addAttribute("actionType", actionType);
        addDateTimeFormatPatterns(uiModel);
    }
    
    String ActionTypeController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
