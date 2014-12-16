package com.lela.data.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud")
@Controller
public class MasterDataController {

    @RequestMapping
    public String index() {
        return "masterdata/index";
    }
}
