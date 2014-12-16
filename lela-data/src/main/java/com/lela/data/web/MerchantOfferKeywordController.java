package com.lela.data.web;

import com.lela.data.domain.entity.MerchantOfferKeyword;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/merchantofferkeywords")
@Controller
@RooWebScaffold(path = "crud/merchantofferkeywords", formBackingObject = MerchantOfferKeyword.class)
public class MerchantOfferKeywordController {
}
