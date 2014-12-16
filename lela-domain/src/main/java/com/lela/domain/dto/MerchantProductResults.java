/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import com.lela.domain.document.AvailableInStore;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 9/13/11
 * Time: 11:41 AM
 * Responsibility:
 */
public class MerchantProductResults {
    private List<AvailableInStore> stores;

    public List<AvailableInStore> getStores() {
        return stores;
    }

    public void addStores(List<AvailableInStore> stores) {
        if (this.stores == null) {
            this.stores = stores;
        } else {
            this.stores.addAll(stores);
        }
    }
}
