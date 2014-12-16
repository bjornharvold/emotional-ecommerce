/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.domain.document.AbstractItem;
import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.AvailableInStore;
import com.lela.domain.document.Item;
import com.lela.domain.document.Redirect;
import com.lela.domain.document.User;
import com.lela.domain.dto.BranchSearchResults;
import com.lela.domain.dto.LocationQuery;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 9/13/11
 * Time: 9:02 PM
 * Responsibility:
 */
public interface MerchantService {
    //Item updateAvailableStores(Item item);
    List<AvailableInStore> findOnlineStores(AbstractItem item);
    BranchSearchResults findLocalStoresForItem(AbstractItem item, LocationQuery locationQuery, List<AvailableInStore> onlineResults);
    Redirect constructRedirect(String itemUrlName, String merchantId, ObjectId userId);
    Boolean hasStoreDataExpired(Item item);

    Redirect constructAddToCartRedirect(AffiliateAccount domainAffiliate, String itemUrlName, String merchantId, ObjectId userId);
}
