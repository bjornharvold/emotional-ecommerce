package com.lela.data.web;

import com.lela.data.domain.entity.ItemColorMerchant;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/itemcolormerchants")
@Controller
@RooWebScaffold(path = "crud/itemcolormerchants", formBackingObject = ItemColorMerchant.class)
public class ItemColorMerchantController {
}
