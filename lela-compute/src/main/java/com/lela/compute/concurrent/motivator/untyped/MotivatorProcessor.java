/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.compute.concurrent.motivator.untyped;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.lela.compute.dto.compute.MotivatorMatch;
import com.lela.compute.dto.compute.MotivatorMatchResult;
import com.lela.compute.dto.compute.MotivatorMatchResults;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 2/7/12
 * Time: 3:28 AM
 * Responsibility:
 */
public class MotivatorProcessor extends UntypedActor {
    private final ActorRef motivatorCollector;

    public MotivatorProcessor(final ActorRef motivatorCollector) {
        this.motivatorCollector = motivatorCollector;
    }

    @Override
    public void preStart() {
        registerToReceivePayload();
    }

    public void registerToReceivePayload() {
        motivatorCollector.tell(new RequestPayload(), getContext());
    }

    @Override
    public void onReceive(Object message) throws Exception {
        MotivatorPayload payload = (MotivatorPayload) message;
        MotivatorMatchResults result = new MotivatorMatchResults();
        
        for (MotivatorMatch product : payload.getProducts()) {
            // first we run computation on everything by passing it over to the computation code
            Map<String, Integer> relevancy = computeMotivatorMatch(payload.getSubject(), product);

            // create result object with only the necessary values
            result.addMotivatorMatchResult(new MotivatorMatchResult(payload.getSubject().getIdentifier(), new MotivatorMatch(product.getIdentifier(), relevancy)));
        }

//        System.out.println("Sending result back: " + result);
        // return the result to the collecting thread
        motivatorCollector.tell(result);

        // request to receive a new payload to process
        registerToReceivePayload();
    }

    private Map<String, Integer> computeMotivatorMatch(MotivatorMatch subject, MotivatorMatch product) {
//        System.out.println("Thread name: " + Thread.currentThread().getName());

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

        return result;
    }
}
