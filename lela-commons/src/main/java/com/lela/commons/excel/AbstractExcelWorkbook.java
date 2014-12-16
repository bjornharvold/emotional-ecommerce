/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Chris Tallent
 * Date: 6/8/12
 * Time: 4:28 PM
 */
public abstract class AbstractExcelWorkbook {
    public static final String NORMAL_STYLE = "cell_normal";
    public static final String NORMAL_STYLE_NOWRAP = "cell_normal_nowrap";
    public static final String NORMAL_CENTERED = "cell_normal_centered";
    public static final String HEADER_STYLE = "header";
    public static final String HEADER_DAY_MONTH_STYLE = "HEADER_DAY_MONTH_STYLE";
    public static final String HEADER_MONTH_YEAR_STYLE = "HEADER_MONTH_YEAR_STYLE";
    public static final String CELL_B_STYLE = "cell_b";
    public static final String NORMAL_DATE_RIGHT = "cell_normal_date";
    public static final String CELL_INDENTED_STYLE = "cell_indented";
    public static final String CELL_BLUE_STYLE = "cell_blue";
    protected static final int COLUMN_WIDTH_MULTIPLIER = 256;
    public static final String MONTH_DAY_YEAR = "cell_month_day_year";
    public static final String YEAR_MONTH_DAY = "cell_year_month_day";
    public static final String NUMBER_NO_DECIMALS = "NUMBER_NO_DECIMALS";
    public static final String NUMBER_DECIMALS = "NUMBER_DECIMALS";
    public static final String PERCENT_NO_DECIMALS = "PERCENT_NO_DECIMALS";
    public static final String PERCENT_DECIMALS = "PERCENT_DECIMALS";

    protected abstract String getSheetName();
    protected abstract Object[] getTitles();
    protected abstract int[] getColumnWidths();
    protected abstract void generateContent(Workbook wb, Sheet sheet, Map<String, CellStyle> styles);

    protected Workbook wb;

    public Workbook generate() {
        wb = newWorkbook();
        Map<String, CellStyle> styles = createStyles(wb);
        Sheet sheet = wb.createSheet(getSheetName());

        setSheetDefaults(sheet);

        //freeze the first row
        sheet.createFreezePane(0, 1);

        // Allow the implementing class to generate content on the sheet
        generateContent(wb, sheet, styles);

        // Handle Titles
        // the header row: centered text in 48pt font
        populateTitle(styles, sheet);

        // Set the column widths
        // The width is measured in units of 1/256th of a character width
        int[] columnWidths = getColumnWidths();
        for (int i = 0; i < columnWidths.length; i++) {
            sheet.setColumnWidth(i, COLUMN_WIDTH_MULTIPLIER * columnWidths[i]);
        }

        return wb;
    }

    protected int getTitleRow() {
        return 0;
    }

    protected String getTitleStyle() {
        return HEADER_STYLE;
    }

    protected String getTitleDateStyle() {
        return HEADER_DAY_MONTH_STYLE;
    }

    protected void populateTitle(Map<String, CellStyle> styles, Sheet sheet) {
        Object[] titles = getTitles();
        Row headerRow = sheet.createRow(getTitleRow());
        headerRow.setHeightInPoints(12.75f);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = headerRow.createCell(i);
            if (titles[i] instanceof Date) {
                cell.setCellValue((Date) titles[i]);
                cell.setCellStyle(styles.get(getTitleDateStyle()));
            } else {
                cell.setCellValue(String.valueOf(titles[i]));
                cell.setCellStyle(styles.get(getTitleStyle()));
            }
        }
    }

    protected void setSheetDefaults(Sheet sheet) {
        //turn off gridlines
        sheet.setDisplayGridlines(false);
        sheet.setPrintGridlines(false);
        sheet.setFitToPage(true);
        sheet.setHorizontallyCenter(true);

        PrintSetup printSetup = sheet.getPrintSetup();
        printSetup.setLandscape(true);
    }

    /**
     * create a library of cell styles
     */
    protected static Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        DataFormat df = wb.createDataFormat();

        CellStyle style;
        Font headerFont = wb.createFont();
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(headerFont);
        styles.put(HEADER_STYLE, style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(headerFont);
        style.setDataFormat(df.getFormat("d-mmm"));
        styles.put(HEADER_DAY_MONTH_STYLE, style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(headerFont);
        style.setDataFormat(df.getFormat("mmm-yy"));
        styles.put(HEADER_MONTH_YEAR_STYLE, style);

        Font font1 = wb.createFont();
        font1.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setFont(font1);
        styles.put(CELL_B_STYLE, style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFont(font1);
        styles.put("cell_b_centered", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setFont(font1);
        style.setDataFormat(df.getFormat("d-mmm"));
        styles.put("cell_b_date", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setFont(font1);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setDataFormat(df.getFormat("d-mmm"));
        styles.put("cell_g", style);

        Font font2 = wb.createFont();
        font2.setColor(IndexedColors.BLUE.getIndex());
        font2.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setFont(font2);
        styles.put("cell_bb", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setFont(font1);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setDataFormat(df.getFormat("d-mmm"));
        styles.put("cell_bg", style);

        Font font3 = wb.createFont();
        font3.setFontHeightInPoints((short) 14);
        font3.setColor(IndexedColors.DARK_BLUE.getIndex());
        font3.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setFont(font3);
        style.setWrapText(true);
        styles.put("cell_h", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setWrapText(true);
        styles.put(NORMAL_STYLE, style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setWrapText(false);
        styles.put(NORMAL_STYLE_NOWRAP, style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setWrapText(true);
        styles.put(NORMAL_CENTERED, style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setWrapText(true);
        style.setDataFormat(df.getFormat("d-mmm"));
        styles.put(NORMAL_DATE_RIGHT, style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setIndention((short) 1);
        style.setWrapText(true);
        styles.put(CELL_INDENTED_STYLE, style);

        style = createBorderedStyle(wb);
        style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styles.put(CELL_BLUE_STYLE, style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setWrapText(true);
        style.setDataFormat(df.getFormat("m-d-yyyy"));
        styles.put(MONTH_DAY_YEAR, style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setWrapText(true);
        style.setDataFormat(df.getFormat("yyyy-m-d"));
        styles.put(YEAR_MONTH_DAY, style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setDataFormat(df.getFormat("0"));
        styles.put(NUMBER_NO_DECIMALS, style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setDataFormat(df.getFormat("0.00"));
        styles.put(NUMBER_DECIMALS, style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setDataFormat(df.getFormat("0%"));
        styles.put(PERCENT_NO_DECIMALS, style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setDataFormat(df.getFormat("0.00%"));
        styles.put(PERCENT_DECIMALS, style);

        return styles;
    }

    private static CellStyle createBorderedStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        return style;
    }

    protected Workbook newWorkbook()
    {
        return new XSSFWorkbook();
    }
}
