package com.lela.data.web;

import com.lela.data.domain.entity.AttributeTypeNormalization;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/attributetypenormalizations")
@Controller
@RooWebScaffold(path = "crud/attributetypenormalizations", formBackingObject = AttributeTypeNormalization.class)
public class AttributeTypeNormalizationController {
}
