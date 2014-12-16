package com.lela.data.web;

import com.lela.data.domain.entity.FunctionalFilterAnswerValue;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/functionalfilteranswervalues")
@Controller
@RooWebScaffold(path = "crud/functionalfilteranswervalues", formBackingObject = FunctionalFilterAnswerValue.class)
public class FunctionalFilterAnswerValueController {
}
