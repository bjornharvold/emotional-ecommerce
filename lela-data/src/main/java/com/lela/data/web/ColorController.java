package com.lela.data.web;

import com.lela.data.domain.entity.Color;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/colors")
@Controller
@RooWebScaffold(path = "crud/colors", formBackingObject = Color.class)
public class ColorController {
}
