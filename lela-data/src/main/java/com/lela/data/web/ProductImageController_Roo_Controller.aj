// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.web;

import com.lela.data.domain.entity.ImageSource;
import com.lela.data.domain.entity.ProductImage;
import com.lela.data.web.ProductImageController;
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

privileged aspect ProductImageController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String ProductImageController.create(@Valid ProductImage productImage, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, productImage);
            return "crud/productimages/create";
        }
        uiModel.asMap().clear();
        productImage.persist();
        return "redirect:/crud/productimages/" + encodeUrlPathSegment(productImage.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String ProductImageController.createForm(Model uiModel) {
        populateEditForm(uiModel, new ProductImage());
        return "crud/productimages/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String ProductImageController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("productimage", ProductImage.findProductImage(id));
        uiModel.addAttribute("itemId", id);
        return "crud/productimages/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String ProductImageController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("productimages", ProductImage.findProductImageEntries(firstResult, sizeNo));
            float nrOfPages = (float) ProductImage.countProductImages() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("productimages", ProductImage.findAllProductImages());
        }
        addDateTimeFormatPatterns(uiModel);
        return "crud/productimages/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String ProductImageController.update(@Valid ProductImage productImage, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, productImage);
            return "crud/productimages/update";
        }
        uiModel.asMap().clear();
        productImage.merge();
        return "redirect:/crud/productimages/" + encodeUrlPathSegment(productImage.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String ProductImageController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ProductImage.findProductImage(id));
        return "crud/productimages/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String ProductImageController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ProductImage productImage = ProductImage.findProductImage(id);
        productImage.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/crud/productimages";
    }
    
    void ProductImageController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("productImage_datecreated_date_format", "yyy-MM-dd hh:mm:ss a");
        uiModel.addAttribute("productImage_datemodified_date_format", "yyy-MM-dd hh:mm:ss a");
    }
    
    void ProductImageController.populateEditForm(Model uiModel, ProductImage productImage) {
        uiModel.addAttribute("productImage", productImage);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("imagesources", ImageSource.findAllImageSources());
    }
    
    String ProductImageController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
