package com.lela.data.web;

import com.lela.data.domain.entity.ImageSource;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/imagesources")
@Controller
@RooWebScaffold(path = "crud/imagesources", formBackingObject = ImageSource.class)
public class ImageSourceController {
}
