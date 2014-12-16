package com.lela.data.web;

import com.lela.data.domain.entity.FunctionalFilter;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/functionalfilters")
@Controller
@RooWebScaffold(path = "crud/functionalfilters", formBackingObject = FunctionalFilter.class)
public class FunctionalFilterController {
}
