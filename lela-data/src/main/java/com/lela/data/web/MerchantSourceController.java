package com.lela.data.web;

import com.lela.data.domain.entity.MerchantSource;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/merchantsources")
@Controller
@RooWebScaffold(path = "crud/merchantsources", formBackingObject = MerchantSource.class)
public class MerchantSourceController {
}
