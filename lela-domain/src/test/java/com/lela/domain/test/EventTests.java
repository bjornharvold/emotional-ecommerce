package com.lela.domain.test;

import com.lela.domain.document.Event;
import com.lela.domain.document.EventField;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 5/1/12
 * Time: 2:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventTests {

    @Test
    public void testValidEvent()
    {
        Event event = new Event();
        List<EventField> eventFields = new ArrayList<EventField>();

        EventField eventField1 = new EventField();
        eventField1.setFldNm("fld1");
        EventField eventField2 = new EventField();
        eventField2.setFldNm("fld2");
        eventField2.setRqrd(true);

        eventFields.add(eventField1);
        eventFields.add(eventField2);
        event.setVntFlds(eventFields);

        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("fld1", "value");
        attributes.put("fld2", "value");
        assertTrue("Event with both attributes is valid", event.isValid(attributes));

        attributes = new HashMap<String, Object>();
        attributes.put("fld1", "value");
        assertFalse("Event without required attribute is not valid", event.isValid(attributes));

        attributes = new HashMap<String, Object>();
        attributes.put("fld2", "value");
        assertTrue("Event with required attribute is valid", event.isValid(attributes));

        attributes = new HashMap<String, Object>();
        attributes.put("fld1", "value");
        attributes.put("fld2", "");
        assertFalse("Event with blank required attribute is not valid", event.isValid(attributes));

        attributes = new HashMap<String, Object>();
        attributes.put("fld3", "value");
        attributes.put("fld2", "value");
        assertFalse("Event with undefined attribute is not valid", event.isValid(attributes));

    }
}
