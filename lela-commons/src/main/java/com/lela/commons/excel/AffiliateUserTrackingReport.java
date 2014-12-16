/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.excel;

import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.Campaign;
import com.lela.domain.dto.admin.AffiliateUserTracking;
import com.lela.domain.dto.admin.AffiliateUserTrackingPerPeriod;
import com.lela.domain.dto.admin.AffiliateUserTrackingPerYear;
import com.lela.domain.dto.admin.CampaignDailyTotal;
import com.lela.domain.enums.DeviceType;
import com.lela.domain.enums.RegistrationType;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: Chris Tallent
 * Date: 8/13/12
 * Time: 5:17 PM
 *
 * This report is not thread-safe
 */
public class AffiliateUserTrackingReport extends AbstractExcelWorkbook {
    private static final String WORKSHEET_NAME = "User Tracking";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-M-dd");
    private static final SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat("MMM-yy");

    private final AffiliateAccount affiliate;
    private final Campaign campaign;
    private final AffiliateUserTracking data;

    private List<AffiliateUserTrackingPerPeriod> months;
    private List<AffiliateUserTrackingPerYear> years;
    private AffiliateUserTrackingPerPeriod currentMonth;

    private List<Object> titles = new ArrayList<Object>();
    private List<Integer> columnWidths = new ArrayList<Integer>();

    private Workbook wb;
    private Sheet sheet;
    private Map<String, CellStyle> styles;

    private Row row;
    private int rowNum = 0;

    private Cell cell;
    private int cellNum = 0;

    public AffiliateUserTrackingReport(AffiliateAccount affiliate, AffiliateUserTracking data) {
        this(affiliate, null, data);
    }

    public AffiliateUserTrackingReport(AffiliateAccount affiliate, Campaign campaign, AffiliateUserTracking data) {
        this.affiliate = affiliate;
        this.campaign = campaign;
        this.data = data;

        months = new ArrayList<AffiliateUserTrackingPerPeriod>();
        if (data != null) {
            months.addAll(data.getByMonth().values());
        }

        years = new ArrayList<AffiliateUserTrackingPerYear>();
        if (data != null) {
            years.addAll(data.getByYear().values());
        }

        // Titles and column widths
        titles.add("");
        columnWidths.add(45);

        titles.add("Overall Total");
        columnWidths.add(22);

        titles.add("Previous Day Total");
        columnWidths.add(22);

        titles.add("Current Month avg");
        columnWidths.add(22);

        titles.add("Current Month total");
        columnWidths.add(22);

        DateTime now = new DateTime();
        for (AffiliateUserTrackingPerPeriod month: months) {
            DateTime period = new DateTime(month.getPeriod().getTime());

            titles.add(month.getPeriod());
            columnWidths.add(13);

            if (period.getYear() == now.getYear() && period.getMonthOfYear() == now.getMonthOfYear()) {
                currentMonth = month;
            }
        }

        for (AffiliateUserTrackingPerYear year: years) {
            String period = String.valueOf(new DateTime(year.getPeriod().getTime()).getYear());

            titles.add(period + " Daily Avg");
            columnWidths.add(20);

            titles.add(period + " Month Avg");
            columnWidths.add(20);

            titles.add(period + " Daily Total");
            columnWidths.add(20);
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
        return 2;
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
        cell("Affiliate", HEADER_STYLE);
        cell(affiliate.getNm(), NORMAL_STYLE);
        cell(new Date(), MONTH_DAY_YEAR);

        // Campaign information
        if (campaign != null) {
            nextRow();
            cell("Campaign", HEADER_STYLE);
            cell(campaign.getNm(), NORMAL_STYLE);
        }

        // Skip the title Row
        rowNum = 3;

        if (data != null) {
            // Unique visitors
            nextRow();
            cell("Unique Visitors", NORMAL_STYLE);
            cell(data.getUnique(), NUMBER_NO_DECIMALS);

            if (data.getYesterday() != null) {
                cell(data.getYesterday().getUnique(), NUMBER_NO_DECIMALS);
            } else {
                skipCell();
            }

            if (currentMonth != null) {
                cell(currentMonth.getUniqueDayAvg(), NUMBER_DECIMALS);
                cell(currentMonth.getUnique(), NUMBER_NO_DECIMALS);
            } else {
                skipCells(2);
            }

            for (AffiliateUserTrackingPerPeriod month : months) {
                cell(month.getUnique(), NUMBER_NO_DECIMALS);
            }

            for (AffiliateUserTrackingPerYear year : years) {
                cell(year.getUniqueDayAvg(), NUMBER_DECIMALS);
                cell(year.getUniqueMonthAvg(), NUMBER_DECIMALS);
                cell(year.getUnique(), NUMBER_NO_DECIMALS);
            }

            // Pages / Visit
            nextRow();
            cell("# Pages/Visit", NORMAL_STYLE);
            cell(data.getPagesPerVisit(), NUMBER_DECIMALS);

            if (data.getYesterday() != null) {
                cell(data.getYesterday().getPagesPerVisit(), NUMBER_DECIMALS);
            } else {
                skipCell();
            }

            if (currentMonth != null) {
                skipCell();
                cell(currentMonth.getPagesPerVisit(), NUMBER_DECIMALS);
            } else {
                skipCells(2);
            }

            for (AffiliateUserTrackingPerPeriod month : months) {
                cell(month.getPagesPerVisit(), NUMBER_DECIMALS);
            }

            for (AffiliateUserTrackingPerYear year : years) {
                skipCells(2);
                cell(year.getPagesPerVisit(), NUMBER_DECIMALS);
            }

            // Returning Visitors
            nextRow();
            cell("Returning Visitors", NORMAL_STYLE);
            cell(data.getReturned(), NUMBER_NO_DECIMALS);

            if (data.getYesterday() != null) {
                cell(data.getYesterday().getReturned(), NUMBER_NO_DECIMALS);
            } else {
                skipCell();
            }

            if (currentMonth != null) {
                cell(currentMonth.getReturnedDayAvg(), NUMBER_DECIMALS);
                cell(currentMonth.getReturned(), NUMBER_NO_DECIMALS);
            } else {
                skipCells(2);
            }

            for (AffiliateUserTrackingPerPeriod month : months) {
                cell(month.getReturned(), NUMBER_NO_DECIMALS);
            }

            for (AffiliateUserTrackingPerYear year : years) {
                cell(year.getReturnedDayAvg(), NUMBER_DECIMALS);
                cell(year.getReturnedMonthAvg(), NUMBER_DECIMALS);
                cell(year.getReturned(), NUMBER_NO_DECIMALS);
            }

            // Returned Rate
            nextRow();
            cell("Returned Rate", NORMAL_STYLE);
            cell(data.getReturnedRate(), PERCENT_DECIMALS);

            if (data.getYesterday() != null) {
                cell(data.getYesterday().getReturnedRate(), PERCENT_DECIMALS);
            } else {
                skipCell();
            }

            if (currentMonth != null) {
                skipCell();
                cell(currentMonth.getReturnedRate(), PERCENT_DECIMALS);
            } else {
                skipCells(2);
            }

            for (AffiliateUserTrackingPerPeriod month : months) {
                cell(month.getReturnedRate(), PERCENT_DECIMALS);
            }

            for (AffiliateUserTrackingPerYear year : years) {
                skipCells(2);
                cell(year.getReturnedRate(), PERCENT_DECIMALS);
            }

            // Bounce Rate
            nextRow();
            cell("Bounce Rate", NORMAL_STYLE);
            cell(data.getBounceRate(), PERCENT_DECIMALS);

            if (data.getYesterday() != null) {
                cell(data.getYesterday().getBounceRate(), PERCENT_DECIMALS);
            } else {
                skipCell();
            }

            if (currentMonth != null) {
                skipCell();
                cell(currentMonth.getBounceRate(), PERCENT_DECIMALS);
            } else {
                skipCells(2);
            }

            for (AffiliateUserTrackingPerPeriod month : months) {
                cell(month.getBounceRate(), PERCENT_DECIMALS);
            }

            for (AffiliateUserTrackingPerYear year : years) {
                skipCells(2);
                cell(year.getBounceRate(), PERCENT_DECIMALS);
            }

            // Stores click through
            nextRow();
            cell("# Store clicks", NORMAL_STYLE);
            cell(data.getStores(), NUMBER_NO_DECIMALS);

            if (data.getYesterday() != null) {
                cell(data.getYesterday().getStores(), NUMBER_NO_DECIMALS);
            } else {
                skipCell();
            }

            if (currentMonth != null) {
                cell(currentMonth.getStoresDayAvg(), NUMBER_DECIMALS);
                cell(currentMonth.getStores(), NUMBER_NO_DECIMALS);
            } else {
                skipCells(2);
            }

            for (AffiliateUserTrackingPerPeriod month : months) {
                cell(month.getStores(), NUMBER_NO_DECIMALS);
            }

            for (AffiliateUserTrackingPerYear year : years) {
                cell(year.getStoresDayAvg(), NUMBER_DECIMALS);
                cell(year.getStoresMonthAvg(), NUMBER_DECIMALS);
                cell(year.getStores(), NUMBER_NO_DECIMALS);
            }

            // Purchases #s
            nextRow();
            cell("Purchase #", NORMAL_STYLE);
            cell(data.getPurchases(), NUMBER_NO_DECIMALS);

            if (data.getYesterday() != null) {
                cell(data.getYesterday().getPurchases(), NUMBER_NO_DECIMALS);
            } else {
                skipCell();
            }

            if (currentMonth != null) {
                cell(currentMonth.getPurchasesDayAvg(), NUMBER_DECIMALS);
                cell(currentMonth.getPurchases(), NUMBER_NO_DECIMALS);
            } else {
                skipCells(2);
            }

            for (AffiliateUserTrackingPerPeriod month : months) {
                cell(month.getPurchases(), NUMBER_NO_DECIMALS);
            }

            for (AffiliateUserTrackingPerYear year : years) {
                cell(year.getPurchasesDayAvg(), NUMBER_DECIMALS);
                cell(year.getPurchasesMonthAvg(), NUMBER_DECIMALS);
                cell(year.getPurchases(), NUMBER_NO_DECIMALS);
            }

            // Purchases $s
            nextRow();
            cell("Purchase $", NORMAL_STYLE);
            cell(data.getSales(), NUMBER_DECIMALS);

            if (data.getYesterday() != null) {
                cell(data.getYesterday().getSales(), NUMBER_DECIMALS);
            } else {
                skipCell();
            }

            if (currentMonth != null) {
                cell(currentMonth.getSalesDayAvg(), NUMBER_DECIMALS);
                cell(currentMonth.getSales(), NUMBER_DECIMALS);
            } else {
                skipCells(2);
            }

            for (AffiliateUserTrackingPerPeriod month : months) {
                cell(month.getSales(), NUMBER_DECIMALS);
            }

            for (AffiliateUserTrackingPerYear year : years) {
                cell(year.getSalesDayAvg(), NUMBER_DECIMALS);
                cell(year.getSalesMonthAvg(), NUMBER_DECIMALS);
                cell(year.getSales(), NUMBER_DECIMALS);
            }

            // Quiz Total Start
            nextRow();
            quizStart("TOTAL");
            quizStart(DeviceType.NORMAL.toString());
            quizStart(DeviceType.MOBILE.toString());

            // Quiz Total Complete
            nextRow();
            quizComplete("TOTAL");
            quizComplete(DeviceType.NORMAL.toString());
            quizComplete(DeviceType.MOBILE.toString());

            // Quiz Total Complete Rate
            nextRow();
            quizRate("TOTAL");
            quizRate(DeviceType.NORMAL.toString());
            quizRate(DeviceType.MOBILE.toString());

            // Reg Total Start
            nextRow();
            nextRow();
            cell("Reg Total Start", NORMAL_STYLE);
            cell(data.getRegStartTotal(), NUMBER_NO_DECIMALS);

            if (data.getYesterday() != null) {
                cell(data.getYesterday().getRegStartTotal(), NUMBER_NO_DECIMALS);
            } else {
                skipCell();
            }

            if (currentMonth != null) {
                cell(currentMonth.getRegStartTotalDayAvg(), NUMBER_DECIMALS);
                cell(currentMonth.getRegStartTotal(), NUMBER_NO_DECIMALS);
            } else {
                skipCells(2);
            }

            for (AffiliateUserTrackingPerPeriod month : months) {
                cell(month.getRegStartTotal(), NUMBER_NO_DECIMALS);
            }

            for (AffiliateUserTrackingPerYear year : years) {
                cell(year.getRegStartTotalDayAvg(), NUMBER_DECIMALS);
                cell(year.getRegStartTotalMonthAvg(), NUMBER_DECIMALS);
                cell(year.getRegStartTotal(), NUMBER_NO_DECIMALS);
            }

            regStart(RegistrationType.WEBSITE.toString(), DeviceType.NORMAL.toString());
            regStart(RegistrationType.WEBSITE.toString(), DeviceType.MOBILE.toString());
            regStart(RegistrationType.FACEBOOK.toString(), DeviceType.NORMAL.toString());
            regStart(RegistrationType.FACEBOOK.toString(), DeviceType.MOBILE.toString());
            regStart(RegistrationType.API.toString(), DeviceType.NORMAL.toString());
            regStart(RegistrationType.API.toString(), DeviceType.MOBILE.toString());

            // Reg Total Complete
            nextRow();
            nextRow();
            cell("Reg Total Complete", NORMAL_STYLE);
            cell(data.getRegCompleteTotal(), NUMBER_NO_DECIMALS);

            if (data.getYesterday() != null) {
                cell(data.getYesterday().getRegCompleteTotal(), NUMBER_NO_DECIMALS);
            } else {
                skipCell();
            }

            if (currentMonth != null) {
                cell(currentMonth.getRegCompleteTotalDayAvg(), NUMBER_DECIMALS);
                cell(currentMonth.getRegCompleteTotal(), NUMBER_NO_DECIMALS);
            } else {
                skipCells(2);
            }

            for (AffiliateUserTrackingPerPeriod month : months) {
                cell(month.getRegCompleteTotal(), NUMBER_NO_DECIMALS);
            }

            for (AffiliateUserTrackingPerYear year : years) {
                cell(year.getRegCompleteTotalDayAvg(), NUMBER_DECIMALS);
                cell(year.getRegCompleteTotalMonthAvg(), NUMBER_DECIMALS);
                cell(year.getRegCompleteTotal(), NUMBER_NO_DECIMALS);
            }

            regComplete(RegistrationType.WEBSITE.toString(), DeviceType.NORMAL.toString());
            regComplete(RegistrationType.WEBSITE.toString(), DeviceType.MOBILE.toString());
            regComplete(RegistrationType.FACEBOOK.toString(), DeviceType.NORMAL.toString());
            regComplete(RegistrationType.FACEBOOK.toString(), DeviceType.MOBILE.toString());
            regComplete(RegistrationType.API.toString(), DeviceType.NORMAL.toString());
            regComplete(RegistrationType.API.toString(), DeviceType.MOBILE.toString());

            // Reg Total Rate
            nextRow();
            nextRow();
            cell("Reg Total Rate", NORMAL_STYLE);
            cell(data.getRegRateTotal(), PERCENT_DECIMALS);

            if (data.getYesterday() != null) {
                cell(data.getYesterday().getRegRateTotal(), PERCENT_DECIMALS);
            } else {
                skipCell();
            }

            if (currentMonth != null) {
                skipCell();
                cell(currentMonth.getRegRateTotal(), PERCENT_DECIMALS);
            } else {
                skipCells(2);
            }

            for (AffiliateUserTrackingPerPeriod month : months) {
                cell(month.getRegRateTotal(), PERCENT_DECIMALS);
            }

            for (AffiliateUserTrackingPerYear year : years) {
                skipCells(2);
                cell(year.getRegRateTotal(), PERCENT_DECIMALS);
            }

            regRate(RegistrationType.WEBSITE.toString(), DeviceType.NORMAL.toString());
            regRate(RegistrationType.WEBSITE.toString(), DeviceType.MOBILE.toString());
            regRate(RegistrationType.FACEBOOK.toString(), DeviceType.NORMAL.toString());
            regRate(RegistrationType.FACEBOOK.toString(), DeviceType.MOBILE.toString());
            regRate(RegistrationType.API.toString(), DeviceType.NORMAL.toString());
            regRate(RegistrationType.API.toString(), DeviceType.MOBILE.toString());
        } else {
            nextRow();
            cell("No data is available for this Affiliate", NORMAL_STYLE);
        }

        //freeze the first row
        sheet.createFreezePane(0, 3);
        sheet.setZoom(5, 6);
    }

    private void regRate(String regType, String deviceType) {
        nextRow();
        cell("Reg Completion Rate " + regType + " / " + deviceType, CELL_INDENTED_STYLE);
        if (data.getRegRate().get(regType) != null && data.getRegRate().get(regType).get(deviceType) != null) {
            cell(data.getRegRate().get(regType).get(deviceType), PERCENT_DECIMALS);
        } else {
            skipCell();
        }

        if (data.getYesterday() != null && data.getYesterday().getRegRate().get(regType) != null && data.getYesterday().getRegRate().get(regType).get(deviceType) != null) {
            cell(data.getYesterday().getRegRate().get(regType).get(deviceType), PERCENT_DECIMALS);
        } else {
            skipCell();
        }

        if (currentMonth != null && currentMonth.getRegRate().get(regType) != null) {
            skipCell();
            cell(currentMonth.getRegRate().get(regType).get(deviceType), PERCENT_DECIMALS);
        } else {
            skipCells(2);
        }

        for (AffiliateUserTrackingPerPeriod month : months) {
            if (month.getRegRate().get(regType) != null) {
                cell(month.getRegRate().get(regType).get(deviceType), PERCENT_DECIMALS);
            } else {
                skipCell();
            }
        }

        for (AffiliateUserTrackingPerYear year : years) {
            skipCells(2);
            if (year.getRegRate().get(regType) != null) {
                cell(year.getRegRate().get(regType).get(deviceType), PERCENT_DECIMALS);
            } else {
                skipCell();
            }
        }
    }

    private void regComplete(String regType, String deviceType) {
        nextRow();
        cell("Reg Start " + regType + " / " + deviceType, CELL_INDENTED_STYLE);
        if (data.getRegComplete().get(regType) != null && data.getRegComplete().get(regType).get(deviceType) != null) {
            cell(data.getRegComplete().get(regType).get(deviceType), NUMBER_NO_DECIMALS);
        } else {
            skipCell();
        }

        if (data.getYesterday() != null && data.getYesterday().getRegComplete().get(regType) != null && data.getYesterday().getRegComplete().get(regType).get(deviceType) != null) {
            cell(data.getYesterday().getRegComplete().get(regType).get(deviceType), NUMBER_NO_DECIMALS);
        } else {
            skipCell();
        }

        if (currentMonth != null && currentMonth.getRegComplete().get(regType) != null) {
            cell(currentMonth.getRegCompleteDayAvg().get(regType).get(deviceType), NUMBER_DECIMALS);
            cell(currentMonth.getRegComplete().get(regType).get(deviceType), NUMBER_NO_DECIMALS);
        } else {
            skipCells(2);
        }

        for (AffiliateUserTrackingPerPeriod month : months) {
            if (month.getRegComplete().get(regType) != null) {
                cell(month.getRegComplete().get(regType).get(deviceType), NUMBER_NO_DECIMALS);
            } else {
                skipCell();
            }
        }

        for (AffiliateUserTrackingPerYear year : years) {
            if (year.getRegComplete().get(regType) != null) {
                cell(year.getRegCompleteDayAvg().get(regType).get(deviceType), NUMBER_DECIMALS);
                cell(year.getRegCompleteMonthAvg().get(regType).get(deviceType), NUMBER_DECIMALS);
                cell(year.getRegComplete().get(regType).get(deviceType), NUMBER_NO_DECIMALS);
            } else {
                skipCells(3);
            }
        }
    }

    private void regStart(String regType, String deviceType) {
        nextRow();
        cell("Reg Start " + regType + " / " + deviceType, CELL_INDENTED_STYLE);
        if (data.getRegStart().get(regType) != null && data.getRegStart().get(regType).get(deviceType) != null) {
            cell(data.getRegStart().get(regType).get(deviceType), NUMBER_NO_DECIMALS);
        } else {
            skipCell();
        }

        if (data.getYesterday() != null && data.getYesterday().getRegStart().get(regType) != null && data.getYesterday().getRegStart().get(regType).get(deviceType) != null) {
            cell(data.getYesterday().getRegStart().get(regType).get(deviceType), NUMBER_NO_DECIMALS);
        } else {
            skipCell();
        }

        if (currentMonth != null && currentMonth.getRegStart().get(regType) != null) {
            cell(currentMonth.getRegStartDayAvg().get(regType).get(deviceType), NUMBER_DECIMALS);
            cell(currentMonth.getRegStart().get(regType).get(deviceType), NUMBER_NO_DECIMALS);
        } else {
            skipCells(2);
        }

        for (AffiliateUserTrackingPerPeriod month : months) {
            if (month.getRegStart().get(regType) != null) {
                cell(month.getRegStart().get(regType).get(deviceType), NUMBER_NO_DECIMALS);
            } else {
                skipCell();
            }
        }

        for (AffiliateUserTrackingPerYear year : years) {
            if (year.getRegStart().get(regType) != null) {
                cell(year.getRegStartDayAvg().get(regType).get(deviceType), NUMBER_DECIMALS);
                cell(year.getRegStartMonthAvg().get(regType).get(deviceType), NUMBER_DECIMALS);
                cell(year.getRegStart().get(regType).get(deviceType), NUMBER_NO_DECIMALS);
            } else {
                skipCells(3);
            }
        }
    }

    private void quizRate(String type) {
        nextRow();
        cell("Quiz Completion Rate " + type, "TOTAL".equals(type) ? NORMAL_STYLE : CELL_INDENTED_STYLE);
        if (data.getQuizRate().get(type) != null) {
            cell(data.getQuizRate().get(type), PERCENT_DECIMALS);
        } else {
            skipCell();
        }

        if (data.getYesterday() != null && data.getYesterday().getQuizRate().get(type) != null) {
            cell(data.getYesterday().getQuizRate().get(type), PERCENT_DECIMALS);
        } else {
            skipCell();
        }

        if (currentMonth != null) {
            skipCell();
            cell(currentMonth.getQuizRate().get(type), PERCENT_DECIMALS);
        } else {
            skipCells(2);
        }

        for (AffiliateUserTrackingPerPeriod month : months) {
            cell(month.getQuizRate().get(type), PERCENT_DECIMALS);
        }

        for (AffiliateUserTrackingPerYear year : years) {
            skipCells(2);
            cell(year.getQuizRate().get(type), PERCENT_DECIMALS);
        }
    }

    private void quizComplete(String type) {
        nextRow();
        cell("Quiz Total Complete " + type, "TOTAL".equals(type) ? NORMAL_STYLE : CELL_INDENTED_STYLE);
        if (data.getQuizComplete().get(type) != null) {
            cell(data.getQuizComplete().get(type), NUMBER_NO_DECIMALS);
        } else {
            skipCell();
        }

        if (data.getYesterday() != null && data.getYesterday().getQuizComplete().get(type) != null) {
            cell(data.getYesterday().getQuizComplete().get(type), NUMBER_NO_DECIMALS);
        } else {
            skipCell();
        }

        if (currentMonth != null && currentMonth.getQuizComplete().get(type) != null) {
            cell(currentMonth.getQuizCompleteDayAvg().get(type), NUMBER_DECIMALS);
            cell(currentMonth.getQuizComplete().get(type), NUMBER_NO_DECIMALS);
        } else {
            skipCells(2);
        }

        for (AffiliateUserTrackingPerPeriod month : months) {
            if (month.getQuizComplete().get(type) != null) {
                cell(month.getQuizComplete().get(type), NUMBER_NO_DECIMALS);
            } else {
                skipCell();
            }
        }

        for (AffiliateUserTrackingPerYear year : years) {
            if (year.getQuizComplete().get(type) != null) {
                cell(year.getQuizCompleteDayAvg().get(type), NUMBER_DECIMALS);
                cell(year.getQuizCompleteMonthAvg().get(type), NUMBER_DECIMALS);
                cell(year.getQuizComplete().get(type), NUMBER_NO_DECIMALS);
            } else {
                skipCells(3);
            }
        }
    }

    private void quizStart(String type) {
        nextRow();
        cell("Quiz Start " + type, "TOTAL".equals(type) ? NORMAL_STYLE : CELL_INDENTED_STYLE);
        if (data.getQuizStart().get(type) != null) {
            cell(data.getQuizStart().get(type), NUMBER_NO_DECIMALS);
        } else {
            skipCell();
        }

        if (data.getYesterday() != null && data.getYesterday().getQuizStart().get(type) != null) {
            cell(data.getYesterday().getQuizStart().get(type), NUMBER_NO_DECIMALS);
        } else {
            skipCell();
        }

        if (currentMonth != null && currentMonth.getQuizStart().get(type) != null) {
            cell(currentMonth.getQuizStartDayAvg().get(type), NUMBER_DECIMALS);
            cell(currentMonth.getQuizStart().get(type), NUMBER_NO_DECIMALS);
        } else {
            skipCells(2);
        }

        for (AffiliateUserTrackingPerPeriod month : months) {
            if (month.getQuizStart().get(type) != null) {
                cell(month.getQuizStart().get(type), NUMBER_NO_DECIMALS);
            } else {
                skipCell();
            }
        }

        for (AffiliateUserTrackingPerYear year : years) {
            if (year.getQuizStart().get(type) != null) {
                cell(year.getQuizStartDayAvg().get(type), NUMBER_DECIMALS);
                cell(year.getQuizStartMonthAvg().get(type), NUMBER_DECIMALS);
                cell(year.getQuizStart().get(type), NUMBER_NO_DECIMALS);
            } else {
                skipCells(3);
            }
        }
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