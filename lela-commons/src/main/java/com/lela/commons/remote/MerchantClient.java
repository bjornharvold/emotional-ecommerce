/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.remote;

import com.lela.domain.document.AvailableInStore;
import com.lela.domain.document.Item;
import com.lela.domain.document.Redirect;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 9/13/11
 * Time: 12:15 PM
 * Responsibility:
 */
public interface MerchantClient {
    String constructTrackableUrl(String baseUrl, ObjectId userId, Redirect redirect, Item item);
}
