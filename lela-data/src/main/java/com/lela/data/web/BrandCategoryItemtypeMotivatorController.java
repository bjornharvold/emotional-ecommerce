package com.lela.data.web;

import com.lela.data.domain.entity.BrandCategoryItemtypeMotivator;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/brandcategoryitemtypemotivators")
@Controller
@RooWebScaffold(path = "crud/brandcategoryitemtypemotivators", formBackingObject = BrandCategoryItemtypeMotivator.class)
public class BrandCategoryItemtypeMotivatorController {
}
