// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.web;

import com.lela.data.domain.entity.Series;
import com.lela.data.domain.entity.SeriesType;
import com.lela.data.web.SeriesController;
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

privileged aspect SeriesController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String SeriesController.create(@Valid Series series, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, series);
            return "crud/series/create";
        }
        uiModel.asMap().clear();
        series.persist();
        return "redirect:/crud/series/" + encodeUrlPathSegment(series.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String SeriesController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Series());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (SeriesType.countSeriesTypes() == 0) {
            dependencies.add(new String[] { "seriestype", "crud/seriestypes" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "crud/series/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String SeriesController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("series", Series.findSeries(id));
        uiModel.addAttribute("itemId", id);
        return "crud/series/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String SeriesController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("seriesitems", Series.findSeriesEntries(firstResult, sizeNo));
            float nrOfPages = (float) Series.countSeries() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("seriesitems", Series.findAllSeries());
        }
        addDateTimeFormatPatterns(uiModel);
        return "crud/series/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String SeriesController.update(@Valid Series series, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, series);
            return "crud/series/update";
        }
        uiModel.asMap().clear();
        series.merge();
        return "redirect:/crud/series/" + encodeUrlPathSegment(series.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String SeriesController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Series.findSeries(id));
        return "crud/series/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String SeriesController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Series series = Series.findSeries(id);
        series.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/crud/series";
    }
    
    void SeriesController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("series_datecreated_date_format", "yyy-MM-dd hh:mm:ss a");
        uiModel.addAttribute("series_datemodified_date_format", "yyy-MM-dd hh:mm:ss a");
    }
    
    void SeriesController.populateEditForm(Model uiModel, Series series) {
        uiModel.addAttribute("series", series);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("seriestypes", SeriesType.findAllSeriesTypes());
    }
    
    String SeriesController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
