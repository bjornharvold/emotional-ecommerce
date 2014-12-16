package com.lela.data.web;

import com.lela.data.domain.entity.QuestionsSlider;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/questionssliders")
@Controller
@RooWebScaffold(path = "crud/questionssliders", formBackingObject = QuestionsSlider.class)
public class QuestionsSliderController {
}
