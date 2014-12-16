package com.lela.data.web;

import com.lela.data.domain.entity.ValidationType;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/validationtypes")
@Controller
@RooWebScaffold(path = "crud/validationtypes", formBackingObject = ValidationType.class)
public class ValidationTypeController {
}
