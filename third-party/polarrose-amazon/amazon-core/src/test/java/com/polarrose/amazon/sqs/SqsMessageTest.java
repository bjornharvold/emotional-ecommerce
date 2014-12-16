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

package com.polarrose.amazon.sqs;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class SqsMessageTest
{
    @Test
    public void testEquals()
    {
        SqsMessage message1 = new SqsMessage("Id", "Body");
        SqsMessage message2 = new SqsMessage("Id", "Body");
        assertTrue(message1.equals(message2));
    }

    @Test
    public void testNotEquals()
    {
        SqsMessage message1 = new SqsMessage("Id1", "Body1");
        SqsMessage message2 = new SqsMessage("Id2", "Body2");
        assertFalse(message1.equals(message2));
    }

    @Test
    public void testHashCodes()
    {
        SqsMessage message1 = new SqsMessage("Id1", "Body1");
        SqsMessage message2 = new SqsMessage("Id2", "Body2");
        assertTrue(message1.hashCode() != message2.hashCode());
    }

    @Test
    public void testDifferentHashCodes()
    {
        SqsMessage message1 = new SqsMessage("Id1", "Body1");
        SqsMessage message2 = new SqsMessage("Id2", "Body2");
        assertTrue(message1.hashCode() != message2.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullParameter1()
    {
        new SqsMessage("Foo", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullParameter2()
    {
        new SqsMessage(null, "Bar");
    }
}
