/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.compute.concurrent.create;

import akka.actor.UntypedActor;

@SuppressWarnings("unchecked")
public class HollywoodActor extends UntypedActor {
  public void onReceive(final Object role) {
    System.out.println("Playing " + role + 
      " from Thread " + Thread.currentThread().getName());
  }  
}
