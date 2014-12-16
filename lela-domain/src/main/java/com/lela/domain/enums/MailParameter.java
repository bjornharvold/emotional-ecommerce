/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.enums;

/**
 * Created by IntelliJ IDEA.
 * User: Chris Tallent
 * Date: 10/25/11
 * Time: 9:24 AM
 * To change this template use File | Settings | File Templates.
 */
public enum MailParameter {

    BASE_URL("baseUrl"),
    SUBJECT("subject"),
    TO("to"),
    USER_EMAIL("userEmail"),
    USER_FIRST_NAME("userFirstName"),
    USER_LAST_NAME("userLastName"),
    USER_ATTRIBUTES("userAttributes"),
    PASSWORD("password"),
    INVITATION_COUNT("invitationCount"),
    INVITATION_URL("invitationUrl"),
    BLOG("blog"),
    EVENT_NAME("eventName"),
    URL_NAME("urlName"),
    START_DATE("startDate"),
    END_DATE("endDate"),
    COUPON_CODE("couponCode"),
    COUPON_FIRST_NAME("couponFirstName"),
    COUPON_LAST_NAME("couponLastName"),
    COUPON_REQUEST_DATE("couponRequestDate"),
    COUPON_REDEEM_DATE("couponRedeemDate"),
    COUPON_ITEM("couponItemName"),
    BRANCH_URL_NAME("branchUrlName"),
    BRANCH_NAME("branchName"),
    BRANCH_ADDRESS("branchAddress"),
    BRANCH_CITY("branchCity"),
    BRANCH_STATE("branchState"),
    BRANCH_ZIP("branchZip"),
    OFFER_URL_NAME("offerUrlName"),
    OFFER_VALUE_TERM("offerValueTerm"),
    OFFER_EXPIRATION_DATE("offerExpirationDate"),
    OFFER_DESCRIPTION("offerDescription"),
    VERIFICATION_TOKEN("verificationToken"),
    ITEM("item"),
    STORES("stores"),
    PRICE_ALERT("priceAlert"),
    MAILCHIMP_LIST_ID("mailChimpListId"),
    NEWSLETTER_OPTIN_FLAG("newsletterOptinFlag"),
    ITEM_RELEVANCY("itemRelevancy"),
    USER_SUPPLEMENT("us");

    private String templateKey;

    MailParameter(String templateKey) {
        this.templateKey = templateKey;
    }

    @Override
    public String toString() {
        return templateKey;
    }
}