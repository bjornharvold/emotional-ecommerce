package com.lela.data.web;

import com.lela.data.domain.entity.CategoryDataSource;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/categorydatasources")
@Controller
@RooWebScaffold(path = "crud/categorydatasources", formBackingObject = CategoryDataSource.class)
public class CategoryDataSourceController {
}
