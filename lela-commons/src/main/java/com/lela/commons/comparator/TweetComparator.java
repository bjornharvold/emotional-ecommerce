/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.comparator;

import com.lela.domain.document.Tweet;

import java.util.Comparator;

/**
 * Created by Bjorn Harvold
 * Date: 10/10/11
 * Time: 9:54 PM
 * Responsibility:
 */
public class TweetComparator implements Comparator<Tweet> {

        /**
         * Sorts in descending order
         *
         * @param a1 a1
         * @param a2 a2
         * @return Return value
         */
        @Override
        public int compare(Tweet a1, Tweet a2) {
            int result = 0;

            if (a1.getRdr() > a2.getRdr()) {
                result = 1;
            } else if (a1.getRdr() < a2.getRdr()) {
                result = -1;
            }

            return result;
        }
    }
