/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.event;

import com.google.common.eventbus.EventBus;

/**
 * User: Chris Tallent
 * Date: 11/2/12
 * Time: 4:31 PM
 */
public class EventHelper {
    private static EventHandler eventHandler;

    public static void post(Object event) {
        if(eventHandler!=null){
            eventHandler.post(event);
        }
    }

    public EventHelper(EventHandler eventHandler) {
        EventHelper.eventHandler = eventHandler;
    }
}
