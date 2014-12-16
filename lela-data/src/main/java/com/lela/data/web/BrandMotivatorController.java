package com.lela.data.web;

import com.lela.data.domain.entity.BrandMotivator;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/brandmotivators")
@Controller
@RooWebScaffold(path = "crud/brandmotivators", formBackingObject = BrandMotivator.class)
public class BrandMotivatorController {
}
