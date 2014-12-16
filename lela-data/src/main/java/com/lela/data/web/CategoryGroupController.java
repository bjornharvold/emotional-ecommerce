package com.lela.data.web;

import com.lela.data.domain.entity.CategoryGroup;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/categorygroups")
@Controller
@RooWebScaffold(path = "crud/categorygroups", formBackingObject = CategoryGroup.class)
public class CategoryGroupController {
}
