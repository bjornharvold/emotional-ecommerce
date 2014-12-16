/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.domain.enums;

/**
 * User: Chris Tallent
 * Date: 10/22/12
 * Time: 1:07 PM
 */
public enum MixpanelEngagementPropertyType {

    //
    // SPECIAL PROPERTIES
    //

    /** A way to uniquely identify your users for things like funnels. You can set this to any value or use the Javascript call mpq.identify("Username here"); */
    DISTINCT_ID("distinct_id"),

    /** the ip address of the client. If distinct_id is not present, this will be used for uniqueness. **/
    IP("ip"),

    /** This is used to give your users real names in the streams feed so you can watch users more easily. */
    STREAMS_NAME("mp_name_tag"),

    /** This is the only property available in streams. It is not available in other reports. */
    STREAMS_NOTE("mp_note"),

    /** The token used to associate data you send Mixpanel to your account and project. */
    TOKEN("token"),

    //
    // SPECIAL PROPERTIES ... Automatically filled in if using Javascript client
    //

    /** A unix time epoch that is used to determine the time of an event. */
    TIMESTAMP("time"),

    /** The first referrer the user came from ever. */
    INITIAL_REFERRER("$initial_referrer"),

    /** The first referring domain the user came from ever. */
    INITIAL_REFERRING_DOMAIN("$initial_referring_domain"),

    /** The search engine a user came from. */
    SEARCH_ENGINE("$search_engine"),

    /** The search keyword the user used to get to your website. */
    SEARCH_KEYWORD("mp_keyword"),

    /** The operating system of the user. */
    OPERATING_SYSTEM("$os"),

    /** The browser of the user. */
    BROWSER("$browser"),

    /** The current referrer of the user. */
    REFERRER("$referrer"),

    /** The current referring domain of the user. */
    REFERRING_DOMAIN("$referring_domain"),

    /** A two letter country code representing the geolocation of the user. */
    COUNTRY_CODE("mp_country_code"),

    //
    // LELA CUSTOM PROPERTIES
    //

    SOURCE_TYPE("Source Type"),

    AFFILIATE_ID("Affiliate ID"),

    CAMPAIGN_ID("Campaign ID"),

    AFFILIATE_REFERRER_ID("Affiliate Referrer ID"),

    CITY("City"),

    STATE("State"),

    GENDER("Gender"),

    FIRST_NAME("First Name"),

    LAST_NAME("Last Name"),

    FULL_NAME("Full Name"),

    EMAIL("Email"),

    DUE_DATE("Due Date"),

    RELATIONSHIP_STATUS("Relationship Status"),

    REGISTRATION_TYPE("Registration Type"),

    AUTHENTICATION_TYPE("Login Type"),

    MOTIVATOR_SOURCE("Motivator Source"),

    APPLICATION_ID("API Application ID"),

    QUIZ_AFFILIATE_ID("Quiz Affiliate ID"),

    QUIZ_ID("Quiz ID"),

    QUIZ_STEP_ID("Quiz Step ID"),

    CATEGORY_ID("Category ID"),

    DEPARTMENT_ID("Department ID"),

    DEVICE_TYPE("Device Type"),

    ITEM_ID("Item ID"),

    ITEM_VIEW_TYPE("Item View Type"),

    COMPARED_ITEMS("Compared Items"),

    DELETED_USER("Deleted User"),

    EMAIL_LIST_ID("Email List ID"),

    FILTER("Filter"),

    FILTER_OPTION("Filter Option"),

    FILTER_OPTION_VALUE("Filter Option Value"),

    SORT("Sort"),

    PRODUCT_CATEGORY("Product Category"),

    PRODUCT_NAME("Product Name"),

    PRODUCT_ID("Product ID"),

    ADVERTISER("Advertiser"),

    NETWORK("Network"),

    SALES_AMOUNT("Sales Amount"),

    COMMISSION_AMOUNT("Commission Amount"),

    QUANTITY("Quantity"),

    EVENT_ID("Event ID"),

    EVENT_AFFILIATE_ID("Event Affiliate ID"),

    EVENT_CAMPAIGN_ID("Event Campaign ID"),

    COMPATIBILITY_SCORE("Compatibility Score"),

    LELA_LIST_ACTION("Action"),

    USER_AGENT("User-Agent"),

    OBJECT_ID("Object ID"),

    IP_ADDRESS("IP Address"),

    DOMAIN("Domain"),

    DOMAIN_AFFILIATE_ID("Domain Affiliate ID"),

    VISIT_AFFILIATE_ID("Visit Affiliate ID"),

    VISIT_CAMPAIGN_ID("Visit Campaign ID");


    private String value;

    private MixpanelEngagementPropertyType(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return this.value;
    }
}
