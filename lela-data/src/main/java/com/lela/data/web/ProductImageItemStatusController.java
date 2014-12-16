package com.lela.data.web;

import com.lela.data.domain.entity.ProductImageItemStatus;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/productimageitemstatus")
@Controller
@RooWebScaffold(path = "crud/productimageitemstatus", formBackingObject = ProductImageItemStatus.class)
public class ProductImageItemStatusController {
}
