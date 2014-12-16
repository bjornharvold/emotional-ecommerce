package com.lela.data.web;

import com.lela.data.domain.entity.Subdepartment;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/subdepartments")
@Controller
@RooWebScaffold(path = "crud/subdepartments", formBackingObject = Subdepartment.class)
public class SubdepartmentController {
}
