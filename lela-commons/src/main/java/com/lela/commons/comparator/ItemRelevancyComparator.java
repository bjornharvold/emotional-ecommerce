/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.comparator;

import com.lela.domain.document.RelevantItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;

/**
 * Created by Bjorn Harvold
 * Date: 10/10/11
 * Time: 10:01 PM
 * Responsibility:
 */
public class ItemRelevancyComparator implements Comparator<RelevantItem> {

    /**
     * Method description
     *
     * @param ri1 ri1
     * @param ri2 ri2
     * @return Return value
     */
    @Override
    public int compare(RelevantItem ri1, RelevantItem ri2) {
        int result = 0;

        if (ri1 != null && ri2 != null) {
            // primary sort condition
            if (ri1.getTtlrlvncy() != null && ri2.getTtlrlvncy() != null) {
                if (ri1.getTtlrlvncy() > ri2.getTtlrlvncy()) {
                    result = -1;
                } else if (ri1.getTtlrlvncy() < ri2.getTtlrlvncy()) {
                    result = 1;
                }
            }

            // secondary sort condition
            if (result == 0 && ri1.getTld() != null && ri2.getTld() != null) {
                if (ri1.getTld() > ri2.getTld()) {
                    result = -1;
                } else if (ri1.getTld() < ri2.getTld()) {
                    result = 1;
                }
            }
        }

        return result;
    }
}
