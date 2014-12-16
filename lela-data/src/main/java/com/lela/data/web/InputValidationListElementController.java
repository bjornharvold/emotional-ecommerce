package com.lela.data.web;

import com.lela.data.domain.entity.InputValidationListElement;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/inputvalidationlistelements")
@Controller
@RooWebScaffold(path = "crud/inputvalidationlistelements", formBackingObject = InputValidationListElement.class)
public class InputValidationListElementController {
}
