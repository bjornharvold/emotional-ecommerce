package com.lela.data.web.controller.administration;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
@Controller
public class AdminController {

    @RequestMapping
    public String index(ModelMap modelMap) {

        return "administration.dashboard";
    }
}
