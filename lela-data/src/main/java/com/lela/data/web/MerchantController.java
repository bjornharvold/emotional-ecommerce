package com.lela.data.web;

import com.lela.data.domain.entity.Merchant;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/merchants")
@Controller
@RooWebScaffold(path = "crud/merchants", formBackingObject = Merchant.class)
public class MerchantController {
}
