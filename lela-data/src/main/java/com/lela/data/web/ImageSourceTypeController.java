package com.lela.data.web;

import com.lela.data.domain.entity.ImageSourceType;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/imagesourcetypes")
@Controller
@RooWebScaffold(path = "crud/imagesourcetypes", formBackingObject = ImageSourceType.class)
public class ImageSourceTypeController {
}
