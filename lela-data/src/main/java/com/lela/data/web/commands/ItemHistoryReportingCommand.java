package com.lela.data.web.commands;

import com.lela.data.enums.ItemHistoryReportType;
import com.lela.data.enums.ItemStatuses;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 11/14/12
 * Time: 10:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class ItemHistoryReportingCommand {

    @NotNull
    private ItemHistoryReportType reportType;

    @NotNull
    private Date from;

    @NotNull
    private Date to;

    private Long categoryId;

    private ItemStatuses fromItemStatus;

    private ItemStatuses toItemStatus;

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public ItemStatuses getFromItemStatus() {
        return fromItemStatus;
    }

    public void setFromItemStatus(ItemStatuses fromItemStatus) {
        this.fromItemStatus = fromItemStatus;
    }

    public ItemStatuses getToItemStatus() {
        return toItemStatus;
    }

    public void setToItemStatus(ItemStatuses toItemStatus) {
        this.toItemStatus = toItemStatus;
    }

    public ItemHistoryReportType getReportType() {
        return reportType;
    }

    public void setReportType(ItemHistoryReportType reportType) {
        this.reportType = reportType;
    }
}
