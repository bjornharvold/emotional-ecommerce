/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.comparator;

import com.lela.domain.document.AvailableInStore;
import com.lela.domain.document.User;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Chris Tallent
 * Date: 1/17/12
 * Time: 3:36 PM
 */
public class AvailableInStoreByPriceAndNameComparator implements Comparator<AvailableInStore> {

    private final static ItemListPriceLowHighComparator listPriceComparator = new ItemListPriceLowHighComparator();
    private final static StoreByNameComparator storeByNameComparator = new StoreByNameComparator();

    @Override
    public int compare(AvailableInStore lhs, AvailableInStore rhs) {
        // first sort on price
        int result = listPriceComparator.compare(lhs.getTms().get(0), rhs.getTms().get(0));

        if (result == 0) {
            // if price is same, sort by store alphabetically
            result = storeByNameComparator.compare(lhs, rhs);
        }

        return result;
    }
}
