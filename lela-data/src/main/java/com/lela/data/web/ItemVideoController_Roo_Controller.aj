// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.web;

import com.lela.data.domain.entity.Item;
import com.lela.data.domain.entity.ItemVideo;
import com.lela.data.domain.entity.ReviewStatus;
import com.lela.data.domain.entity.VideoType;
import com.lela.data.web.ItemVideoController;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
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

privileged aspect ItemVideoController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String ItemVideoController.create(@Valid ItemVideo itemVideo, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, itemVideo);
            return "crud/itemvideos/create";
        }
        uiModel.asMap().clear();
        itemVideo.persist();
        return "redirect:/crud/itemvideos/" + encodeUrlPathSegment(itemVideo.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String ItemVideoController.createForm(Model uiModel) {
        populateEditForm(uiModel, new ItemVideo());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (VideoType.countVideoTypes() == 0) {
            dependencies.add(new String[] { "videotype", "videotypes" });
        }
        if (ReviewStatus.countReviewStatuses() == 0) {
            dependencies.add(new String[] { "reviewstatus", "crud/reviewstatus" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "crud/itemvideos/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String ItemVideoController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("itemvideo", ItemVideo.findItemVideo(id));
        uiModel.addAttribute("itemId", id);
        return "crud/itemvideos/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String ItemVideoController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("itemvideos", ItemVideo.findItemVideoEntries(firstResult, sizeNo));
            float nrOfPages = (float) ItemVideo.countItemVideos() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("itemvideos", ItemVideo.findAllItemVideos());
        }
        addDateTimeFormatPatterns(uiModel);
        return "crud/itemvideos/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String ItemVideoController.update(@Valid ItemVideo itemVideo, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, itemVideo);
            return "crud/itemvideos/update";
        }
        uiModel.asMap().clear();
        itemVideo.merge();
        return "redirect:/crud/itemvideos/" + encodeUrlPathSegment(itemVideo.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String ItemVideoController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ItemVideo.findItemVideo(id));
        return "crud/itemvideos/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String ItemVideoController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ItemVideo itemVideo = ItemVideo.findItemVideo(id);
        itemVideo.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/crud/itemvideos";
    }
    
    void ItemVideoController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("itemVideo_datecreated_date_format", "yyy-MM-dd hh:mm:ss a");
        uiModel.addAttribute("itemVideo_datemodified_date_format", "yyy-MM-dd hh:mm:ss a");
    }
    
    void ItemVideoController.populateEditForm(Model uiModel, ItemVideo itemVideo) {
        uiModel.addAttribute("itemVideo", itemVideo);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("items", Item.findAllItems());
        uiModel.addAttribute("reviewstatuses", ReviewStatus.findAllReviewStatuses());
        uiModel.addAttribute("videotypes", VideoType.findAllVideoTypes());
    }
    
    String ItemVideoController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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