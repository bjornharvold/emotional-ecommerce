/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.compute.test;

import akka.actor.ActorRef;
import akka.actor.Actors;
import akka.actor.TypedActor;
import com.lela.compute.concurrent.create.HollywoodActor;
import com.lela.compute.concurrent.typed1.EnergySource;
import com.lela.compute.concurrent.typed1.EnergySourceImpl;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Bjorn Harvold
 * Date: 2/1/12
 * Time: 9:43 PM
 * Responsibility:
 */
public class TesterFunctionalTest extends AbstractFunctionalTest {
//    private final static Logger log = LoggerFactory.getLogger(TesterFunctionalTest.class);

    /*
    @Autowired
    private AmazonElasticMapReduce amazonElasticMapReduce;


    @Autowired
    private Job wordCountJob;


    @Test
    public void testMapReduceJobs() {
        log.info("Hello world: " + wordCountJob);
        assertNotNull("Job is null", wordCountJob);
        
        try {
            boolean result = wordCountJob.waitForCompletion(true);
            assertTrue("Job didn't complete", result);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        
    }

    
    @Test
    public void testElasticMapReduce() {
        assertNotNull("mapReduce bean is null", amazonElasticMapReduce);

        JobFlowInstancesConfig config = new JobFlowInstancesConfig();
        config.set
        RunJobFlowRequest request = new RunJobFlowRequest("myflow", config);
        amazonElasticMapReduce.runJobFlow()

    }
    */

    @Resource(name = "myActor")
    private ActorRef myActor;

    @Resource(name = "typedActor")
    private EnergySource energySource;

    @Test
    public void testTrue() {
        assertTrue(true);
    }

    @Test
    public void testActor() {
        try {
            assertNotNull("Actor is null", myActor);

            myActor.tell("Jack Sparrow");
            Thread.sleep(100);
            myActor.tell("Edward Scissorhands");
            Thread.sleep(100);
            myActor.tell("Willy Wonka");
//            Actors.registry().shutdownAll();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void testTypedActor() {
        try {
            assertNotNull("energySource is null", energySource);

            System.out.println("Thread in main: " +
                    Thread.currentThread().getName());

            System.out.println("Energy units " + energySource.getUnitsAvailable());

            System.out.println("Firing two requests for use energy");
            energySource.useEnergy(10);
            System.out.println("Energy units " + energySource.getUnitsAvailable());
            energySource.useEnergy(10);
            System.out.println("Fired two requests for use energy");
            Thread.sleep(100);
            System.out.println("Firing one more requests for use energy");
            energySource.useEnergy(10);

            Thread.sleep(1000);
            System.out.println("Energy units " + energySource.getUnitsAvailable());
            System.out.println("Usage " + energySource.getUsageCount());
//            TypedActor.stop(energySource);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


}
