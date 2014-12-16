package com.lela.data.web;

import com.lela.data.domain.entity.ZipCode;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/zipcodes")
@Controller
@RooWebScaffold(path = "crud/zipcodes", formBackingObject = ZipCode.class)
public class ZipCodeController {
}
