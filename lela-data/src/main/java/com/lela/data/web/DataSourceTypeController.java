package com.lela.data.web;

import com.lela.data.domain.entity.DataSourceType;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/datasourcetypes")
@Controller
@RooWebScaffold(path = "crud/datasourcetypes", formBackingObject = DataSourceType.class)
public class DataSourceTypeController {
}
