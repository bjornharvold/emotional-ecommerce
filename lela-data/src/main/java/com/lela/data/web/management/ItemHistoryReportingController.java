package com.lela.data.web.management;

import com.google.common.collect.Iterables;
import com.lela.data.domain.entity.Category;
import com.lela.data.domain.entity.ItemHistory;
import com.lela.data.enums.ItemHistoryReportType;
import com.lela.data.enums.ItemReportingAttribute;
import com.lela.data.enums.ItemStatuses;
import com.lela.data.excel.ItemWorkbook;
import com.lela.data.web.commands.ItemHistoryReportingCommand;
import com.lela.data.web.commands.ItemReportingCommand;
import org.joda.time.DateTime;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 11/14/12
 * Time: 9:57 AM
 * To change this template use File | Settings | File Templates.
 */
@RequestMapping("/manage/items/reporting/history")
@Controller
public class ItemHistoryReportingController {

    @RequestMapping(value = "/", produces = "text/html", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String reporting(Model uiModel) {
        ItemHistoryReportingCommand itemHistoryReportingCommand = initItemHistoryReportingCommand();

        uiModel.addAttribute("reportTypes", ItemHistoryReportType.values());
        uiModel.addAttribute("categorys" , Category.findAllCategorys());
        uiModel.addAttribute("itemHistoryReportingCommand", itemHistoryReportingCommand);
        uiModel.addAttribute("itemStatuses", ItemStatuses.values());
        return "manage/items/history";
    }

    private ItemHistoryReportingCommand initItemHistoryReportingCommand()
    {
        ItemHistoryReportingCommand itemHistoryReportingCommand = new ItemHistoryReportingCommand();
        DateTime _24hoursAgo = new DateTime();
        _24hoursAgo = _24hoursAgo.plusDays(-1);
        Date now = new Date();
        itemHistoryReportingCommand.setFrom(_24hoursAgo.toDate());
        itemHistoryReportingCommand.setTo(now);
        return itemHistoryReportingCommand;
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public String downloadItemReport(@Valid @ModelAttribute ItemHistoryReportingCommand itemHistoryReportingCommand, BindingResult bindingResult, Model uiModel, HttpServletRequest request, Principal principal) throws Exception {
       return generateItemReport(itemHistoryReportingCommand, bindingResult, uiModel, request);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @Transactional(readOnly = true)
    public String generateItemReport(@ModelAttribute @Valid ItemHistoryReportingCommand itemHistoryReportingCommand, BindingResult bindingResult, Model uiModel, HttpServletRequest request) throws Exception {

        uiModel.addAttribute("reportTypes", ItemHistoryReportType.values());
        uiModel.addAttribute("categorys" , Category.findAllCategorys());
        uiModel.addAttribute("itemHistoryReportingCommand", itemHistoryReportingCommand);
        uiModel.addAttribute("itemStatuses", ItemStatuses.values());

        if (!bindingResult.hasErrors()) {
            List<ItemHistory> itemHistories = ItemHistory.findChangedItems(itemHistoryReportingCommand);
            uiModel.addAttribute("itemHistories", itemHistories);
        }
        else{
            uiModel.addAllAttributes(bindingResult.getModel());
        }
        return "manage/items/history";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }
}
