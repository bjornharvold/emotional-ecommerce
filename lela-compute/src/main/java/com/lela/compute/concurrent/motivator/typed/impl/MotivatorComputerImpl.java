/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.compute.concurrent.motivator.typed.impl;

import akka.actor.TypedActor;
import com.lela.compute.concurrent.motivator.typed.MotivatorComputer;
import com.lela.compute.dto.compute.MotivatorMatch;
import com.lela.compute.dto.compute.MotivatorMatchResult;
import com.lela.compute.dto.compute.MotivatorMatchResults;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 2/6/12
 * Time: 2:05 AM
 * Responsibility:
 */
public class MotivatorComputerImpl extends TypedActor implements MotivatorComputer {
    private MotivatorMatchResults motivatorRelevancy = new MotivatorMatchResults();
    
    @Override
    public void computeMotivatorMatch(MotivatorMatch subject,
                                      MotivatorMatch product) {
//        System.out.println("Thread in useEnergy: " + Thread.currentThread().getName());

        Map<String, Integer> result = new HashMap<String, Integer>();

        // compute motivators into relevancy
        for (String key : subject.getMotivators().keySet()) {
            if (product.getMotivators().containsKey(key)) {
                Integer resultMotivatorValue = 0;

                // e.g. 6
                Integer userMotivatorValue = subject.getMotivators().get(key);

                // if it is not greater than 0, there is no relevancy at all
                if (userMotivatorValue > 0) {

                    // e.g. 9
                    Integer itemMotivatorValue = product.getMotivators().get(key);

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

        motivatorRelevancy.addMotivatorMatchResult(new MotivatorMatchResult(subject.getIdentifier(), new MotivatorMatch(product.getIdentifier(), result)));
    }

    @Override
    public MotivatorMatchResults getRelevancy() {
        return motivatorRelevancy;
    }
}
