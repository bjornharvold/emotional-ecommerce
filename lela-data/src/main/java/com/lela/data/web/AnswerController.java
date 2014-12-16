package com.lela.data.web;

import com.lela.data.domain.entity.Answer;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/answers")
@Controller
@RooWebScaffold(path = "crud/answers", formBackingObject = Answer.class)
public class AnswerController {
}
