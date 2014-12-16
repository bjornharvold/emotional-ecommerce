// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.web;

import com.lela.data.domain.entity.Item;
import com.lela.data.domain.entity.Motivator;
import com.lela.data.domain.entity.ProductMotivatorError;
import com.lela.data.web.ProductMotivatorErrorController;
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

privileged aspect ProductMotivatorErrorController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String ProductMotivatorErrorController.create(@Valid ProductMotivatorError productMotivatorError, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, productMotivatorError);
            return "crud/productmotivatorerrors/create";
        }
        uiModel.asMap().clear();
        productMotivatorError.persist();
        return "redirect:/crud/productmotivatorerrors/" + encodeUrlPathSegment(productMotivatorError.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String ProductMotivatorErrorController.createForm(Model uiModel) {
        populateEditForm(uiModel, new ProductMotivatorError());
        return "crud/productmotivatorerrors/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String ProductMotivatorErrorController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("productmotivatorerror", ProductMotivatorError.findProductMotivatorError(id));
        uiModel.addAttribute("itemId", id);
        return "crud/productmotivatorerrors/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String ProductMotivatorErrorController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("productmotivatorerrors", ProductMotivatorError.findProductMotivatorErrorEntries(firstResult, sizeNo));
            float nrOfPages = (float) ProductMotivatorError.countProductMotivatorErrors() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("productmotivatorerrors", ProductMotivatorError.findAllProductMotivatorErrors());
        }
        addDateTimeFormatPatterns(uiModel);
        return "crud/productmotivatorerrors/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String ProductMotivatorErrorController.update(@Valid ProductMotivatorError productMotivatorError, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, productMotivatorError);
            return "crud/productmotivatorerrors/update";
        }
        uiModel.asMap().clear();
        productMotivatorError.merge();
        return "redirect:/crud/productmotivatorerrors/" + encodeUrlPathSegment(productMotivatorError.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String ProductMotivatorErrorController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ProductMotivatorError.findProductMotivatorError(id));
        return "crud/productmotivatorerrors/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String ProductMotivatorErrorController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ProductMotivatorError productMotivatorError = ProductMotivatorError.findProductMotivatorError(id);
        productMotivatorError.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/crud/productmotivatorerrors";
    }
    
    void ProductMotivatorErrorController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("productMotivatorError_datecreated_date_format", "yyy-MM-dd hh:mm:ss a");
        uiModel.addAttribute("productMotivatorError_datemodified_date_format", "yyy-MM-dd hh:mm:ss a");
    }
    
    void ProductMotivatorErrorController.populateEditForm(Model uiModel, ProductMotivatorError productMotivatorError) {
        uiModel.addAttribute("productMotivatorError", productMotivatorError);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("items", Item.findAllItems());
        uiModel.addAttribute("motivators", Motivator.findAllMotivators());
    }
    
    String ProductMotivatorErrorController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
