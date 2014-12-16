package com.lela.commons.event;

import com.google.common.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 11/9/12
 * Time: 10:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class EventBusEventHandlerImpl implements EventHandler {

    @Autowired
    EventBus eventBus;

    public void post(Object event)
    {
        if(eventBus!=null)
            eventBus.post(event);
    }
}
