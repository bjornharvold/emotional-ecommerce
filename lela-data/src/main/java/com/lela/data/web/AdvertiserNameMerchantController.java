package com.lela.data.web;

import com.lela.data.domain.entity.AdvertiserNameMerchant;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/advertisernamemerchants")
@Controller
@RooWebScaffold(path = "crud/advertisernamemerchants", formBackingObject = AdvertiserNameMerchant.class)
public class AdvertiserNameMerchantController {
}
