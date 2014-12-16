/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.compute.web.controller;

import com.lela.compute.service.ComputingService;
import com.lela.compute.dto.compute.MotivatorMatchQuery;
import com.lela.compute.dto.compute.MotivatorMatchResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Bjorn Harvold
 * Date: 2/3/12
 * Time: 11:41 PM
 * Responsibility: Handles all requests where computing is necessary
 */
@Controller
public class ComputationController {

    private final ComputingService computingService;

    @Autowired
    public ComputationController(ComputingService computingService) {
        this.computingService = computingService;
    }

    /**
     * Computes a list of subjects against a list of objects
     * @param query query
     * @return Returns a list of computed results
     */
    @RequestMapping(value = "/compute/motivators", method = RequestMethod.POST,
            consumes = "application/json", produces = "application/json")
    @ResponseBody
    public MotivatorMatchResults computeMotivators(@RequestBody MotivatorMatchQuery query) {
        return computingService.computeMotivatorsOnSingleCode(query);
    }
}
