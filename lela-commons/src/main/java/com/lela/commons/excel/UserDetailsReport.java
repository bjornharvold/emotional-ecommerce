/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.excel;

import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.admin.UserDetailReportRow;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by Chris Tallent
 */
public class UserDetailsReport extends AbstractExcelWorkbook {
    private static final String WORKSHEET_NAME = "Campaign Users";
    private static final String[] titles = {
            "First Name", "Last Name", "Email", "Reg Date", "Quiz Completed", "Device Type", "Repeat Visitor"
    };
    private static final int[] columnWidths = { 20, 20, 50, 20, 20, 20, 15, 20, 20 };
    private static final String DUE_DATE_KEY = "ddt";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    private static final String YES = "Yes";

    private final List<UserDetailReportRow> users;

    public UserDetailsReport(List<UserDetailReportRow> users) {
        this.users = users;
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
        if (users != null && !users.isEmpty()) {
            for (int i = 0; i < users.size(); i++, rownum++) {
                row = sheet.createRow(rownum);

                UserDetailReportRow data = users.get(i);
                UserSupplement us = data.getUs();

                if (StringUtils.isNotBlank(us.getFnm())) {
                    // first name
                    cell = row.createCell(0);
                    cell.setCellValue(us.getFnm());
                    cell.setCellStyle(styles.get(NORMAL_STYLE));
                }

                if (StringUtils.isNotBlank(us.getLnm())) {
                    // last name
                    cell = row.createCell(1);
                    cell.setCellValue(us.getLnm());
                    cell.setCellStyle(styles.get(NORMAL_STYLE));
                }

                if (StringUtils.isNotBlank(us.getMl())) {
                    // email
                    cell = row.createCell(2);
                    cell.setCellValue(us.getMl());
                    cell.setCellStyle(styles.get(NORMAL_STYLE));
                }

                // Check for a Registration Date
                if (us.getCdt() != null) {
                    cell = row.createCell(3);
                    cell.setCellValue(us.getCdt());
                    cell.setCellStyle(styles.get(MONTH_DAY_YEAR));
                }

                // Check for a Due Date
                /*
                if (us.getAttrs() != null) {
                    List<String> values = us.getAttrs().get(DUE_DATE_KEY);
                    if (values != null && !values.isEmpty()) {
                        cell = row.createCell(4);
                        Date date = null;
                        try {
                            date = dateFormat.parse(values.get(0));
                            cell.setCellValue(date);
                        } catch (ParseException e) {
                            cell.setCellValue(values.get(0));
                        }
                        cell.setCellStyle(styles.get(MONTH_DAY_YEAR));

                        if (date != null) {
                            cell = row.createCell(5);
                            cell.setCellValue(Days.daysBetween(new DateTime(), new DateTime(date.getTime())).getDays());
                            cell.setCellStyle(styles.get(NUMBER_NO_DECIMALS));
                        }
                    }
                }*/

                if (data.isQuizComplete()) {
                    // email
                    cell = row.createCell(4);
                    cell.setCellValue(YES);
                    cell.setCellStyle(styles.get(NORMAL_STYLE));
                }

                if (data.getRegistrationDeviceType() != null) {
                    cell = row.createCell(5);
                    cell.setCellValue(data.getRegistrationDeviceType().toString());
                    cell.setCellStyle(styles.get(NORMAL_STYLE));
                }

                if (data.isRepeatUser()) {
                    cell = row.createCell(6);
                    cell.setCellValue("Yes");
                    cell.setCellStyle(styles.get(NORMAL_STYLE));
                }
            }

            sheet.setZoom(5, 6);
        } else {
            row = sheet.createRow(rownum);
            cell = row.createCell(0);
            cell.setCellValue("No users are associated with this affiliate or campaign");
            cell.setCellStyle(styles.get(NORMAL_STYLE));
        }
    }

}
