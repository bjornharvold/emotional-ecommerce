package com.lela.data.web;

import com.lela.data.domain.entity.ReviewStatus;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/reviewstatus")
@Controller
@RooWebScaffold(path = "crud/reviewstatus", formBackingObject = ReviewStatus.class)
public class ReviewStatusController {
}
