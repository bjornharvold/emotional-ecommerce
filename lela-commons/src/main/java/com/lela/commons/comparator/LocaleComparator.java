/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.comparator;

import com.lela.domain.document.Answer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Comparator;
import java.util.Locale;

/**
 * Created by Bjorn Harvold
 * Date: 10/10/11
 * Time: 9:56 PM
 * Responsibility:
 */
public class LocaleComparator implements Comparator<Locale> {

    /**
     * Method description
     *
     * @param c1 c1
     * @param c2 c2
     * @return Return value
     */
    @Override
    public int compare(Locale c1, Locale c2) {
        return c1.toString().compareTo(c2.toString());
    }
}
