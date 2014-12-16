/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.compute.service;

import com.lela.compute.dto.compute.MotivatorMatchQuery;
import com.lela.compute.dto.compute.MotivatorMatchResults;

/**
 * Created by Bjorn Harvold
 * Date: 2/4/12
 * Time: 1:40 AM
 * Responsibility:
 */
public interface ComputingService {

    MotivatorMatchResults computeMotivatorsOnSingleCode(MotivatorMatchQuery query);

    MotivatorMatchResults computeMotivatorsWithTypeActors(MotivatorMatchQuery query);
}
