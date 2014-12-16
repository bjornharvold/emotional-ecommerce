package com.lela.data.web;

import com.lela.data.domain.entity.ProductMotivatorError;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/productmotivatorerrors")
@Controller
@RooWebScaffold(path = "crud/productmotivatorerrors", formBackingObject = ProductMotivatorError.class)
public class ProductMotivatorErrorController {
}
