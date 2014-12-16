package com.lela.data.web;

import com.lela.data.domain.entity.ProductImage;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/productimages")
@Controller
@RooWebScaffold(path = "crud/productimages", formBackingObject = ProductImage.class)
public class ProductImageController {
}
