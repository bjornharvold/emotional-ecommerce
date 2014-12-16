/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.excel;

import com.lela.domain.document.Motivator;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.report.UserMotivatorWorkbookData;
import com.lela.domain.dto.report.UserUserSupplementEntry;
import com.lela.domain.enums.MotivatorSource;
import com.lela.util.utilities.string.StringUtility;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 5/31/12
 * Time: 10:28 PM
 * Responsibility:
 */
public class UserMotivatorWorkbook extends AbstractExcelWorkbook {
    private static final String USER_MOTIVATORS = "User Motivators";
    private static final String[] titles = {
            "First Name", "Last Name", "Email", "Gender", "Origin", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private static final int[] columnWidths = {20, 20, 50, 20, 20};

    private final UserMotivatorWorkbookData data;

    public UserMotivatorWorkbook(UserMotivatorWorkbookData data) {
        this.data = data;
    }

    @Override
    protected String getSheetName() {
        return USER_MOTIVATORS;
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

        if (data.getUsers() != null && !data.getUsers().isEmpty()){
            // looping through all users
            for (int i = 0; i < data.getUsers().size(); i++, rownum++) {
                row = sheet.createRow(rownum);
                row.setRowStyle(styles.get(NORMAL_STYLE));

                UserUserSupplementEntry entry = data.getUsers().get(i);

                if (StringUtils.isNotBlank(entry.getUserSupplement().getFnm())) {
                    // first name
                    cell = row.createCell(0);
                    cell.setCellValue(entry.getUserSupplement().getFnm());
                    cell.setCellStyle(styles.get(NORMAL_CENTERED));
                }

                if (StringUtils.isNotBlank(entry.getUserSupplement().getLnm())) {
                    // last name
                    cell = row.createCell(1);
                    cell.setCellValue(entry.getUserSupplement().getLnm());
                    cell.setCellStyle(styles.get(NORMAL_CENTERED));
                }

                if (StringUtils.isNotBlank(entry.getUser().getMl())) {
                    // email
                    cell = row.createCell(2);
                    cell.setCellValue(entry.getUser().getMl());
                    cell.setCellStyle(styles.get(NORMAL_CENTERED));
                }

                if (entry.getUserSupplement().getGndr() != null) {
                    // gender
                    cell = row.createCell(3);
                    cell.setCellValue(entry.getUserSupplement().getGndr().name());
                    cell.setCellStyle(styles.get(NORMAL_CENTERED));
                }

                Map<MotivatorSource, Motivator> motivators = entry.getUserSupplement().getMtvtrmp();

                if (motivators != null && !motivators.isEmpty()) {

                    int j = 4;
                    for (Map.Entry<MotivatorSource, Motivator> motivator : motivators.entrySet()) {
                        rownum++;
                        row = sheet.createRow(rownum);
                        row.setRowStyle(styles.get(NORMAL_CENTERED));

                        cell = row.createCell(j);
                        cell.setCellValue(motivator.getKey().name());
                        cell.setCellStyle(styles.get(NORMAL_CENTERED));

                        j++;

                        for (String letter : StringUtility.getAlphabet()) {
                            if (motivator.getValue().getMtvtrs() != null && motivator.getValue().getMtvtrs().containsKey(letter.toUpperCase())) {
                                cell = row.createCell(j);
                                cell.setCellValue(motivator.getValue().getMtvtrs().get(letter.toUpperCase()));
                                cell.setCellStyle(styles.get(NORMAL_CENTERED));
                            }

                            j++;
                        }

                        j = 4;
                    }
                }
            }

        }
        sheet.setZoom(5, 6);
    }

    public static void main(String[] args) {
        List<UserUserSupplementEntry> users = new ArrayList<UserUserSupplementEntry>();
        UserSupplement us = new UserSupplement();
        us.setFnm("Bjorn");
        us.setLnm("Harvold");
        us.setMl("bjorn@lela.com");
        us.setCd("bjorn");

        List<Motivator> motivators = new ArrayList<Motivator>(2);

        Map<String, Integer> quizMotivators = new HashMap<String, Integer>();
        for (String letter : StringUtility.getAlphabet()) {
            quizMotivators.put(letter, Integer.parseInt(RandomStringUtils.randomNumeric(1)));
        }
        Map<String, Integer> fbMotivators = new HashMap<String, Integer>();
        for (String letter : StringUtility.getAlphabet()) {
            Integer randomNumber = Integer.parseInt(RandomStringUtils.randomNumeric(1));

            if (randomNumber > 5) {
                fbMotivators.put(letter, Integer.parseInt(RandomStringUtils.randomNumeric(1)));
            }
        }

        Motivator fb = new Motivator(MotivatorSource.FACEBOOK, fbMotivators);

        Motivator quiz = new Motivator(MotivatorSource.QUIZ, quizMotivators);
        motivators.add(quiz);
        motivators.add(fb);
        us.setMtvtrmp(new HashMap<MotivatorSource, Motivator>());
        us.getMtvtrmp().put(MotivatorSource.QUIZ, quiz);
        us.getMtvtrmp().put(MotivatorSource.FACEBOOK, fb);

        UserUserSupplementEntry entry = new UserUserSupplementEntry(null, us);
        users.add(entry);

        UserMotivatorWorkbookData data = new UserMotivatorWorkbookData(users);

        UserMotivatorWorkbook userMotivatorWorkbook = new UserMotivatorWorkbook(data);

        try {
            Workbook wb = userMotivatorWorkbook.generate();

            // Write the output to a file
            String file = "user_motivator_report.xlsx";
            FileOutputStream out = new FileOutputStream(file);
            wb.write(out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
