package com.lela.data.web;

import com.lela.data.domain.entity.AttributeType;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/attributetypes")
@Controller
@RooWebScaffold(path = "crud/attributetypes", formBackingObject = AttributeType.class)
public class AttributeTypeController {
}
