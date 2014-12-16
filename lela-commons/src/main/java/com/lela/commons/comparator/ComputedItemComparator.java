/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.comparator;

import com.lela.domain.document.ComputedItem;

import java.util.Comparator;

/**
 * Created by Bjorn Harvold
 * Date: 10/10/11
 * Time: 9:53 PM
 * Responsibility:
 */
public class ComputedItemComparator implements Comparator<ComputedItem> {
    @Override
    public int compare(ComputedItem ci1, ComputedItem ci2) {
        int result = 0;

        if (ci1.getTtlrlvncy() > ci2.getTtlrlvncy()) {
            result = -1;
        } else if (ci1.getTtlrlvncy() < ci2.getTtlrlvncy()) {
            result = 1;
        }

        return result;
    }
}
