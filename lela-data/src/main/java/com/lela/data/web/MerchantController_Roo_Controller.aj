// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.web;

import com.lela.data.domain.entity.Merchant;
import com.lela.data.domain.entity.Network;
import com.lela.data.web.MerchantController;
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

privileged aspect MerchantController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String MerchantController.create(@Valid Merchant merchant, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, merchant);
            return "crud/merchants/create";
        }
        uiModel.asMap().clear();
        merchant.persist();
        return "redirect:/crud/merchants/" + encodeUrlPathSegment(merchant.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String MerchantController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Merchant());
        return "crud/merchants/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String MerchantController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("merchant", Merchant.findMerchant(id));
        uiModel.addAttribute("itemId", id);
        return "crud/merchants/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String MerchantController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("merchants", Merchant.findMerchantEntries(firstResult, sizeNo));
            float nrOfPages = (float) Merchant.countMerchants() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("merchants", Merchant.findAllMerchants());
        }
        addDateTimeFormatPatterns(uiModel);
        return "crud/merchants/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String MerchantController.update(@Valid Merchant merchant, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, merchant);
            return "crud/merchants/update";
        }
        uiModel.asMap().clear();
        merchant.merge();
        return "redirect:/crud/merchants/" + encodeUrlPathSegment(merchant.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String MerchantController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Merchant.findMerchant(id));
        return "crud/merchants/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String MerchantController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Merchant merchant = Merchant.findMerchant(id);
        merchant.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/crud/merchants";
    }
    
    void MerchantController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("merchant_datecreated_date_format", "yyy-MM-dd hh:mm:ss a");
        uiModel.addAttribute("merchant_datemodified_date_format", "yyy-MM-dd hh:mm:ss a");
    }
    
    void MerchantController.populateEditForm(Model uiModel, Merchant merchant) {
        uiModel.addAttribute("merchant", merchant);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("networks", Network.findAllNetworks());
    }
    
    String MerchantController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
