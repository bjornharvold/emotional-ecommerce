/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.comparator;

import com.lela.domain.document.ListCard;
import com.lela.domain.enums.list.ListCardType;
import com.lela.util.utilities.number.NumberUtils;

import java.util.Comparator;

/**
 * Created by Bjorn Harvold
 * Date: 9/5/12
 * Time: 11:51 AM
 * Responsibility:
 */
public class ListCardItemSortOnHighestPriceComparator implements Comparator<ListCard> {
    @Override
    public int compare(ListCard lc1, ListCard lc2) {
        int result = 0;

        if (lc1 != null && lc2 != null) {
            if (lc1.getTp().equals(ListCardType.ITEM) && lc2.getTp().equals(ListCardType.ITEM)) {
                Double lc1Price = null;
                Double lc2Price = null;

                if (lc1.getItem() != null) {
                    lc1Price = NumberUtils.safeDouble(lc1.getItem().getAttributes().get("LowestPrice"));
                } else if (lc1.getRelevantItem() != null) {
                    lc1Price = NumberUtils.safeDouble(lc1.getRelevantItem().getAttributes().get("LowestPrice"));
                }
                
                if (lc2.getItem() != null) {
                    lc2Price = NumberUtils.safeDouble(lc2.getItem().getAttributes().get("LowestPrice"));
                } else if (lc2.getRelevantItem() != null) {
                    lc2Price = NumberUtils.safeDouble(lc2.getRelevantItem().getAttributes().get("LowestPrice"));
                }

                if (lc1Price != null && lc2Price != null) {
                    result = lc2Price.compareTo(lc1Price);
                }
            }
        }

        return result;
    }
}
