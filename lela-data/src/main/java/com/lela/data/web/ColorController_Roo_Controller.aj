// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.web;

import com.lela.data.domain.entity.Color;
import com.lela.data.web.ColorController;
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

privileged aspect ColorController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String ColorController.create(@Valid Color color, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, color);
            return "crud/colors/create";
        }
        uiModel.asMap().clear();
        color.persist();
        return "redirect:/crud/colors/" + encodeUrlPathSegment(color.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String ColorController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Color());
        return "crud/colors/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String ColorController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("color", Color.findColor(id));
        uiModel.addAttribute("itemId", id);
        return "crud/colors/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String ColorController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("colors", Color.findColorEntries(firstResult, sizeNo));
            float nrOfPages = (float) Color.countColors() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("colors", Color.findAllColors());
        }
        addDateTimeFormatPatterns(uiModel);
        return "crud/colors/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String ColorController.update(@Valid Color color, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, color);
            return "crud/colors/update";
        }
        uiModel.asMap().clear();
        color.merge();
        return "redirect:/crud/colors/" + encodeUrlPathSegment(color.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String ColorController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Color.findColor(id));
        return "crud/colors/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String ColorController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Color color = Color.findColor(id);
        color.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/crud/colors";
    }
    
    void ColorController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("color_datecreated_date_format", "yyy-MM-dd hh:mm:ss a");
        uiModel.addAttribute("color_datemodified_date_format", "yyy-MM-dd hh:mm:ss a");
    }
    
    void ColorController.populateEditForm(Model uiModel, Color color) {
        uiModel.addAttribute("color", color);
        addDateTimeFormatPatterns(uiModel);
    }
    
    String ColorController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
