// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.web;

import com.lela.data.domain.entity.ImageSource;
import com.lela.data.domain.entity.ImageSourceType;
import com.lela.data.web.ImageSourceController;
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

privileged aspect ImageSourceController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String ImageSourceController.create(@Valid ImageSource imageSource, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, imageSource);
            return "crud/imagesources/create";
        }
        uiModel.asMap().clear();
        imageSource.persist();
        return "redirect:/crud/imagesources/" + encodeUrlPathSegment(imageSource.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String ImageSourceController.createForm(Model uiModel) {
        populateEditForm(uiModel, new ImageSource());
        return "crud/imagesources/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String ImageSourceController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("imagesource", ImageSource.findImageSource(id));
        uiModel.addAttribute("itemId", id);
        return "crud/imagesources/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String ImageSourceController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("imagesources", ImageSource.findImageSourceEntries(firstResult, sizeNo));
            float nrOfPages = (float) ImageSource.countImageSources() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("imagesources", ImageSource.findAllImageSources());
        }
        addDateTimeFormatPatterns(uiModel);
        return "crud/imagesources/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String ImageSourceController.update(@Valid ImageSource imageSource, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, imageSource);
            return "crud/imagesources/update";
        }
        uiModel.asMap().clear();
        imageSource.merge();
        return "redirect:/crud/imagesources/" + encodeUrlPathSegment(imageSource.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String ImageSourceController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ImageSource.findImageSource(id));
        return "crud/imagesources/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String ImageSourceController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ImageSource imageSource = ImageSource.findImageSource(id);
        imageSource.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/crud/imagesources";
    }
    
    void ImageSourceController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("imageSource_datecreated_date_format", "yyy-MM-dd hh:mm:ss a");
        uiModel.addAttribute("imageSource_datemodified_date_format", "yyy-MM-dd hh:mm:ss a");
    }
    
    void ImageSourceController.populateEditForm(Model uiModel, ImageSource imageSource) {
        uiModel.addAttribute("imageSource", imageSource);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("imagesourcetypes", ImageSourceType.findAllImageSourceTypes());
    }
    
    String ImageSourceController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
