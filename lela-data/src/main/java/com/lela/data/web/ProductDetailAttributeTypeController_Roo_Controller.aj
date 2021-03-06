// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.web;

import com.lela.data.domain.entity.AttributeType;
import com.lela.data.domain.entity.ProductDetailAttributeType;
import com.lela.data.domain.entity.ProductDetailGroupAttribute;
import com.lela.data.web.ProductDetailAttributeTypeController;
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

privileged aspect ProductDetailAttributeTypeController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String ProductDetailAttributeTypeController.create(@Valid ProductDetailAttributeType productDetailAttributeType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, productDetailAttributeType);
            return "crud/productdetailattributetypes/create";
        }
        uiModel.asMap().clear();
        productDetailAttributeType.persist();
        return "redirect:/crud/productdetailattributetypes/" + encodeUrlPathSegment(productDetailAttributeType.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String ProductDetailAttributeTypeController.createForm(Model uiModel) {
        populateEditForm(uiModel, new ProductDetailAttributeType());
        return "crud/productdetailattributetypes/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String ProductDetailAttributeTypeController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("productdetailattributetype", ProductDetailAttributeType.findProductDetailAttributeType(id));
        uiModel.addAttribute("itemId", id);
        return "crud/productdetailattributetypes/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String ProductDetailAttributeTypeController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("productdetailattributetypes", ProductDetailAttributeType.findProductDetailAttributeTypeEntries(firstResult, sizeNo));
            float nrOfPages = (float) ProductDetailAttributeType.countProductDetailAttributeTypes() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("productdetailattributetypes", ProductDetailAttributeType.findAllProductDetailAttributeTypes());
        }
        addDateTimeFormatPatterns(uiModel);
        return "crud/productdetailattributetypes/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String ProductDetailAttributeTypeController.update(@Valid ProductDetailAttributeType productDetailAttributeType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, productDetailAttributeType);
            return "crud/productdetailattributetypes/update";
        }
        uiModel.asMap().clear();
        productDetailAttributeType.merge();
        return "redirect:/crud/productdetailattributetypes/" + encodeUrlPathSegment(productDetailAttributeType.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String ProductDetailAttributeTypeController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ProductDetailAttributeType.findProductDetailAttributeType(id));
        return "crud/productdetailattributetypes/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String ProductDetailAttributeTypeController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ProductDetailAttributeType productDetailAttributeType = ProductDetailAttributeType.findProductDetailAttributeType(id);
        productDetailAttributeType.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/crud/productdetailattributetypes";
    }
    
    void ProductDetailAttributeTypeController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("productDetailAttributeType_datecreated_date_format", "yyy-MM-dd hh:mm:ss a");
        uiModel.addAttribute("productDetailAttributeType_datemodified_date_format", "yyy-MM-dd hh:mm:ss a");
    }
    
    void ProductDetailAttributeTypeController.populateEditForm(Model uiModel, ProductDetailAttributeType productDetailAttributeType) {
        uiModel.addAttribute("productDetailAttributeType", productDetailAttributeType);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("attributetypes", AttributeType.findAllAttributeTypes());
        uiModel.addAttribute("productdetailgroupattributes", ProductDetailGroupAttribute.findAllProductDetailGroupAttributes());
    }
    
    String ProductDetailAttributeTypeController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
