// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.web;

import com.lela.data.domain.entity.MerchantSourceType;
import com.lela.data.web.MerchantSourceTypeController;
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

privileged aspect MerchantSourceTypeController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String MerchantSourceTypeController.create(@Valid MerchantSourceType merchantSourceType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, merchantSourceType);
            return "crud/merchantsourcetypes/create";
        }
        uiModel.asMap().clear();
        merchantSourceType.persist();
        return "redirect:/crud/merchantsourcetypes/" + encodeUrlPathSegment(merchantSourceType.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String MerchantSourceTypeController.createForm(Model uiModel) {
        populateEditForm(uiModel, new MerchantSourceType());
        return "crud/merchantsourcetypes/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String MerchantSourceTypeController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("merchantsourcetype", MerchantSourceType.findMerchantSourceType(id));
        uiModel.addAttribute("itemId", id);
        return "crud/merchantsourcetypes/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String MerchantSourceTypeController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("merchantsourcetypes", MerchantSourceType.findMerchantSourceTypeEntries(firstResult, sizeNo));
            float nrOfPages = (float) MerchantSourceType.countMerchantSourceTypes() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("merchantsourcetypes", MerchantSourceType.findAllMerchantSourceTypes());
        }
        addDateTimeFormatPatterns(uiModel);
        return "crud/merchantsourcetypes/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String MerchantSourceTypeController.update(@Valid MerchantSourceType merchantSourceType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, merchantSourceType);
            return "crud/merchantsourcetypes/update";
        }
        uiModel.asMap().clear();
        merchantSourceType.merge();
        return "redirect:/crud/merchantsourcetypes/" + encodeUrlPathSegment(merchantSourceType.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String MerchantSourceTypeController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, MerchantSourceType.findMerchantSourceType(id));
        return "crud/merchantsourcetypes/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String MerchantSourceTypeController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        MerchantSourceType merchantSourceType = MerchantSourceType.findMerchantSourceType(id);
        merchantSourceType.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/crud/merchantsourcetypes";
    }
    
    void MerchantSourceTypeController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("merchantSourceType_datecreated_date_format", "yyy-MM-dd hh:mm:ss a");
        uiModel.addAttribute("merchantSourceType_datemodified_date_format", "yyy-MM-dd hh:mm:ss a");
    }
    
    void MerchantSourceTypeController.populateEditForm(Model uiModel, MerchantSourceType merchantSourceType) {
        uiModel.addAttribute("merchantSourceType", merchantSourceType);
        addDateTimeFormatPatterns(uiModel);
    }
    
    String MerchantSourceTypeController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
