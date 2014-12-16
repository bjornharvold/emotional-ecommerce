package com.lela.data.web;

import com.lela.data.domain.entity.ProductDetailPart;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/productdetailparts")
@Controller
@RooWebScaffold(path = "crud/productdetailparts", formBackingObject = ProductDetailPart.class)
public class ProductDetailPartController {
}
