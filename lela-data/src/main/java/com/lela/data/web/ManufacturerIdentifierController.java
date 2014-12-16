package com.lela.data.web;

import com.lela.data.domain.entity.ManufacturerIdentifier;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/manufactureridentifiers")
@Controller
@RooWebScaffold(path = "crud/manufactureridentifiers", formBackingObject = ManufacturerIdentifier.class)
public class ManufacturerIdentifierController {
}
