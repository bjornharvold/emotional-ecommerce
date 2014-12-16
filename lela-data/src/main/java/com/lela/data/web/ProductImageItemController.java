package com.lela.data.web;

import com.lela.data.domain.entity.ProductImageItem;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/productimageitems")
@Controller
@RooWebScaffold(path = "crud/productimageitems", formBackingObject = ProductImageItem.class)
public class ProductImageItemController {


}
