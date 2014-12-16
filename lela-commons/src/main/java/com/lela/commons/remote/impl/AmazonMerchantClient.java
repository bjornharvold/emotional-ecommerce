/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.remote.impl;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lela.commons.remote.MerchantClient;
import com.lela.domain.document.Item;
import com.lela.domain.document.Redirect;

/**
 * Created by Bjorn Harvold
 * Date: 9/19/11
 * Time: 11:42 AM
 * Responsibility:
 */
public class AmazonMerchantClient extends SubscriberIdMerchantClient implements MerchantClient {
    private final static Logger log = LoggerFactory.getLogger(AmazonMerchantClient.class);
    public static final String MERCHANT_ID = "Amazon";

    private final String affiliateRedirectUrl;

    private final String affiliateId;

    private final Boolean enabled;

    private final Boolean allUsers;

    public AmazonMerchantClient(String affiliateId, String affiliateRedirectUrl, Boolean enabled, Boolean allUsers) {
        this.affiliateId = affiliateId;
        this.affiliateRedirectUrl = affiliateRedirectUrl;
        this.enabled = enabled;
        this.allUsers = allUsers;
    }

    @Override
    public String constructTrackableUrl(String baseUrl, ObjectId userId, Redirect redirect, Item item) {

        String subId = this.buildSubscriberId(redirect, item);

        //replace the affiliate id within the url with our base affiliate id
        if (StringUtils.isNotBlank(baseUrl)) {
            baseUrl = StringUtils.replace(baseUrl, "%26tag%3Dws%26","%26tag%3D" + affiliateId + "%26");
        }

        //if the redirect is associated with a user send them to affiliate reporting
        //to get a tracking id
        if(this.enabled && StringUtils.isNotBlank(baseUrl) && ( userId != null || allUsers) )
        {
           baseUrl = buildAffiliateReportingUrl(baseUrl, subId, userId != null);
        }


        return baseUrl;
    }

    /**
     * Modifies the url to send the user to Affiliate Reporting
     * @param baseUrl
     * @param subId
     * @return
     */
    public String buildAffiliateReportingUrl(String baseUrl, String subId, Boolean isAuthenticated)
    {
        return String.format(affiliateRedirectUrl, subId, isAuthenticated, baseUrl);
    }
}
