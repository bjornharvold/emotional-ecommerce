/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.data.web.controller.administration.owner;

import com.lela.commons.service.AdministrationService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.AbstractItem;
import com.lela.domain.dto.OwnerItemsQuery;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 9/23/11
 * Time: 4:02 PM
 * Responsibility:
 */
@Controller("administrationOwnerController")
@SessionAttributes(types = OwnerItemsQuery.class)
public class OwnerManagerController {
    private final static Logger log = LoggerFactory.getLogger(OwnerManagerController.class);

    private final AdministrationService administrationService;

    @Autowired
    public OwnerManagerController(AdministrationService administrationService) {
        this.administrationService = administrationService;
    }

    @RequestMapping(value = "/administration/owner/list", method = RequestMethod.GET)
    public String showOwnersPage(Model model) throws Exception {
        model.addAttribute(WebConstants.OWNER_QUERY, new OwnerItemsQuery());

        return "administration.owners";
    }

    @RequestMapping(value = "/administration/owner/list", method = RequestMethod.POST)
    public String showOwnersData(OwnerItemsQuery query, HttpServletResponse response,
                                 Model model) throws Exception {

        List<AbstractItem> items = null;
        MultipartFile excelFile = query.getExcelFile();
        if ((excelFile != null) && (excelFile.getSize() > 0)) {
            HSSFWorkbook updateWorkBook = new HSSFWorkbook();
            items = administrationService.processOwnersDataFromExcel(query, excelFile, updateWorkBook);

            try {
                response.setHeader("Content-Disposition","inline; filename=processed-" + excelFile.getOriginalFilename());
                response.setContentType("application/vnd.ms-excel");

                ServletOutputStream out = response.getOutputStream();
                updateWorkBook.write(out);

                response.flushBuffer();
            } catch(Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        } else {
            items = administrationService.processOwnersDataFromInputs(query);
        }

        model.addAttribute(WebConstants.OWNER_SEARCH_RESULT, items);
        model.addAttribute(WebConstants.OWNER_QUERY, query);

        return "administration.owners.data";
    }
}
