package com.lela.data.web;

import com.lela.data.domain.entity.ItemColor;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/itemcolors")
@Controller
@RooWebScaffold(path = "crud/itemcolors", formBackingObject = ItemColor.class)
public class ItemColorController {
}
