package com.lela.data.web;

import com.lela.data.domain.entity.CategoryDataSourceMap;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/categorydatasourcemaps")
@Controller
@RooWebScaffold(path = "crud/categorydatasourcemaps", formBackingObject = CategoryDataSourceMap.class)
public class CategoryDataSourceMapController {
}
