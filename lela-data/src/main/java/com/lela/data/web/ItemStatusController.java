package com.lela.data.web;

import com.lela.data.domain.entity.ItemStatus;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/itemstatus")
@Controller
@RooWebScaffold(path = "crud/itemstatus", formBackingObject = ItemStatus.class)
public class ItemStatusController {
}
