package com.lela.data.web;

import com.lela.data.domain.entity.ItemChanged;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/itemchangeds")
@Controller
@RooWebScaffold(path = "crud/itemchangeds", formBackingObject = ItemChanged.class)
public class ItemChangedController {
}
