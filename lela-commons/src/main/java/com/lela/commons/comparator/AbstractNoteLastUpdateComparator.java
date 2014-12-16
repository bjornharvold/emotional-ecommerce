/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.comparator;

import com.lela.domain.document.AbstractNote;
import com.lela.domain.document.Blog;
import com.lela.domain.document.Comment;

import java.util.Comparator;

/**
 * Created by Bjorn Harvold
 * Date: 10/10/11
 * Time: 9:54 PM
 * Responsibility:
 */
public class AbstractNoteLastUpdateComparator implements Comparator<AbstractNote> {

        /**
         * Sorts in descending order
         *
         * @param a1 a1
         * @param a2 a2
         * @return Return value
         */
        @Override
        public int compare(AbstractNote a1, AbstractNote a2) {
            int result = 0;

            if (a1.getDt().after(a2.getDt())) {
                result = 1;
            } else if (a1.getDt().before(a2.getDt())) {
                result = -1;
            }

            return result;
        }
    }
