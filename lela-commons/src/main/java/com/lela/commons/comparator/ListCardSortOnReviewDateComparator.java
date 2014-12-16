/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.comparator;

import com.lela.domain.document.Review;
import com.lela.domain.document.ListCard;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by Bjorn Harvold
 * Date: 9/5/12
 * Time: 11:51 AM
 * Responsibility:
 */
public class ListCardSortOnReviewDateComparator implements Comparator<ListCard> {
    @Override
    public int compare(ListCard lc1, ListCard lc2) {
        int result = 0;

        if (lc1 != null && lc2 != null) {

            // items with reviews will trump those with no reviews
            if ((lc1.getRvws() != null && !lc1.getRvws().isEmpty()) && (lc2.getRvws() == null || lc2.getRvws().isEmpty())) {
                result = -1;
            } else if ((lc2.getRvws() != null && !lc2.getRvws().isEmpty()) && (lc1.getRvws() == null || lc1.getRvws().isEmpty())) {
                result = 1;
            } else if ((lc1.getRvws() != null && !lc1.getRvws().isEmpty()) && (lc2.getRvws() != null && !lc2.getRvws().isEmpty())) {
                // now we can start comparing dates - the review with the highest date wins
                Date highestReviewDateLC1 = null;
                Date highestReviewDateLC2 = null;

                for (Review review : lc1.getRvws()) {
                    if (highestReviewDateLC1 == null) {
                        highestReviewDateLC1 = review.getDt();
                    }

                    if (highestReviewDateLC1.before(review.getDt())) {
                        highestReviewDateLC1 = review.getDt();
                    }
                }

                for (Review review : lc2.getRvws()) {
                    if (highestReviewDateLC2 == null) {
                        highestReviewDateLC2 = review.getDt();
                    }

                    if (highestReviewDateLC2.before(review.getDt())) {
                        highestReviewDateLC2 = review.getDt();
                    }
                }

                if (highestReviewDateLC1 != null && highestReviewDateLC2 != null) {
                    if (highestReviewDateLC1.after(highestReviewDateLC2)) {
                        result = -1;
                    } else if (highestReviewDateLC1.before(highestReviewDateLC2)) {
                        result = 1;
                    }
                }
            }
        }

        return result;
    }
}
