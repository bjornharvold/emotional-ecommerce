// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.web;

import com.lela.data.domain.entity.Item;
import com.lela.data.domain.entity.ItemSeries;
import com.lela.data.domain.entity.Series;
import com.lela.data.web.ItemSeriesController;
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

privileged aspect ItemSeriesController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String ItemSeriesController.create(@Valid ItemSeries itemSeries, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, itemSeries);
            return "crud/itemseries/create";
        }
        uiModel.asMap().clear();
        itemSeries.persist();
        return "redirect:/crud/itemseries/" + encodeUrlPathSegment(itemSeries.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String ItemSeriesController.createForm(Model uiModel) {
        populateEditForm(uiModel, new ItemSeries());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (Series.countSeries() == 0) {
            dependencies.add(new String[] { "series", "crud/series" });
        }
        if (Item.countItems() == 0) {
            dependencies.add(new String[] { "item", "items" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "crud/itemseries/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String ItemSeriesController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("itemseries", ItemSeries.findItemSeries(id));
        uiModel.addAttribute("itemId", id);
        return "crud/itemseries/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String ItemSeriesController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("itemserieses", ItemSeries.findItemSeriesEntries(firstResult, sizeNo));
            float nrOfPages = (float) ItemSeries.countItemSerieses() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("itemserieses", ItemSeries.findAllItemSerieses());
        }
        addDateTimeFormatPatterns(uiModel);
        return "crud/itemseries/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String ItemSeriesController.update(@Valid ItemSeries itemSeries, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, itemSeries);
            return "crud/itemseries/update";
        }
        uiModel.asMap().clear();
        itemSeries.merge();
        return "redirect:/crud/itemseries/" + encodeUrlPathSegment(itemSeries.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String ItemSeriesController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ItemSeries.findItemSeries(id));
        return "crud/itemseries/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String ItemSeriesController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ItemSeries itemSeries = ItemSeries.findItemSeries(id);
        itemSeries.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/crud/itemseries";
    }
    
    void ItemSeriesController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("itemSeries_datecreated_date_format", "yyy-MM-dd hh:mm:ss a");
        uiModel.addAttribute("itemSeries_datemodified_date_format", "yyy-MM-dd hh:mm:ss a");
    }
    
    void ItemSeriesController.populateEditForm(Model uiModel, ItemSeries itemSeries) {
        uiModel.addAttribute("itemSeries", itemSeries);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("items", Item.findAllItems());
        uiModel.addAttribute("seriesitems", Series.findAllSeries());
    }
    
    String ItemSeriesController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
