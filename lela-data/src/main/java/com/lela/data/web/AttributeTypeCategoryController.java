package com.lela.data.web;

import com.lela.data.domain.entity.AttributeTypeCategory;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/attributetypecategorys")
@Controller
@RooWebScaffold(path = "crud/attributetypecategorys", formBackingObject = AttributeTypeCategory.class)
public class AttributeTypeCategoryController {
}
