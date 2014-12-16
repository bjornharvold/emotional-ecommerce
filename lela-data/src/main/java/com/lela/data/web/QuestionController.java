package com.lela.data.web;

import com.lela.data.domain.entity.Question;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/questions")
@Controller
@RooWebScaffold(path = "crud/questions", formBackingObject = Question.class)
public class QuestionController {
}
