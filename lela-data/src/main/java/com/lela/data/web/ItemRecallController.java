package com.lela.data.web;

import com.lela.data.domain.entity.ItemRecall;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/itemrecalls")
@Controller
@RooWebScaffold(path = "crud/itemrecalls", formBackingObject = ItemRecall.class)
public class ItemRecallController {
}
