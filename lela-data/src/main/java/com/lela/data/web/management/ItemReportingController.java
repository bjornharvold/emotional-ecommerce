package com.lela.data.web.management;

import com.google.common.collect.Iterables;
import com.lela.data.domain.entity.Category;
import com.lela.data.enums.ItemReportingAttribute;
import com.lela.data.excel.ItemWorkbook;
import com.lela.data.service.ItemWorkbookService;
import com.lela.data.web.commands.ItemReportingCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 8/1/12
 * Time: 9:24 AM
 * To change this template use File | Settings | File Templates.
 */

@RequestMapping("/manage/items/reporting")
@Controller
public class ItemReportingController implements ApplicationContextAware{



    ApplicationContext ctx;

    @Autowired
    @Resource(name="itemReportingStatusMap")
    Map<String, String> itemReportingStatusMap;

    @Autowired
    @Resource(name="itemWorkbookService")
    ItemWorkbookService itemWorkbookService;

    @RequestMapping(value = "/", produces = "text/html", method = RequestMethod.GET)
    public String reporting(Model uiModel) {
        uiModel.addAttribute("categorys" ,Category.findAllCategorys());
        uiModel.addAttribute("itemReportingStatus" , itemReportingStatusMap);
        uiModel.addAttribute("motivators", ItemReportingCommand.allMotivators);
        ItemReportingCommand itemReportingCommand = initItemReportingCommand();
        uiModel.addAttribute("itemReportingCommand", itemReportingCommand);
        return "manage/items/index";
    }

    private ItemReportingCommand initItemReportingCommand()
    {
        ItemReportingCommand itemReportingCommand = new ItemReportingCommand();
        itemReportingCommand.getItemSearchCommand().setItemStatus(new String[]{Iterables.get(itemReportingStatusMap.keySet(), 0)});
        itemReportingCommand.getItemWorkbookCommand().setShowAttribute(ItemReportingAttribute.NONE);
        return itemReportingCommand;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @Transactional(readOnly = true)
    public String generateItemReport(@ModelAttribute ItemReportingCommand itemReportingCommand, Model uiModel, HttpServletRequest request, Principal principal) throws Exception {

        ItemWorkbook itemWorkbook = (ItemWorkbook)ctx.getBean("itemWorkbook");
        itemReportingCommand.setRecipientEmailAddress(principal.getName());
        itemWorkbook.setItemReportingCommand(itemReportingCommand);
        itemWorkbookService.run(itemWorkbook);

        List<ItemWorkbook> workbooks = (List<ItemWorkbook>)request.getSession(true).getAttribute("workbooks");
        if(workbooks == null)
        {
            workbooks = new ArrayList<ItemWorkbook>();
        }
        workbooks.add(itemWorkbook);
        request.getSession(true).setAttribute("workbooks", workbooks);

        return reporting(uiModel);
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void downloadCompletedReport(String workbookId, HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        List<ItemWorkbook> workbooks = (List<ItemWorkbook>)request.getSession(true).getAttribute("workbooks");
        ItemWorkbook theWorkbook = null;
        if(workbooks!=null)
        {
            for(ItemWorkbook workbook:workbooks)
            {
                if (workbook.getId().equals(workbookId))
                {
                    theWorkbook = workbook;
                }
            }
        }
        if(theWorkbook.getException()==null)
        {
            response.setHeader("Content-Disposition", "attachment; filename=" + theWorkbook.getFilename());
            response.setContentType("application/vnd.ms-excel.12");

            //ServletOutputStream out = response.getOutputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            theWorkbook.writeCached(out);
            ServletOutputStream servletOut = response.getOutputStream();
            servletOut.write(out.toByteArray());
            servletOut.close();
        }
        else
        {
            response.setHeader("Content-Disposition", "attachment; filename=" + "exception.txt");
            response.setContentType("text/plain");

            theWorkbook.getException().printStackTrace(response.getWriter());
        }

        response.flushBuffer();
    }


    public void setApplicationContext(ApplicationContext ctx)
    {
        this.ctx = ctx;
    }
}
