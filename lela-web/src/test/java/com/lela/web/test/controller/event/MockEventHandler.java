package com.lela.web.test.controller.event;

import com.lela.commons.event.EventHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 11/9/12
 * Time: 4:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class MockEventHandler implements EventHandler {

    List events = new ArrayList();

    @Override
    public void post(Object event) {
       events.add(event);
    }


    public List getEvents()
    {
        return events;
    }

    public void clear()
    {
        events.clear();
    }

    public <T> T find(Class<T> clazz)
    {
        for(Object o : events)
        {
            if (o != null && o.getClass() == clazz)
            {
                return clazz.cast(o);
            }
        }

        return null;
    }
}
