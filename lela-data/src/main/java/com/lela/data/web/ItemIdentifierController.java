package com.lela.data.web;

import com.lela.data.domain.entity.ItemIdentifier;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/itemidentifiers")
@Controller
@RooWebScaffold(path = "crud/itemidentifiers", formBackingObject = ItemIdentifier.class)
public class ItemIdentifierController {
}
