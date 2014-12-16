package com.lela.data.web;

import com.lela.data.domain.entity.Department;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/departments")
@Controller
@RooWebScaffold(path = "crud/departments", formBackingObject = Department.class)
public class DepartmentController {
}
