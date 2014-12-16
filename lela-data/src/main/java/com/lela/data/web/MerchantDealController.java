package com.lela.data.web;

import com.lela.data.domain.entity.MerchantDeal;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/merchantdeals")
@Controller
@RooWebScaffold(path = "crud/merchantdeals", formBackingObject = MerchantDeal.class)
public class MerchantDealController {
}
