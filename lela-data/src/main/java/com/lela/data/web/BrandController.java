package com.lela.data.web;

import com.lela.data.domain.entity.Brand;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/brands")
@Controller
@RooWebScaffold(path = "crud/brands", formBackingObject = Brand.class)
@RooWebFinder
public class BrandController {
}
