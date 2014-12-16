package com.lela.data.web;

import com.lela.data.domain.entity.IdentifierType;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/identifiertypes")
@Controller
@RooWebScaffold(path = "crud/identifiertypes", formBackingObject = IdentifierType.class)
public class IdentifierTypeController {
}
