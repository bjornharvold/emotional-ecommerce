package com.lela.data.web;

import com.lela.data.domain.entity.CategoryCategoryGroup;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/categorycategorygroups")
@Controller
@RooWebScaffold(path = "crud/categorycategorygroups", formBackingObject = CategoryCategoryGroup.class)
public class CategoryCategoryGroupController {
}
