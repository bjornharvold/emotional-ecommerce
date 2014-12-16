package com.lela.data.web;

import com.lela.data.domain.entity.ProductDetailSection;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/productdetailsections")
@Controller
@RooWebScaffold(path = "crud/productdetailsections", formBackingObject = ProductDetailSection.class)
public class ProductDetailSectionController {
}
