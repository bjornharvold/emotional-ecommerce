package com.lela.data.web;

import com.lela.data.domain.entity.BrandCategory;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/brandcategorys")
@Controller
@RooWebScaffold(path = "crud/brandcategorys", formBackingObject = BrandCategory.class)
public class BrandCategoryController {
}