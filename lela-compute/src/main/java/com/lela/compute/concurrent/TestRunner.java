/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.compute.concurrent;

import akka.actor.ActorRef;
import akka.actor.Actors;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import com.lela.compute.concurrent.motivator.untyped.MotivatorCollector;
import com.lela.compute.concurrent.motivator.untyped.MotivatorProcessor;
import com.lela.compute.service.ComputingService;
import com.lela.compute.dto.compute.MotivatorMatch;
import com.lela.compute.dto.compute.MotivatorMatchQuery;
import com.lela.compute.dto.compute.MotivatorMatchResults;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 2/6/12
 * Time: 9:00 PM
 * Responsibility:
 */
public class TestRunner {

    private static final int PRODUCTS = 1000;
    private static final int SUBJECTS = 50;

    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = null;

        ctx = new ClassPathXmlApplicationContext("META-INF/spring/applicationContext.xml");

        ComputingService mc = ctx.getBean(ComputingService.class);

        TestRunner tr = new TestRunner();
        MotivatorMatchQuery query = tr.createLotsaTestData(SUBJECTS, PRODUCTS);


        long start = System.nanoTime();
        MotivatorMatchResults result = mc.computeMotivatorsOnSingleCode(query);
        long end = System.nanoTime();

        System.out.println("Running the calculations on a single core took: " + (end - start) / 1.0e9 + " seconds");


        /*
        start = System.nanoTime();
        result = mc.computeMotivatorsWithTypeActors(query);
        end = System.nanoTime();

        System.out.println("Running the calculations using typed actors took: " + (end - start) / 1.0e9 + " seconds");
        */

        final ActorRef motivatorCollector = Actors.actorOf(MotivatorCollector.class).start();

        motivatorCollector.tell(query);

        // start up some processors
        for (int i = 0; i < 100; i++)
            Actors.actorOf(new UntypedActorFactory() {
                public UntypedActor create() {
                    return new MotivatorProcessor(motivatorCollector);
                }
            }).start();
    }

    public MotivatorMatchQuery createLotsaTestData(int subjectCount, int productCount) {
        MotivatorMatchQuery result = new MotivatorMatchQuery();

        List<MotivatorMatch> subjects = new ArrayList<MotivatorMatch>();
        List<MotivatorMatch> products = new ArrayList<MotivatorMatch>();

        for (int i = 0; i < subjectCount; i++) {
            subjects.add(createMotivatorMatch());
        }

        for (int i = 0; i < productCount; i++) {
            products.add(createMotivatorMatch());
        }

        result.setSubjects(subjects);
        result.setProducts(products);

        return result;
    }

    private MotivatorMatch createMotivatorMatch() {
        MotivatorMatch result = new MotivatorMatch();

        result.setIdentifier(RandomStringUtils.randomAlphabetic(10));

        Map<String, Integer> motivators = new HashMap<String, Integer>(8);
        motivators.put("A", new Double(Math.random() * 100).intValue());
        motivators.put("B", new Double(Math.random() * 100).intValue());
        motivators.put("C", new Double(Math.random() * 100).intValue());
        motivators.put("D", new Double(Math.random() * 100).intValue());
        motivators.put("E", new Double(Math.random() * 100).intValue());
        motivators.put("F", new Double(Math.random() * 100).intValue());
        motivators.put("G", new Double(Math.random() * 100).intValue());
        motivators.put("H", new Double(Math.random() * 100).intValue());

        result.setMotivators(motivators);

        return result;
    }
}
