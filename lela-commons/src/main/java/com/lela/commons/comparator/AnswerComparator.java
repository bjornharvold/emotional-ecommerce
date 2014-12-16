/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.comparator;

import com.lela.domain.document.Answer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Comparator;

/**
 * Created by Bjorn Harvold
 * Date: 10/10/11
 * Time: 9:56 PM
 * Responsibility:
 */
public class AnswerComparator implements Comparator<Answer> {

    /**
     * Method description
     *
     * @param c1 c1
     * @param c2 c2
     * @return Return value
     */
    @Override
    public int compare(Answer c1, Answer c2) {
        int result = 0;

        if (StringUtils.isNotBlank(c1.getKy()) && StringUtils.isNotBlank(c2.getKy())
                && NumberUtils.isNumber(c1.getKy()) && NumberUtils.isNumber(c2.getKy())) {
            Integer rdr1 = NumberUtils.createInteger(c1.getKy());
            Integer rdr2 = NumberUtils.createInteger(c2.getKy());

            if (rdr1 > rdr2) {
                result = 1;
            } else if (rdr1 < rdr2) {
                result = -1;
            }
        }

        return result;
    }
}
