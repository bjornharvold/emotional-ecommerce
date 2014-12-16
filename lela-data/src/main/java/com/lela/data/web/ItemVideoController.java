package com.lela.data.web;

import com.lela.data.domain.entity.ItemVideo;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/itemvideos")
@Controller
@RooWebScaffold(path = "crud/itemvideos", formBackingObject = ItemVideo.class)
public class ItemVideoController {
}
