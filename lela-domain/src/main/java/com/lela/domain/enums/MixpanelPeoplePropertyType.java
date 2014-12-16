/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.domain.enums;

/**
 * User: Chris Tallent
 * Date: 10/22/12
 * Time: 1:07 PM
 */
public enum MixpanelPeoplePropertyType {

    /* SPECIAL PROPERTIES */

    /** This distinct ID must match that used by the events in Engagements **/
    DISTINCT_ID("$people_distinct_id"),

    /** The user's two-letter ISO country code. If not explicitly set, this value is set automatically using the user's IP address. **/
    COUNTRY_CODE("$country_code"),

    /**
     * The date the user signed up.
     *      mixpanel.people.set({ $created: new Date(2012, 6, 11, 12, 30, 0) });
     */
    CREATED("$created"),

    /**
     * This property is used whenever you send emails to your users from Mixpanel.
     *      mixpanel.people.set({ $email: 'jsmith@email.com' });
     */
    EMAIL("$email"),

    /**
     * The username of a given user.
     *      mixpanel.people.set({ $username: 'JSmith' });
     */
    USERNAME("$username"),

    /**
     * mixpanel.people.set({ $first_name: 'John' });
     */
    FIRST_NAME("$first_name"),

    /**
     * mixpanel.people.set({ $last_name: 'Smith' });
     */
    LAST_NAME("$last_name"),

    /**
     * Can be used as an alternative to $first_name and $last_name.
     *      mixpanel.people.set({ $name: 'John Smith' });
     */
    NAME("$name"),

    /**
     * The last time the user logged in.
     * mixpanel.people.set({ $last_login: new Date() });
     */
    LAST_LOGIN("$last_login"),

    /** The time any properties were last set on a user record. */
    LAST_SEEN("$last_seen"),

    /** List of user's Apple Push Notification service device tokens for iOS push. Our iOS client library has methods to manage this property for you. */
    IOS_DEVICES("$ios_devices"),

    /** List of user's Google Cloud Messaging registration IDs for Android push. Our Android client library has methods to manage this property for you. */
    ANDROID_DEVICES("$android_devices"),

    //
    // LELA CUSTOM PROPERTIES
    //

    SOURCE_TYPE("Source Type"),

    AFFILIATE_ID("Affiliate ID"),

    CAMPAIGN_ID("Campaign ID"),

    AFFILIATE_REFERRER_ID("Affiliate Referrer ID"),

    CITY("Address City"),

    STATE("Address State"),

    ZIPCODE("Address Zipcode"),

    GENDER("Gender"),

    FULL_NAME("Full Name"),

    MOTIVATOR_SOURCE("Motivator Source"),

    DUE_DATE("Due Date"),

    RELATIONSHIP_STATUS("Relationship Status"),

    QUIZ_AFFILIATE_ID("Quiz Affiliate ID"),

    QUIZ_APPLICATION_ID("Quiz Application ID"),

    QUIZ_ID("Quiz ID"),

    DELETED_USER("Deleted User"),

    EMAIL_OPT_IN("Email Opt In"),

    FILTER_USAGE_TOTAL("Filter Usage Total"),

    SORT_USAGE_TOTAL("Sort Usage Total"),

    USER_VISIT_TOTAL("User Visit Total"),

    PAGE_VIEW_TOTAL("Page View Total"),

    LAST_CATEGORY_ID("Last Category ID"),

    LAST_ITEM_ID("Last Item ID"),

    RANDOM("Random"),

    EMAIL_DOMAIN("Email Domain"),

    AGE("Age"),

    PURCHASED_ITEMS("Purchased Items"),

    SALES_TOTAL("Sales Total"),

    COMMISSION_TOTAL("Commission Total"),

    DOMAIN("Domain"),

    DOMAIN_AFFILIATE_ID("Domain Affiliate ID");

    private String value;

    private MixpanelPeoplePropertyType(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return this.value;
    }
}
