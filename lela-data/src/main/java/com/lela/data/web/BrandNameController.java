package com.lela.data.web;

import com.lela.data.domain.entity.BrandName;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/brandnames")
@Controller
@RooWebScaffold(path = "crud/brandnames", formBackingObject = BrandName.class)
public class BrandNameController {
}
