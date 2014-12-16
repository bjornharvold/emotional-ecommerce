package com.lela.data.web;

import com.lela.data.domain.entity.BrandHistory;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/brandhistorys")
@Controller
@RooWebScaffold(path = "crud/brandhistorys", formBackingObject = BrandHistory.class)
public class BrandHistoryController {
}
