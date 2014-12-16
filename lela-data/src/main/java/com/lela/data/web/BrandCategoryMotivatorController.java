package com.lela.data.web;

import com.lela.data.domain.entity.BrandCategoryMotivator;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/brandcategorymotivators")
@Controller
@RooWebScaffold(path = "crud/brandcategorymotivators", formBackingObject = BrandCategoryMotivator.class)
public class BrandCategoryMotivatorController {
}
