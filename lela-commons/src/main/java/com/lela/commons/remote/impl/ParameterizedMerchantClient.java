/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.remote.impl;

import com.lela.commons.comparator.ItemListPriceLowHighComparator;
import com.lela.commons.remote.MerchantClient;
import com.lela.commons.remote.MerchantException;
import com.lela.domain.document.*;
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
import java.util.*;

/**
 * Created by Bjorn Harvold
 * Date: 9/13/11
 * Time: 11:21 AM
 * Responsibility:
 */
public class ParameterizedMerchantClient extends SubscriberIdMerchantClient{
    private final static Logger log = LoggerFactory.getLogger(ParameterizedMerchantClient.class);

    private final static DateFormat SUBID_DATE_FORMAT = new SimpleDateFormat("MMddyyHHmm");

    private final String parameterName;

    public ParameterizedMerchantClient(String parameterName) {
        this.parameterName = parameterName;
    }

    /**
     * For CJ, LS and GAN we will attach a sub-id to the url.  The sub-id will be used to track this
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

        //if the url contains a question mark add the sid in after it with an & for other parameters
        if (baseUrl != null && baseUrl.length() > 0 && StringUtils.contains(baseUrl, '?')) {
            String endingCharacter = "";
            //check to see if the url has other parameters
            if (StringUtils.contains(baseUrl, '='))
            {
                endingCharacter = "&";
            }
            baseUrl = StringUtils.replace(baseUrl, "?", "?" + parameterName + "=" + subid + endingCharacter);
        }
        //otherwise add the value to the end
        else if (baseUrl != null && baseUrl.length() > 0)
        {
            baseUrl = baseUrl + "?" + parameterName + "=" + subid;
        }

        return baseUrl;
    }

}
