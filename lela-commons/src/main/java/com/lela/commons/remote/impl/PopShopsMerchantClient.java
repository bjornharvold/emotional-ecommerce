/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.remote.impl;

import com.lela.commons.comparator.ItemListPriceLowHighComparator;
import com.lela.commons.remote.MerchantClient;
import com.lela.commons.remote.MerchantException;
import com.lela.domain.document.Attribute;
import com.lela.domain.document.AvailableInStore;
import com.lela.domain.document.Deal;
import com.lela.domain.document.Item;
import com.lela.domain.document.Merchant;
import com.lela.domain.document.Redirect;
import com.lela.domain.document.User;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestOperations;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 9/13/11
 * Time: 11:21 AM
 * Responsibility:
 */
public class PopShopsMerchantClient extends SubscriberIdMerchantClient {
    private final static Logger log = LoggerFactory.getLogger(PopShopsMerchantClient.class);

    public PopShopsMerchantClient() { }

    /**
     * For Popshops we will attach a sub-id to the url.  The sub-id will be used to track this
     * affiliate redirect and account for any affiliate network sales
     * <p/>
     * Format:
     * <p/>
     * MMDDYYHHmm-LELAProductIDXXXXXXXXXXX-ReferralClickIdXXXXXXXXX
     * <p/>
     * Where the components of the format are:
     * <p/>
     * MM - Month
     * DD - Day
     * YY - Year
     * HH - 24 Hour format UTC
     * mm - Minutes
     * LELAProductIDXXXXXXXXXXX - Lela 24 character length unique product identifier (effectively the Lela SKU)
     * ReferralClickIdXXXXXXXXX - Lela 24 character length unique click identifier so that we can find the user and other click parameters
     *
     * @param baseUrl
     * @param redirect
     * @param item
     * @return
     */
    @Override
    public String constructTrackableUrl(String baseUrl, ObjectId userId, Redirect redirect, Item item) {

        // Popshops SUBID
        String subid = buildSubscriberId(redirect, item);

        if (baseUrl != null && baseUrl.length() > 0) {
            baseUrl += "/" + subid;
        }

        return baseUrl;
    }



}
