/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.comparator;

import com.lela.domain.document.AbstractItem;

import java.util.Comparator;

/**
 * Created by Bjorn Harvold
 * Date: 10/10/11
 * Time: 9:54 PM
 * Responsibility:
 */
public class ItemOwnerComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        int result = 0;

        if (o1 instanceof AbstractItem && o2 instanceof AbstractItem) {
            AbstractItem i1 = (AbstractItem) o1;
            AbstractItem i2 = (AbstractItem) o2;

            String lp1 = i1.getWnr().getNm();
            String lp2 = i2.getWnr().getNm();

            if (lp1 != null && lp2 != null) {
                result = lp2.compareTo(lp1);
            }
        }

        return result;
    }
}
