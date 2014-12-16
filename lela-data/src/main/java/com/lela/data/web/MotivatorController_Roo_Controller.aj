// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.web;

import com.lela.data.domain.entity.Category;
import com.lela.data.domain.entity.Motivator;
import com.lela.data.web.MotivatorController;
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

privileged aspect MotivatorController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String MotivatorController.create(@Valid Motivator motivator, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, motivator);
            return "crud/motivators/create";
        }
        uiModel.asMap().clear();
        motivator.persist();
        return "redirect:/crud/motivators/" + encodeUrlPathSegment(motivator.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String MotivatorController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Motivator());
        return "crud/motivators/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String MotivatorController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("motivator", Motivator.findMotivator(id));
        uiModel.addAttribute("itemId", id);
        return "crud/motivators/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String MotivatorController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("motivators", Motivator.findMotivatorEntries(firstResult, sizeNo));
            float nrOfPages = (float) Motivator.countMotivators() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("motivators", Motivator.findAllMotivators());
        }
        addDateTimeFormatPatterns(uiModel);
        return "crud/motivators/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String MotivatorController.update(@Valid Motivator motivator, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, motivator);
            return "crud/motivators/update";
        }
        uiModel.asMap().clear();
        motivator.merge();
        return "redirect:/crud/motivators/" + encodeUrlPathSegment(motivator.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String MotivatorController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Motivator.findMotivator(id));
        return "crud/motivators/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String MotivatorController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Motivator motivator = Motivator.findMotivator(id);
        motivator.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/crud/motivators";
    }
    
    void MotivatorController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("motivator_datecreated_date_format", "yyy-MM-dd hh:mm:ss a");
        uiModel.addAttribute("motivator_datemodified_date_format", "yyy-MM-dd hh:mm:ss a");
    }
    
    void MotivatorController.populateEditForm(Model uiModel, Motivator motivator) {
        uiModel.addAttribute("motivator", motivator);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("categorys", Category.findAllCategorys());
    }
    
    String MotivatorController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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