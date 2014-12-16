package com.lela.data.web;

import com.lela.data.domain.entity.AttributeTypeMotivator;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/attributetypemotivators")
@Controller
@RooWebScaffold(path = "crud/attributetypemotivators", formBackingObject = AttributeTypeMotivator.class)
public class AttributeTypeMotivatorController {
}
