/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.comparator;

import com.lela.domain.document.QuizStepEntry;

import java.util.Comparator;

/**
 * Created by Bjorn Harvold
 * Date: 10/10/11
 * Time: 9:56 PM
 * Responsibility:
 */
public class QuizStepEntryComparator implements Comparator<QuizStepEntry> {

    /**
     * Method description
     *
     * @param c1 c1
     * @param c2 c2
     * @return Return value
     */
    @Override
    public int compare(QuizStepEntry c1, QuizStepEntry c2) {
        int result = 0;

        if (c1.getRdr() > c2.getRdr()) {
            result = 1;
        } else if (c1.getRdr() < c2.getRdr()) {
            result = -1;
        }

        return result;
    }
}
