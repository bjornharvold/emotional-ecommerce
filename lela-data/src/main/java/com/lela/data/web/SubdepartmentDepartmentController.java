package com.lela.data.web;

import com.lela.data.domain.entity.SubdepartmentDepartment;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/subdepartmentdepartments")
@Controller
@RooWebScaffold(path = "crud/subdepartmentdepartments", formBackingObject = SubdepartmentDepartment.class)
public class SubdepartmentDepartmentController {
}
