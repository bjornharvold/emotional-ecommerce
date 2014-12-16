/*
 * Copyright 2006 Polar Rose <http://www.polarrose.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.polarrose.amazon.sdb;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Collection;
import java.util.Set;

public class SdbAttributesTest
{
    @Test
    public void testCreateAttributes()
    {
        SdbAttributes attributes = new SdbAttributes();

        Set<String> attributeNames = attributes.getAttributeNames();
        assertNotNull(attributeNames);
        assertEquals(0, attributeNames.size());

        Collection<String> attributeValues = attributes.getAttributes("DoesNotExist");
        assertNotNull(attributeValues);
        assertEquals(0, attributeValues.size());

        String attributeValue = attributes.getAttribute("DoesNotExist");
        assertNull(attributeValue);
    }

    @Test
    public void testAddMultipleValues()
    {
        SdbAttributes attributes = new SdbAttributes();

        attributes.addAttribute("Blah", "Foo");
        attributes.addAttribute("Blah", "Bar");

        assertTrue(attributes.containsAttribute("Blah"));

        Collection<String> attributeValues = attributes.getAttributes("Blah");
        assertNotNull(attributeValues);
        assertEquals(2, attributeValues.size());

        assertTrue(attributeValues.contains("Foo"));
        assertTrue(attributeValues.contains("Bar"));
    }

    @Test
    public void testContainsAttribute()
    {
        String[] attributeNames = new String[] { "foo", "bar", "baz" };

        SdbAttributes attributes = new SdbAttributes();

        for (String attributeName : attributeNames) {
            attributes.addAttribute(attributeName, "some value");
        }

        for (String attributeName : attributeNames) {
            assertTrue(attributes.containsAttribute(attributeName));
        }
    }
}
