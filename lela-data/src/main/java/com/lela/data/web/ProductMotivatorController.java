package com.lela.data.web;

import com.lela.data.domain.entity.ProductMotivator;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/productmotivators")
@Controller
@RooWebScaffold(path = "crud/productmotivators", formBackingObject = ProductMotivator.class)
public class ProductMotivatorController {
}
