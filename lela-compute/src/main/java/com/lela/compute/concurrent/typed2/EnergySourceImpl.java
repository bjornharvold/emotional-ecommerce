/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.compute.concurrent.typed2;

import akka.actor.ActorRef;
import akka.actor.TypedActor;
import akka.actor.Scheduler;
import com.lela.compute.concurrent.typed1.EnergySource;
import scala.PartialFunction;
import scala.PartialFunction$class;
import scala.Function1;
import scala.Function;
import scala.runtime.BoxedUnit;
import scala.runtime.AbstractFunction1;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("unchecked")
public class EnergySourceImpl extends TypedActor implements EnergySource {
    private final long MAXLEVEL = 100L;
    private long level = MAXLEVEL;
    private long usageCount = 0L;

    class Replenish {
    }

    @Override
    public void preStart() {
        Scheduler.schedule(
                optionSelf().get(), new Replenish(), 1, 1, TimeUnit.SECONDS);
    }

    @Override
    public void postStop() {
        Scheduler.shutdown();
    }

    private void replenish() {
        System.out.println("Thread in replenish: " +
                Thread.currentThread().getName());
        if (level < MAXLEVEL) level += 1;
    }

    @Override
    public PartialFunction receive() {
        return processMessage().orElse(super.receive());
    }

    private PartialFunction processMessage() {

        class MyDispatch extends AbstractFunction1 implements PartialFunction {
            public boolean isDefinedAt(Object message) {
                return message instanceof Replenish;
            }

            public Object apply(Object message) {
                if (message instanceof Replenish) replenish();
                return null;
            }

            public Function1 lift() {
                return PartialFunction$class.lift(this);
            }

            public PartialFunction andThen(Function1 function) {
                return PartialFunction$class.andThen(this, function);
            }

            public PartialFunction orElse(PartialFunction function) {
                return PartialFunction$class.orElse(this, function);
            }
        }
        ;

        return new MyDispatch();
    }

    public long getUnitsAvailable() {
        return level;
    }

    public long getUsageCount() {
        return usageCount;
    }

    public void useEnergy(final long units) {
        if (units > 0 && level - units >= 0) {
            System.out.println(
                    "Thread in useEnergy: " + Thread.currentThread().getName());
            level -= units;
            usageCount++;
        }
    }
}
