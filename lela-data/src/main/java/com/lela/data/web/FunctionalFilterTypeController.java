package com.lela.data.web;

import com.lela.data.domain.entity.FunctionalFilterType;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/functionalfiltertypes")
@Controller
@RooWebScaffold(path = "crud/functionalfiltertypes", formBackingObject = FunctionalFilterType.class)
public class FunctionalFilterTypeController {
}
