package com.lela.data.web.management;

import com.lela.data.domain.entity.Category;
import com.lela.data.enums.ItemHistoryReportType;
import com.lela.data.enums.ItemStatuses;
import com.lela.data.web.commands.ItemHistoryReportingCommand;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 11/27/12
 * Time: 2:24 PM
 * To change this template use File | Settings | File Templates.
 */
@RequestMapping("/info")
@Controller
public class InfoController {

    @RequestMapping(value = "/", produces = "text/html", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String index(Model uiModel) {
        return "info";
    }
}
