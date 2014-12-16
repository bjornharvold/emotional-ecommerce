package com.lela.data.web;

import com.lela.data.domain.entity.ProductDetailPartValue;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/productdetailpartvalues")
@Controller
@RooWebScaffold(path = "crud/productdetailpartvalues", formBackingObject = ProductDetailPartValue.class)
public class ProductDetailPartValueController {
}
