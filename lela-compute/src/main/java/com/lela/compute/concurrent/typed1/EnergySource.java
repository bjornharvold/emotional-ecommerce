/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.compute.concurrent.typed1;

public interface EnergySource {
  long getUnitsAvailable();
  long getUsageCount();
  void useEnergy(final long units);
}
