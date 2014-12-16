/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.comparator;

import com.lela.domain.document.ListCard;
import com.lela.domain.enums.list.ListCardType;

import java.util.Comparator;

/**
 * Created by Bjorn Harvold
 * Date: 9/5/12
 * Time: 11:51 AM
 * Responsibility:
 */
public class ListCardSortOnAlertDateComparator implements Comparator<ListCard>{
    @Override
    public int compare(ListCard lc1, ListCard lc2) {
        int result = 0;

        if (lc1 != null && lc2 != null) {

        }
        // TBD

        return result;
    }
}
