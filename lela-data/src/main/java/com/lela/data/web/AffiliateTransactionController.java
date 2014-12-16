package com.lela.data.web;

import com.lela.data.domain.entity.AffiliateTransaction;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/affiliatetransactions")
@Controller
@RooWebScaffold(path = "crud/affiliatetransactions", formBackingObject = AffiliateTransaction.class)
public class AffiliateTransactionController {
}
