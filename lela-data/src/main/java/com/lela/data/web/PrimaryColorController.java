package com.lela.data.web;

import com.lela.data.domain.entity.PrimaryColor;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/primarycolors")
@Controller
@RooWebScaffold(path = "crud/primarycolors", formBackingObject = PrimaryColor.class)
public class PrimaryColorController {
}
