package com.lela.data.web;

import com.lela.data.domain.entity.LocalStore;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/localstores")
@Controller
@RooWebScaffold(path = "crud/localstores", formBackingObject = LocalStore.class)
public class LocalStoreController {
}
