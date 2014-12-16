package com.lela.data.web;

import com.lela.data.domain.entity.ObjectIdSequence;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/objectidsequences")
@Controller
@RooWebScaffold(path = "crud/objectidsequences", formBackingObject = ObjectIdSequence.class)
public class ObjectIdSequenceController {
}
