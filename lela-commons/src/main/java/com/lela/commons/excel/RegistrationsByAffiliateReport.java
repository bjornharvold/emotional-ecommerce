/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.excel;

import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.Campaign;
import com.lela.domain.dto.admin.AffiliateUserTracking;
import com.lela.domain.dto.admin.AffiliateUserTrackingPerPeriod;
import com.lela.domain.dto.admin.AffiliateUserTrackingPerYear;
import com.lela.domain.dto.admin.RegistrationsByAffiliateResult;
import com.lela.domain.enums.DeviceType;
import com.lela.domain.enums.RegistrationType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * User: Chris Tallent
 * Date: 8/22/12
 *
 * This report is not thread-safe
 */
public class RegistrationsByAffiliateReport extends AbstractExcelWorkbook {
    private static final String WORKSHEET_NAME = "Registrations";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-M-dd");
    private static final SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat("MMM-yy");
    private static final String TOTAL = "total";

    private final List<RegistrationsByAffiliateResult> data;
    private final Set<String> affiliates;
    private Integer total = 0;

    private List<Object> titles = new ArrayList<Object>();
    private List<Integer> columnWidths = new ArrayList<Integer>();

    private Workbook wb;
    private Sheet sheet;
    private Map<String, CellStyle> styles;

    private Row row;
    private int rowNum = 0;

    private Cell cell;
    private int cellNum = 0;

    public RegistrationsByAffiliateReport(List<RegistrationsByAffiliateResult> data) {
        this.data = data;
        Collections.sort(this.data, Collections.reverseOrder());

        // Titles and column widths
        titles.add("Date");
        columnWidths.add(25);

        titles.add("Total");
        columnWidths.add(25);

        this.affiliates = new TreeSet<String>();
        for (RegistrationsByAffiliateResult result : this.data) {
            for (String affiliate : result.getValue().keySet()) {
                if (TOTAL.equals(affiliate)) {
                    total += result.getValue().get(TOTAL);
                } else {
                    affiliates.add(affiliate);
                }
            }
        }
        affiliates.remove(TOTAL);

        //  Add titles for affiliates
        for (String affiliate : affiliates) {
            titles.add(affiliate);
            columnWidths.add(25);
        }
    }

    @Override
    protected String getSheetName() {
        return WORKSHEET_NAME;
    }

    @Override
    protected Object[] getTitles() {
        return titles.toArray();
    }

    @Override
    protected String getTitleDateStyle() {
        return HEADER_MONTH_YEAR_STYLE;
    }

    @Override
    protected int getTitleRow() {
        return 3;
    }

    @Override
    protected int[] getColumnWidths() {
        int[] result = new int[columnWidths.size()];
        for (int i=0; i<columnWidths.size(); i++) {
            result[i] = columnWidths.get(i);
        }

        return result;
    }

    @Override
    protected void generateContent(Workbook wb, Sheet sheet, Map<String, CellStyle> styles) {
        this.wb = wb;
        this.sheet = sheet;
        this.styles = styles;

        // Heading information
        nextRow();
        cell("Report Date", HEADER_STYLE);
        cell(new Date(), MONTH_DAY_YEAR);

        nextRow();
        cell("Total", HEADER_STYLE);
        cell(total, NUMBER_NO_DECIMALS);

        // Skip the title Row
        rowNum = 4;

        if (data != null && !data.isEmpty()) {
            for (RegistrationsByAffiliateResult result : data) {
                nextRow();
                cell(result.getId(), MONTH_DAY_YEAR);
                cell(result.getValue().get(TOTAL), NUMBER_NO_DECIMALS);

                for (String affiliate : affiliates) {
                    if (result.getValue().containsKey(affiliate)) {
                        cell(result.getValue().get(affiliate), NUMBER_NO_DECIMALS);
                    } else {
                        skipCell();
                    }
                }
            }
        } else {
            nextRow();
            cell("No data is available for this Affiliate", NORMAL_STYLE);
        }

        //freeze the first row
        sheet.createFreezePane(0, 4);
        sheet.setZoom(5, 6);
    }

    private Row nextRow() {
        row = sheet.createRow(rowNum++);
        cellNum = 0;

        return row;
    }

    private void cell(String value, String style) {
        if (value != null) {
            cell = row.createCell(cellNum++);
            cell.setCellValue(value);
            cell.setCellStyle(styles.get(style));
        } else {
            skipCell();
        }
    }

    private void cell(Integer value, String style) {
        if (value != null) {
            cell = row.createCell(cellNum++);
            cell.setCellValue(value.intValue());
            cell.setCellStyle(styles.get(style));
        } else {
            skipCell();
        }
    }

    private void cell(Float value, String style) {
        if (value != null) {
            cell = row.createCell(cellNum++);
            cell.setCellValue(value.floatValue());
            cell.setCellStyle(styles.get(style));
        } else {
            skipCell();
        }
    }

    private void cell(Double value, String style) {
        if (value != null) {
            cell = row.createCell(cellNum++);
            cell.setCellValue(value.doubleValue());
            cell.setCellStyle(styles.get(style));
        } else {
            skipCell();
        }
    }

    private void cell(Date value, String style) {
        if (value != null) {
            cell = row.createCell(cellNum++);
            cell.setCellValue(value);
            cell.setCellStyle(styles.get(style));
        } else {
            skipCell();
        }
    }

    private void skipCell() {
        cellNum++;
    }

    private void skipCells(int skip) {
        cellNum += skip;
    }
}