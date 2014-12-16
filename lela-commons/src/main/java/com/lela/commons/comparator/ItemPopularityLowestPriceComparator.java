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
public class ItemPopularityLowestPriceComparator implements Comparator {

    /**
     * Sorts mainly on Motivator B. If B is identical, we search on lowest price
     *
     * @param o1 o1
     * @param o2 o2
     * @return Return value
     */
    @Override
    public int compare(Object o1, Object o2) {
        int result = 0;

        if (o1 instanceof AbstractItem && o2 instanceof AbstractItem) {
            AbstractItem a1 = (AbstractItem) o1;
            AbstractItem a2 = (AbstractItem) o2;
            Integer item1MotivatorB = a1.getMtvtrs().get("B");
            Integer item2MotivatorB = a2.getMtvtrs().get("B");

            if (item2MotivatorB != null){
            	result = item2MotivatorB.compareTo(item1MotivatorB);
            }

            if (result == 0) {
                // Get price for item #1
                Double lp1 = NumberUtils.safeDouble(a1.getSubAttributes().get("LowestPrice"));
                if (lp1 == null || lp1 <= 0) {
                    lp1 = NumberUtils.safeDouble(a1.getSubAttributes().get("ListPrice"));
                }

                // Get price for item #2
                Double lp2 = NumberUtils.safeDouble(a2.getSubAttributes().get("LowestPrice"));
                if (lp2 == null || lp2 <= 0) {
                    lp2 = NumberUtils.safeDouble(a2.getSubAttributes().get("ListPrice"));
                }

                if (lp1 != null && lp2 != null) {
                    result = lp1.compareTo(lp2);
                }
            }
        }

        return result;
    }
}
