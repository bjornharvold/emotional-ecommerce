/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.excel;

import com.lela.domain.dto.admin.CampaignDailyTotal;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Chris Tallent
 */
public class CampaignDailyBreakdown extends AbstractExcelWorkbook {
    private static final String WORKSHEET_NAME = "Daily Counts";
    private static final String[] titles = {
            "Date", "Total Registers", "Campaign Registers", "Total completing Quiz", "Campaign Completing Quiz"
    };
    private static final int[] columnWidths = { 12, 20, 22, 20, 25 };

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-M-dd");

    private final List<CampaignDailyTotal> totals;

    public CampaignDailyBreakdown(List<CampaignDailyTotal> totals) {
        this.totals = totals;
    }

    @Override
    protected String getSheetName() {
        return WORKSHEET_NAME;
    }

    @Override
    protected String[] getTitles() {
        return titles;
    }

    @Override
    protected int[] getColumnWidths() {
        return columnWidths;
    }

    @Override
    protected void generateContent(Workbook wb, Sheet sheet, Map<String, CellStyle> styles) {
        Row row;
        Cell cell;
        int rownum = 1;

        // looping through all users
        for (int i = 0; i < totals.size(); i++, rownum++) {
            row = sheet.createRow(rownum);

            CampaignDailyTotal total = totals.get(i);

            if (StringUtils.isNotEmpty(total.getId())) {
                // first name
                cell = row.createCell(0);

                Date date = null;
                try {
                    date = DATE_FORMAT.parse(total.getId());
                    cell.setCellValue(date);
                } catch (ParseException e) {
                    cell.setCellValue(total.getId());
                }

                cell.setCellStyle(styles.get(YEAR_MONTH_DAY));
            }

            if (total.getValue().getTotal() != null) {
                // first name
                cell = row.createCell(1);
                cell.setCellValue(total.getValue().getTotal());
                cell.setCellStyle(styles.get(NORMAL_STYLE));
            }

            if (total.getValue().getAssoc() != null) {
                // last name
                cell = row.createCell(2);
                cell.setCellValue(total.getValue().getAssoc());
                cell.setCellStyle(styles.get(NORMAL_STYLE));
            }

            if (total.getValue().getTotalQuiz() != null) {
                // email
                cell = row.createCell(3);
                cell.setCellValue(total.getValue().getTotalQuiz());
                cell.setCellStyle(styles.get(NORMAL_STYLE));
            }

            if (total.getValue().getAssocQuiz() != null) {
                // email
                cell = row.createCell(4);
                cell.setCellValue(total.getValue().getAssocQuiz());
                cell.setCellStyle(styles.get(NORMAL_STYLE));
            }

        }

        sheet.setZoom(5, 6);
    }

}
