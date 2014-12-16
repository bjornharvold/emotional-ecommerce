/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.comparator;

import com.lela.domain.document.ListCard;

import java.util.Comparator;

/**
 * Created by Bjorn Harvold
 * Date: 9/5/12
 * Time: 11:51 AM
 * Responsibility:
 */
public class ListCardSortOnDateComparator implements Comparator<ListCard> {

    @Override
    public int compare(ListCard lc1, ListCard lc2) {
        int result = 0;

        if (lc1 != null && lc2 != null) {
            if (lc1.getDt() != null && lc2.getDt() == null) {
                result = 1;
            } else if (lc1.getDt() == null && lc2.getDt() != null) {
                result = -1;
            } else if (lc1.getDt() != null && lc2.getDt() != null) {
                if (lc1.getDt().before(lc2.getDt())) {
                    result = 1;
                } else {
                    result = -1;
                }
            }
        }

        return result;
    }

}
