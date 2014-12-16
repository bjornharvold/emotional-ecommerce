/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.domain.enums;

/**
 * User: Chris Tallent
 * Date: 10/23/12
 * Time: 1:03 PM
 */
public enum MixpanelEventType {

    /* Special Event Types */

    /** User registration */
    SIGNUP("$signup"),

    /** Track a page view ... used with RequestUtils.getPageviewInfo() */
    PAGE_VIEW("mp_page_view"),

    /* LELA Custom Events */

    VISIT("User Visit"),

    LOGIN("Login"),

    MOTIVATORS("Motivators"),

    QUIZ_START("Quiz Start"),

    QUIZ_FINISH("Quiz Finish"),

    QUIZ_STEP_FINISH("Quiz Step Finish"),

    QUIZ_ANSWERS("Quiz Answers"),

    VIEWED_CATEGORY("Viewed Category"),

    VIEWED_DEPARTMENT("Viewed Department"),

    VIEWED_ITEM("Viewed Item"),

    COMPARED_ITEMS("Compared Items"),

    DELETE_USER("Delete User"),

    SUBSCRIBE("Subscribe"),

    UNSUBSCRIBE("Unsubscribe"),

    FILTERED_RESULTS("Filtered Results"),

    SORTED_RESULTS("Sorted Results"),

    PURCHASE("Purchase"),

    RETURN("Return"),

    EVENT_PARTICIPANT("Event Participant"),

    LELA_LIST("Lela List");

    private String value;
    private MixpanelEventType(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return this.value;
    }
}
