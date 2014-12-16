package com.lela.data.web;

import com.lela.data.domain.document.ItemAttributes;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/crud/itemattributeses")
@Controller
@RooWebScaffold(path = "crud/itemattributeses", formBackingObject = ItemAttributes.class)
public class ItemAttributesController {

    //@Autowired
    //ItemAttributesRepository repository;

    @RequestMapping(value = "/{id}", produces = "text/html")
    public String listByItem(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        /*if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("itemattributes", repository.findByItemId(id).getItemAttributeMap().values());
            ItemAttributes itemAttributes = repository.findByItemId(id);
            float nrOfPages = (float) itemAttributes.getItemAttributeMap().size() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("itemattributes", repository.findByItemId(id).getItemAttributeMap().values());
        }
        return "itemattributeses/list";
        */
        return "";
    }
}
