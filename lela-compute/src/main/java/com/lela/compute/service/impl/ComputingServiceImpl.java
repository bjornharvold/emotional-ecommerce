/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.compute.service.impl;

import com.lela.compute.concurrent.motivator.typed.MotivatorComputer;
import com.lela.compute.service.ComputingService;
import com.lela.compute.dto.compute.MotivatorMatch;
import com.lela.compute.dto.compute.MotivatorMatchQuery;
import com.lela.compute.dto.compute.MotivatorMatchResult;
import com.lela.compute.dto.compute.MotivatorMatchResults;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 2/4/12
 * Time: 1:41 AM
 * Responsibility:
 */
@Service
public class ComputingServiceImpl implements ComputingService {

    @Resource(name = "motivatorComputer")
    private MotivatorComputer motivatorComputer;

    public void setMotivatorComputer(MotivatorComputer motivatorComputer) {
        this.motivatorComputer = motivatorComputer;
    }

    @Override
    public MotivatorMatchResults computeMotivatorsOnSingleCode(MotivatorMatchQuery query) {
        MotivatorMatchResults result = null;

        if (query == null) {
            throw new IllegalArgumentException("Query cannot be null");
        }

        if (query.getSubjects() == null) {
            throw new IllegalArgumentException("Query subjects cannot be null");
        }

        if (query.getProducts() == null) {
            throw new IllegalArgumentException("Query products cannot be null");
        }

        for (MotivatorMatch subject : query.getSubjects()) {
            // the subject is usually the main object of interest, such as a user

            for (MotivatorMatch product : query.getProducts()) {
                // first we run computation on everything by passing it over to the computation code
                Map<String, Integer> relevancy = computeMotivatorRelevancy(subject.getMotivators(), product.getMotivators());

                // create result object with only the necessary values
                MotivatorMatchResult mmr = new MotivatorMatchResult(subject.getIdentifier(), new MotivatorMatch(product.getIdentifier(), relevancy));

                if (result == null) {
                    result = new MotivatorMatchResults();
                    result.addMotivatorMatchResult(mmr);
                }
            }

        }

        return result;
    }

    @Override
    public MotivatorMatchResults computeMotivatorsWithTypeActors(MotivatorMatchQuery query) {
        if (query == null) {
            throw new IllegalArgumentException("Query cannot be null");
        }

        if (query.getSubjects() == null) {
            throw new IllegalArgumentException("Query subjects cannot be null");
        }

        if (query.getProducts() == null) {
            throw new IllegalArgumentException("Query products cannot be null");
        }

        for (MotivatorMatch subject : query.getSubjects()) {
            // the subject is usually the main object of interest, such as a user

            for (MotivatorMatch product : query.getProducts()) {
                // first we run computation on everything by passing it over to the computation code
                motivatorComputer.computeMotivatorMatch(subject, product);
            }
        }

        return motivatorComputer.getRelevancy();
    }

    private Map<String, Integer> computeMotivatorRelevancy(Map<String, Integer> subject,
                                                           Map<String, Integer> product) {
        Map<String, Integer> result = new HashMap<String, Integer>();

        // compute motivators into relevancy
        for (String key : subject.keySet()) {
            if (product.containsKey(key)) {
                Integer resultMotivatorValue = 0;

                // e.g. 6
                Integer userMotivatorValue = subject.get(key);

                // if it is not greater than 0, there is no relevancy at all
                if (userMotivatorValue > 0) {

                    // e.g. 9
                    Integer itemMotivatorValue = product.get(key);

                    // e.g. 3
                    Integer itemDistance = itemMotivatorValue - userMotivatorValue;
                    StringBuilder uniqueIdentifier = new StringBuilder(userMotivatorValue.toString());

                    if (itemDistance > 0) {

                        // product motivator is greater than user motivator we need to change the
                        // item motivator value to be less
                        // than user motivator but with the same distance away from the user motivator
                        // e.g. user motivator is 6
                        // e.g. item motivator is 9
                        // e.g. distance is 3
                        // e.g. so we need to subtract 3 from 6 to end up on the opposite side of the 6 spectrum
                        // e.g. result is 63
                        if (userMotivatorValue - itemDistance < 0) {
                            uniqueIdentifier.append("0");
                        } else {
                            uniqueIdentifier.append((userMotivatorValue - itemDistance));
                        }
                    } else {

                        // e.g. "69"
                        uniqueIdentifier.append(itemMotivatorValue.toString());
                    }

                    // e.g. 69
                    Integer uniqueIdentifierValue = Integer.parseInt(uniqueIdentifier.toString());

                    // e.g. 69 % 60 = 9
                    Integer subtractFromUniqueIdentifierValue = uniqueIdentifierValue % (userMotivatorValue * 10);

                    // if modulus 10 is 0 here it means the last number was a 0
                    // and that there is no relevancy here worth thinking about
                    if (uniqueIdentifierValue % 10 == 0) {
                        resultMotivatorValue = 0;
                    } else {

                        // e.g. 6 * (69 - 60) = 54
                        resultMotivatorValue = userMotivatorValue
                                * (uniqueIdentifierValue
                                - (uniqueIdentifierValue - subtractFromUniqueIdentifierValue));
                    }

                    /*
                     * System.out.println("key: " + key);
                     * System.out.println("user motivator value: " + userMotivatorValue);
                     * System.out.println("item motivator value: " + itemMotivatorValue);
                     * System.out.println("item distance: " + itemDistance);
                     * System.out.println("unique identifer: " + uniqueIdentifier.toString());
                     * System.out.println("unique identifer value: " + uniqueIdentifierValue);
                     * System.out.println("motivator relevancy: " + resultMotivatorValue);
                     */

                    // e.g. 6 * (69 - (69 - 9)) = 36
                    result.put(key, resultMotivatorValue);
                }
            }
        }

        return result;
    }
}
