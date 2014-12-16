package com.lela.data.web;

import com.lela.data.domain.entity.AccessoryValueGroup;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/accessoryvaluegroups")
@Controller
@RooWebScaffold(path = "crud/accessoryvaluegroups", formBackingObject = AccessoryValueGroup.class)
public class AccessoryValueGroupController {
}
