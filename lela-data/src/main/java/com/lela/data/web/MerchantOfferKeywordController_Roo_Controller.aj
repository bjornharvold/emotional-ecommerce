// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.web;

import com.lela.data.domain.entity.ActionType;
import com.lela.data.domain.entity.Category;
import com.lela.data.domain.entity.MerchantOfferKeyword;
import com.lela.data.web.MerchantOfferKeywordController;
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

privileged aspect MerchantOfferKeywordController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String MerchantOfferKeywordController.create(@Valid MerchantOfferKeyword merchantOfferKeyword, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, merchantOfferKeyword);
            return "crud/merchantofferkeywords/create";
        }
        uiModel.asMap().clear();
        merchantOfferKeyword.persist();
        return "redirect:/crud/merchantofferkeywords/" + encodeUrlPathSegment(merchantOfferKeyword.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String MerchantOfferKeywordController.createForm(Model uiModel) {
        populateEditForm(uiModel, new MerchantOfferKeyword());
        return "crud/merchantofferkeywords/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String MerchantOfferKeywordController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("merchantofferkeyword", MerchantOfferKeyword.findMerchantOfferKeyword(id));
        uiModel.addAttribute("itemId", id);
        return "crud/merchantofferkeywords/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String MerchantOfferKeywordController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("merchantofferkeywords", MerchantOfferKeyword.findMerchantOfferKeywordEntries(firstResult, sizeNo));
            float nrOfPages = (float) MerchantOfferKeyword.countMerchantOfferKeywords() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("merchantofferkeywords", MerchantOfferKeyword.findAllMerchantOfferKeywords());
        }
        addDateTimeFormatPatterns(uiModel);
        return "crud/merchantofferkeywords/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String MerchantOfferKeywordController.update(@Valid MerchantOfferKeyword merchantOfferKeyword, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, merchantOfferKeyword);
            return "crud/merchantofferkeywords/update";
        }
        uiModel.asMap().clear();
        merchantOfferKeyword.merge();
        return "redirect:/crud/merchantofferkeywords/" + encodeUrlPathSegment(merchantOfferKeyword.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String MerchantOfferKeywordController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, MerchantOfferKeyword.findMerchantOfferKeyword(id));
        return "crud/merchantofferkeywords/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String MerchantOfferKeywordController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        MerchantOfferKeyword merchantOfferKeyword = MerchantOfferKeyword.findMerchantOfferKeyword(id);
        merchantOfferKeyword.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/crud/merchantofferkeywords";
    }
    
    void MerchantOfferKeywordController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("merchantOfferKeyword_datecreated_date_format", "yyy-MM-dd hh:mm:ss a");
        uiModel.addAttribute("merchantOfferKeyword_datemodified_date_format", "yyy-MM-dd hh:mm:ss a");
    }
    
    void MerchantOfferKeywordController.populateEditForm(Model uiModel, MerchantOfferKeyword merchantOfferKeyword) {
        uiModel.addAttribute("merchantOfferKeyword", merchantOfferKeyword);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("actiontypes", ActionType.findAllActionTypes());
        uiModel.addAttribute("categorys", Category.findAllCategorys());
    }
    
    String MerchantOfferKeywordController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
