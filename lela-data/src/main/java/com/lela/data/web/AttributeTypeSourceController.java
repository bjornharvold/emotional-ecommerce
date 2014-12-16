package com.lela.data.web;

import com.lela.data.domain.entity.AttributeTypeSource;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/attributetypesources")
@Controller
@RooWebScaffold(path = "crud/attributetypesources", formBackingObject = AttributeTypeSource.class)
public class AttributeTypeSourceController {
}
