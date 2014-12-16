package com.lela.data.web;

import com.lela.data.domain.entity.Series;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/series")
@Controller
@RooWebScaffold(path = "crud/series", formBackingObject = Series.class)
public class SeriesController {
}
