/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.compute.concurrent.typed1;

import akka.actor.TypedActor;

public class EnergySourceImpl extends TypedActor implements EnergySource {
  private final long MAXLEVEL = 100L;
  private long level = MAXLEVEL;
  private long usageCount = 0L;
  
  public long getUnitsAvailable() { return level; }
  
  public long getUsageCount() { return usageCount; }
  
  public void useEnergy(final long units) {
    if (units > 0 && level - units >= 0) {
      System.out.println(
        "Thread in useEnergy: " + Thread.currentThread().getName());
      level -= units;
      usageCount++;
    }
  }
}
