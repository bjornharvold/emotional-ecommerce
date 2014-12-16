/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.comparator;

import com.lela.domain.document.FunctionalFilterOption;

import java.util.Comparator;

/**
 * Created by Bjorn Harvold
 * Date: 10/10/11
 * Time: 10:03 PM
 * Responsibility:
 */
public class FunctionalFilterOptionComparator implements Comparator<FunctionalFilterOption> {

    /**
     * Method description
     *
     * @param ta1 ta1
     * @param ta2 ta2
     * @return Return value
     */
    @Override
    public int compare(FunctionalFilterOption ta1, FunctionalFilterOption ta2) {
        int result = 0;

        // we either compare on the order attribute and if it doesn't exist
        // we sort on the option value
        if ((ta1.getRdr() != null) && (ta2.getRdr() != null)) {
            if (ta1.getRdr() > ta2.getRdr()) {
                result = 1;
            } else if (ta1.getRdr() < ta2.getRdr()) {
                result = -1;
            }
        } else if (ta1.getVl() != null && ta2.getVl() != null &&
                ta1.getVl() instanceof String && ta2.getVl() instanceof String) {
            // sort on case insensitive
            String v1 = ((String) ta1.getVl()).toLowerCase();
            String v2 = ((String) ta2.getVl()).toLowerCase();

            result = v1.compareTo(v2);
        }

        return result;
    }
}