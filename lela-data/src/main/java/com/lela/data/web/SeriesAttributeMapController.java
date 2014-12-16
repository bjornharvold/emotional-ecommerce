package com.lela.data.web;

import com.lela.data.domain.entity.SeriesAttributeMap;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/seriesattributemaps")
@Controller
@RooWebScaffold(path = "crud/seriesattributemaps", formBackingObject = SeriesAttributeMap.class)
public class SeriesAttributeMapController {
}
