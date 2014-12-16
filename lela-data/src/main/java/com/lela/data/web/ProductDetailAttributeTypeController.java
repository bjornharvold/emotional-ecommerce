package com.lela.data.web;

import com.lela.data.domain.entity.ProductDetailAttributeType;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/productdetailattributetypes")
@Controller
@RooWebScaffold(path = "crud/productdetailattributetypes", formBackingObject = ProductDetailAttributeType.class)
public class ProductDetailAttributeTypeController {
}
