// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.web;

import com.lela.data.domain.entity.ImageSourceType;
import com.lela.data.web.ImageSourceTypeController;
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

privileged aspect ImageSourceTypeController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String ImageSourceTypeController.create(@Valid ImageSourceType imageSourceType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, imageSourceType);
            return "crud/imagesourcetypes/create";
        }
        uiModel.asMap().clear();
        imageSourceType.persist();
        return "redirect:/crud/imagesourcetypes/" + encodeUrlPathSegment(imageSourceType.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String ImageSourceTypeController.createForm(Model uiModel) {
        populateEditForm(uiModel, new ImageSourceType());
        return "crud/imagesourcetypes/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String ImageSourceTypeController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("imagesourcetype", ImageSourceType.findImageSourceType(id));
        uiModel.addAttribute("itemId", id);
        return "crud/imagesourcetypes/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String ImageSourceTypeController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("imagesourcetypes", ImageSourceType.findImageSourceTypeEntries(firstResult, sizeNo));
            float nrOfPages = (float) ImageSourceType.countImageSourceTypes() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("imagesourcetypes", ImageSourceType.findAllImageSourceTypes());
        }
        addDateTimeFormatPatterns(uiModel);
        return "crud/imagesourcetypes/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String ImageSourceTypeController.update(@Valid ImageSourceType imageSourceType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, imageSourceType);
            return "crud/imagesourcetypes/update";
        }
        uiModel.asMap().clear();
        imageSourceType.merge();
        return "redirect:/crud/imagesourcetypes/" + encodeUrlPathSegment(imageSourceType.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String ImageSourceTypeController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ImageSourceType.findImageSourceType(id));
        return "crud/imagesourcetypes/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String ImageSourceTypeController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ImageSourceType imageSourceType = ImageSourceType.findImageSourceType(id);
        imageSourceType.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/crud/imagesourcetypes";
    }
    
    void ImageSourceTypeController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("imageSourceType_datecreated_date_format", "yyy-MM-dd hh:mm:ss a");
        uiModel.addAttribute("imageSourceType_datemodified_date_format", "yyy-MM-dd hh:mm:ss a");
    }
    
    void ImageSourceTypeController.populateEditForm(Model uiModel, ImageSourceType imageSourceType) {
        uiModel.addAttribute("imageSourceType", imageSourceType);
        addDateTimeFormatPatterns(uiModel);
    }
    
    String ImageSourceTypeController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
