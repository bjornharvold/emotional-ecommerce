// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.web;

import com.lela.data.domain.entity.MultiValuedType;
import com.lela.data.web.MultiValuedTypeController;
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

privileged aspect MultiValuedTypeController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String MultiValuedTypeController.create(@Valid MultiValuedType multiValuedType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, multiValuedType);
            return "crud/multivaluedtypes/create";
        }
        uiModel.asMap().clear();
        multiValuedType.persist();
        return "redirect:/crud/multivaluedtypes/" + encodeUrlPathSegment(multiValuedType.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String MultiValuedTypeController.createForm(Model uiModel) {
        populateEditForm(uiModel, new MultiValuedType());
        return "crud/multivaluedtypes/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String MultiValuedTypeController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("multivaluedtype", MultiValuedType.findMultiValuedType(id));
        uiModel.addAttribute("itemId", id);
        return "crud/multivaluedtypes/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String MultiValuedTypeController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("multivaluedtypes", MultiValuedType.findMultiValuedTypeEntries(firstResult, sizeNo));
            float nrOfPages = (float) MultiValuedType.countMultiValuedTypes() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("multivaluedtypes", MultiValuedType.findAllMultiValuedTypes());
        }
        addDateTimeFormatPatterns(uiModel);
        return "crud/multivaluedtypes/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String MultiValuedTypeController.update(@Valid MultiValuedType multiValuedType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, multiValuedType);
            return "crud/multivaluedtypes/update";
        }
        uiModel.asMap().clear();
        multiValuedType.merge();
        return "redirect:/crud/multivaluedtypes/" + encodeUrlPathSegment(multiValuedType.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String MultiValuedTypeController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, MultiValuedType.findMultiValuedType(id));
        return "crud/multivaluedtypes/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String MultiValuedTypeController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        MultiValuedType multiValuedType = MultiValuedType.findMultiValuedType(id);
        multiValuedType.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/crud/multivaluedtypes";
    }
    
    void MultiValuedTypeController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("multiValuedType_datecreated_date_format", "yyy-MM-dd hh:mm:ss a");
        uiModel.addAttribute("multiValuedType_datemodified_date_format", "yyy-MM-dd hh:mm:ss a");
    }
    
    void MultiValuedTypeController.populateEditForm(Model uiModel, MultiValuedType multiValuedType) {
        uiModel.addAttribute("multiValuedType", multiValuedType);
        addDateTimeFormatPatterns(uiModel);
    }
    
    String MultiValuedTypeController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
