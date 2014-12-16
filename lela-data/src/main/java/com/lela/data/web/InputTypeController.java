package com.lela.data.web;

import com.lela.data.domain.entity.InputType;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/inputtypes")
@Controller
@RooWebScaffold(path = "crud/inputtypes", formBackingObject = InputType.class)
public class InputTypeController {
}
