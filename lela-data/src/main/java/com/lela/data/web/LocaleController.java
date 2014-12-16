package com.lela.data.web;

import com.lela.data.domain.entity.Locale;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/locales")
@Controller
@RooWebScaffold(path = "crud/locales", formBackingObject = Locale.class)
public class LocaleController {
}
