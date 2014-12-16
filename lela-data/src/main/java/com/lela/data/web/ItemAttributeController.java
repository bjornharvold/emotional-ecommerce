package com.lela.data.web;

import com.lela.data.domain.entity.ItemAttribute;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/crud/itemattributes")
@Controller
@RooWebScaffold(path = "crud/itemattributes", formBackingObject = ItemAttribute.class)
public class ItemAttributeController {

    @RequestMapping(value = "item/{id}", produces = "text/html")
    public String listByItem(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("itemattributes", ItemAttribute.findItemAttributeEntriesByItem(id, firstResult, sizeNo));
            float nrOfPages = (float) ItemAttribute.countItemAttributesByItem(id) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("itemattributes", ItemAttribute.findAllItemAttributes());
        }
        addDateTimeFormatPatterns(uiModel);
        return "crud/itemattributes/list";
    }
}
