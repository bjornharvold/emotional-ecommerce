/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.comparator;

import com.lela.domain.document.FunctionalFilter;

import java.util.Comparator;

/**
 * Created by Bjorn Harvold
 * Date: 10/10/11
 * Time: 10:04 PM
 * Responsibility:
 */
public class FunctionalFilterComparator implements Comparator<FunctionalFilter> {

    /**
     * Method description
     *
     * @param tq1 tq1
     * @param tq2 tq2
     * @return Return value
     */
    @Override
    public int compare(FunctionalFilter tq1, FunctionalFilter tq2) {
        int result = 0;

        if (tq1.getRdr() > tq2.getRdr()) {
            result = 1;
        } else if (tq1.getRdr() < tq2.getRdr()) {
            result = -1;
        }

        return result;
    }
}
