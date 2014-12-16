package com.lela.data.web;

import com.lela.data.domain.entity.CategorySubdepartment;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/categorysubdepartments")
@Controller
@RooWebScaffold(path = "crud/categorysubdepartments", formBackingObject = CategorySubdepartment.class)
public class CategorySubdepartmentController {
}
