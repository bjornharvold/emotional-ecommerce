/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.comparator;

import com.lela.domain.document.FunctionalFilterOption;
import com.lela.util.utilities.number.NumberUtils;

import java.util.Comparator;

/**
 * Created by Bjorn Harvold
 * Date: 10/10/11
 * Time: 10:05 PM
 * Responsibility:
 */
public class DynamicRangeTunerAnswerComparator implements Comparator<FunctionalFilterOption> {

    /**
     * Sorts the list from high to low based on the expected number values in FunctionalFilterOption
     *
     * @param ta1 ta1
     * @param ta2 ta2
     * @return Return value
     */
    @Override
    public int compare(FunctionalFilterOption ta1, FunctionalFilterOption ta2) {
        int result = 0;

        if ((ta1.getVl() != null) && (ta2.getVl() != null)) {
            Double v1 = NumberUtils.safeDouble(ta1.getVl());
            Double v2 = NumberUtils.safeDouble(ta2.getVl());

            if (v1.compareTo(v2) > 0) {
                result = 1;
            } else if (v1.compareTo(v2) < 0) {
                result = -1;
            }
        }

        return result;
    }
}
