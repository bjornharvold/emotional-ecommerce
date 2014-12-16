package com.lela.data.web;

import com.lela.data.domain.entity.ActionType;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/actiontypes")
@Controller
@RooWebScaffold(path = "crud/actiontypes", formBackingObject = ActionType.class)
public class ActionTypeController {
}
