package com.lela.data.web;

import com.lela.data.domain.entity.AttributeTypeHistory;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/attributetypehistorys")
@Controller
@RooWebScaffold(path = "crud/attributetypehistorys", formBackingObject = AttributeTypeHistory.class)
public class AttributeTypeHistoryController {
}
