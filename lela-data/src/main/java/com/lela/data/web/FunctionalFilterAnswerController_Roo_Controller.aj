// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.web;

import com.lela.data.domain.entity.FunctionalFilter;
import com.lela.data.domain.entity.FunctionalFilterAnswer;
import com.lela.data.domain.entity.FunctionalFilterAnswerValue;
import com.lela.data.web.FunctionalFilterAnswerController;
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

privileged aspect FunctionalFilterAnswerController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String FunctionalFilterAnswerController.create(@Valid FunctionalFilterAnswer functionalFilterAnswer, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, functionalFilterAnswer);
            return "crud/functionalfilteranswers/create";
        }
        uiModel.asMap().clear();
        functionalFilterAnswer.persist();
        return "redirect:/crud/functionalfilteranswers/" + encodeUrlPathSegment(functionalFilterAnswer.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String FunctionalFilterAnswerController.createForm(Model uiModel) {
        populateEditForm(uiModel, new FunctionalFilterAnswer());
        return "crud/functionalfilteranswers/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String FunctionalFilterAnswerController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("functionalfilteranswer", FunctionalFilterAnswer.findFunctionalFilterAnswer(id));
        uiModel.addAttribute("itemId", id);
        return "crud/functionalfilteranswers/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String FunctionalFilterAnswerController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("functionalfilteranswers", FunctionalFilterAnswer.findFunctionalFilterAnswerEntries(firstResult, sizeNo));
            float nrOfPages = (float) FunctionalFilterAnswer.countFunctionalFilterAnswers() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("functionalfilteranswers", FunctionalFilterAnswer.findAllFunctionalFilterAnswers());
        }
        addDateTimeFormatPatterns(uiModel);
        return "crud/functionalfilteranswers/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String FunctionalFilterAnswerController.update(@Valid FunctionalFilterAnswer functionalFilterAnswer, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, functionalFilterAnswer);
            return "crud/functionalfilteranswers/update";
        }
        uiModel.asMap().clear();
        functionalFilterAnswer.merge();
        return "redirect:/crud/functionalfilteranswers/" + encodeUrlPathSegment(functionalFilterAnswer.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String FunctionalFilterAnswerController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, FunctionalFilterAnswer.findFunctionalFilterAnswer(id));
        return "crud/functionalfilteranswers/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String FunctionalFilterAnswerController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        FunctionalFilterAnswer functionalFilterAnswer = FunctionalFilterAnswer.findFunctionalFilterAnswer(id);
        functionalFilterAnswer.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/crud/functionalfilteranswers";
    }
    
    void FunctionalFilterAnswerController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("functionalFilterAnswer_datecreated_date_format", "yyy-MM-dd hh:mm:ss a");
        uiModel.addAttribute("functionalFilterAnswer_datemodified_date_format", "yyy-MM-dd hh:mm:ss a");
    }
    
    void FunctionalFilterAnswerController.populateEditForm(Model uiModel, FunctionalFilterAnswer functionalFilterAnswer) {
        uiModel.addAttribute("functionalFilterAnswer", functionalFilterAnswer);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("functionalfilters", FunctionalFilter.findAllFunctionalFilters());
        uiModel.addAttribute("functionalfilteranswervalues", FunctionalFilterAnswerValue.findAllFunctionalFilterAnswerValues());
    }
    
    String FunctionalFilterAnswerController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
