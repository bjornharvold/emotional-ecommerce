package com.lela.data.enums;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 11/26/12
 * Time: 4:51 PM
 * To change this template use File | Settings | File Templates.
 */
public enum ItemHistoryReportType {
    ADDED_TO_PRODUCTION("Added"),
    DROPPED_FROM_PRODUCTION("Dropped"),
    CUSTOM("Custom");

    String label = "";

    private ItemHistoryReportType(String label)
    {
      this.label = label;
    }

    public String getLabel()
    {
        return this.label;
    }
}
