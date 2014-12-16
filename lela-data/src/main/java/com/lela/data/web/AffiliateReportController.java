package com.lela.data.web;

import com.lela.data.domain.entity.AffiliateReport;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/crud/affiliatereports")
@Controller
@RooWebScaffold(path = "crud/affiliatereports", formBackingObject = AffiliateReport.class)
public class AffiliateReportController {
}
