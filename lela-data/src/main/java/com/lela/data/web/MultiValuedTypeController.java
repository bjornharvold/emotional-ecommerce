package com.lela.data.web;

import com.lela.data.domain.entity.MultiValuedType;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/multivaluedtypes")
@Controller
@RooWebScaffold(path = "crud/multivaluedtypes", formBackingObject = MultiValuedType.class)
public class MultiValuedTypeController {
}
