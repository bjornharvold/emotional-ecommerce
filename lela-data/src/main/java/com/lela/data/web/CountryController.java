package com.lela.data.web;

import com.lela.data.domain.entity.Country;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/countrys")
@Controller
@RooWebScaffold(path = "crud/countrys", formBackingObject = Country.class)
public class CountryController {
}
