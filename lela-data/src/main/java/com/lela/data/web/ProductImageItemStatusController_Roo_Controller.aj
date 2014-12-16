// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.web;

import com.lela.data.domain.entity.ProductImageItemStatus;
import com.lela.data.web.ProductImageItemStatusController;
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

privileged aspect ProductImageItemStatusController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String ProductImageItemStatusController.create(@Valid ProductImageItemStatus productImageItemStatus, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, productImageItemStatus);
            return "crud/productimageitemstatus/create";
        }
        uiModel.asMap().clear();
        productImageItemStatus.persist();
        return "redirect:/crud/productimageitemstatus/" + encodeUrlPathSegment(productImageItemStatus.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String ProductImageItemStatusController.createForm(Model uiModel) {
        populateEditForm(uiModel, new ProductImageItemStatus());
        return "crud/productimageitemstatus/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String ProductImageItemStatusController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("productimageitemstatus", ProductImageItemStatus.findProductImageItemStatus(id));
        uiModel.addAttribute("itemId", id);
        return "crud/productimageitemstatus/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String ProductImageItemStatusController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("productimageitemstatuses", ProductImageItemStatus.findProductImageItemStatusEntries(firstResult, sizeNo));
            float nrOfPages = (float) ProductImageItemStatus.countProductImageItemStatuses() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("productimageitemstatuses", ProductImageItemStatus.findAllProductImageItemStatuses());
        }
        addDateTimeFormatPatterns(uiModel);
        return "crud/productimageitemstatus/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String ProductImageItemStatusController.update(@Valid ProductImageItemStatus productImageItemStatus, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, productImageItemStatus);
            return "crud/productimageitemstatus/update";
        }
        uiModel.asMap().clear();
        productImageItemStatus.merge();
        return "redirect:/crud/productimageitemstatus/" + encodeUrlPathSegment(productImageItemStatus.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String ProductImageItemStatusController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ProductImageItemStatus.findProductImageItemStatus(id));
        return "crud/productimageitemstatus/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String ProductImageItemStatusController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ProductImageItemStatus productImageItemStatus = ProductImageItemStatus.findProductImageItemStatus(id);
        productImageItemStatus.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/crud/productimageitemstatus";
    }
    
    void ProductImageItemStatusController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("productImageItemStatus_datecreated_date_format", "yyy-MM-dd hh:mm:ss a");
        uiModel.addAttribute("productImageItemStatus_datemodified_date_format", "yyy-MM-dd hh:mm:ss a");
    }
    
    void ProductImageItemStatusController.populateEditForm(Model uiModel, ProductImageItemStatus productImageItemStatus) {
        uiModel.addAttribute("productImageItemStatus", productImageItemStatus);
        addDateTimeFormatPatterns(uiModel);
    }
    
    String ProductImageItemStatusController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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