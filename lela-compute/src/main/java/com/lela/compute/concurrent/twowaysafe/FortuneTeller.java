/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.compute.concurrent.twowaysafe;

import akka.actor.UntypedActor;
import akka.actor.Actors;
import akka.actor.ActorRef;
import akka.actor.ActorTimeoutException;

public class FortuneTeller extends UntypedActor {
  public void onReceive(final Object name) {
    if(getContext().tryReply(String.format("%s you'll rock", name)))
      System.out.println("Message sent for " + name);
    else
      System.out.println("Sender not found for " + name);
  }  

  public static void main(final String[] args) {
    final ActorRef fortuneTeller = 
      Actors.actorOf(FortuneTeller.class).start();
    
    try {
      fortuneTeller.tell("Bill");
      final Object response = fortuneTeller.ask("Joe").get();
      System.out.println(response);
    } catch(ActorTimeoutException ex) {
      System.out.println("Never got a response before timeout");      
    } finally {
      fortuneTeller.stop();      
    }
  }
}
