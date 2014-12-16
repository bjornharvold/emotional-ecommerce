package com.lela.commons.remote.impl;

import com.lela.commons.remote.MerchantClient;
import com.lela.domain.document.Item;
import com.lela.domain.document.Redirect;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 5/23/12
 * Time: 1:49 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class SubscriberIdMerchantClient implements MerchantClient {
    private final static DateFormat SUBID_DATE_FORMAT = new SimpleDateFormat("MMddyyHHmm");

    protected String buildSubscriberId(Redirect redirect, Item item) {
        return String.format("%s-%s-%s", SUBID_DATE_FORMAT.format(redirect.getRdrctdt()), item.getId(), redirect.getId());
    }
}
