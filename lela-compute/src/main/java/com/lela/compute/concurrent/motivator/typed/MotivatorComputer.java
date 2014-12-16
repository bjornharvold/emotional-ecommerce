/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.compute.concurrent.motivator.typed;

import com.lela.compute.dto.compute.MotivatorMatch;
import com.lela.compute.dto.compute.MotivatorMatchResults;

/**
 * Created by Bjorn Harvold
 * Date: 2/6/12
 * Time: 2:03 AM
 * Responsibility:
 */
public interface MotivatorComputer {
    void computeMotivatorMatch(MotivatorMatch subject,
                               MotivatorMatch product);

    MotivatorMatchResults getRelevancy();
}
