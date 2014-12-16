package com.lela.data.web;

import com.lela.data.domain.entity.ManufacturerName;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/manufacturernames")
@Controller
@RooWebScaffold(path = "crud/manufacturernames", formBackingObject = ManufacturerName.class)
public class ManufacturerNameController {
}
