package com.lela.data.web;

import com.lela.data.domain.entity.MerchantSourceType;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/merchantsourcetypes")
@Controller
@RooWebScaffold(path = "crud/merchantsourcetypes", formBackingObject = MerchantSourceType.class)
public class MerchantSourceTypeController {
}
