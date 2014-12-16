package com.lela.data.web;

import com.lela.data.domain.entity.Manufacturer;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/manufacturers")
@Controller
@RooWebScaffold(path = "crud/manufacturers", formBackingObject = Manufacturer.class)
public class ManufacturerController {
}
