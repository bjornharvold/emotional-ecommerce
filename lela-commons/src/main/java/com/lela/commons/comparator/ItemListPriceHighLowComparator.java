/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.comparator;

import com.lela.domain.document.AbstractItem;
import com.lela.util.utilities.number.NumberUtils;

import java.util.Comparator;

/**
 * Created by Bjorn Harvold
 * Date: 10/10/11
 * Time: 9:54 PM
 * Responsibility:
 */
public class ItemListPriceHighLowComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        int result = 0;

        if (o1 instanceof AbstractItem && o2 instanceof AbstractItem) {
            AbstractItem i1 = (AbstractItem) o1;
            AbstractItem i2 = (AbstractItem) o2;

            Double lp1 = NumberUtils.safeDouble(i1.getSubAttributes().get("ListPrice"));
            Double lp2 = NumberUtils.safeDouble(i2.getSubAttributes().get("ListPrice"));

            if (lp1 != null && lp2 != null) {
                result = lp2.compareTo(lp1);
            }
        }

        return result;
    }
}
