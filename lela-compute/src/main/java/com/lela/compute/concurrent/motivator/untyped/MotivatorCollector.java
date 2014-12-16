/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.compute.concurrent.motivator.untyped;

import akka.actor.ActorRef;
import akka.actor.Actors;
import akka.actor.UntypedActor;
import com.lela.compute.dto.compute.MotivatorMatch;
import com.lela.compute.dto.compute.MotivatorMatchQuery;
import com.lela.compute.dto.compute.MotivatorMatchResults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 2/7/12
 * Time: 2:50 AM
 * Responsibility:
 */
public class MotivatorCollector extends UntypedActor {
    private final List<ActorRef> idleMotivatorProcessors = new ArrayList<ActorRef>();
    private MotivatorMatchQuery query = null;
    private Map<String, MotivatorMatchResults> result = new HashMap<String, MotivatorMatchResults>();
    private long start = System.nanoTime();
    private long pendingNumberOfSubjectsToProcess = 0L;

    private void sendPayload() {
        if (query != null && !query.getSubjects().isEmpty() && !idleMotivatorProcessors.isEmpty()) {
            // always retrieve the subject in the first slot as it changes
            MotivatorMatch subject = query.getSubjects().get(0);
//            System.out.println("Subject payload to send: " + subject);
            idleMotivatorProcessors.remove(0).tell(new MotivatorPayload(subject, query.getProducts()));
            query.getSubjects().remove(0);
        }
    }

    public void onReceive(final Object message) {

        if (message instanceof RequestPayload) {
//            System.out.println("RequestPayload received");
            idleMotivatorProcessors.add(getContext().getSender().get());
            sendPayload();
        }

        // this one happens only once
        if (message instanceof MotivatorMatchQuery) {
//            System.out.println("MotivatorMatchQuery received");

            query = (MotivatorMatchQuery) message;

            if (query.getSubjects() == null) {
//                System.err.println("No subjects to process");
                Actors.registry().shutdownAll();
            }

            if (query.getProducts() == null) {
//                System.err.println("No products to process");
                Actors.registry().shutdownAll();
            }

            pendingNumberOfSubjectsToProcess += query.getSubjects().size();
        }

        if (message instanceof MotivatorMatchResults) {
//            System.out.println("MotivatorMatchResult received");
            MotivatorMatchResults mmr = (MotivatorMatchResults) message;

            result.put(mmr.getPerProductRelevancy().get(0).getIdentifier(), mmr);

            pendingNumberOfSubjectsToProcess -= 1;

//            System.out.println("Subjects left to process: " + pendingNumberOfSubjectsToProcess);

            if (pendingNumberOfSubjectsToProcess == 0) {
                long end = System.nanoTime();

                System.out.println("Processed: " + result.size() + " subjects");
                System.out.println("It took: " + (end - start) / 1.0e9 + " seconds");
                Actors.registry().shutdownAll();
            }
        }

    }
}
