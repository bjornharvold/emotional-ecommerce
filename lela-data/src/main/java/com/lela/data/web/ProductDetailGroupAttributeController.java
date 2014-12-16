package com.lela.data.web;

import com.lela.data.domain.entity.ProductDetailGroupAttribute;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/productdetailgroupattributes")
@Controller
@RooWebScaffold(path = "crud/productdetailgroupattributes", formBackingObject = ProductDetailGroupAttribute.class)
public class ProductDetailGroupAttributeController {
}
