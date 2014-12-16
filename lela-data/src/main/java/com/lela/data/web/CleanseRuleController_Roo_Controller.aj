// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.web;

import com.lela.data.domain.entity.CleanseRule;
import com.lela.data.web.CleanseRuleController;
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

privileged aspect CleanseRuleController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String CleanseRuleController.create(@Valid CleanseRule cleanseRule, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, cleanseRule);
            return "crud/cleanserules/create";
        }
        uiModel.asMap().clear();
        cleanseRule.persist();
        return "redirect:/crud/cleanserules/" + encodeUrlPathSegment(cleanseRule.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String CleanseRuleController.createForm(Model uiModel) {
        populateEditForm(uiModel, new CleanseRule());
        return "crud/cleanserules/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String CleanseRuleController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("cleanserule", CleanseRule.findCleanseRule(id));
        uiModel.addAttribute("itemId", id);
        return "crud/cleanserules/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String CleanseRuleController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("cleanserules", CleanseRule.findCleanseRuleEntries(firstResult, sizeNo));
            float nrOfPages = (float) CleanseRule.countCleanseRules() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("cleanserules", CleanseRule.findAllCleanseRules());
        }
        addDateTimeFormatPatterns(uiModel);
        return "crud/cleanserules/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String CleanseRuleController.update(@Valid CleanseRule cleanseRule, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, cleanseRule);
            return "crud/cleanserules/update";
        }
        uiModel.asMap().clear();
        cleanseRule.merge();
        return "redirect:/crud/cleanserules/" + encodeUrlPathSegment(cleanseRule.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String CleanseRuleController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, CleanseRule.findCleanseRule(id));
        return "crud/cleanserules/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String CleanseRuleController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        CleanseRule cleanseRule = CleanseRule.findCleanseRule(id);
        cleanseRule.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/crud/cleanserules";
    }
    
    void CleanseRuleController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("cleanseRule_datecreated_date_format", "yyy-MM-dd hh:mm:ss a");
        uiModel.addAttribute("cleanseRule_datemodified_date_format", "yyy-MM-dd hh:mm:ss a");
    }
    
    void CleanseRuleController.populateEditForm(Model uiModel, CleanseRule cleanseRule) {
        uiModel.addAttribute("cleanseRule", cleanseRule);
        addDateTimeFormatPatterns(uiModel);
    }
    
    String CleanseRuleController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
