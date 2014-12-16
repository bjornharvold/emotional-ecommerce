/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.comparator;

import com.lela.domain.document.Blog;

import java.util.Comparator;

/**
 * Created by Bjorn Harvold
 * Date: 10/10/11
 * Time: 9:54 PM
 * Responsibility:
 */
public class BlogLastUpdateComparator implements Comparator<Blog> {

        /**
         * Sorts in descending order
         *
         * @param a1 a1
         * @param a2 a2
         * @return Return value
         */
        @Override
        public int compare(Blog a1, Blog a2) {
            int result = 0;

            if (a1.getLdt().after(a2.getLdt())) {
                result = -1;
            } else if (a1.getLdt().before(a2.getLdt())) {
                result = 1;
            }

            return result;
        }
    }
