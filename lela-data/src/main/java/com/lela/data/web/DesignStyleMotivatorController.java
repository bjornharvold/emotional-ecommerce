package com.lela.data.web;

import com.lela.data.domain.entity.DesignStyleMotivator;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/designstylemotivators")
@Controller
@RooWebScaffold(path = "crud/designstylemotivators", formBackingObject = DesignStyleMotivator.class)
public class DesignStyleMotivatorController {
}
