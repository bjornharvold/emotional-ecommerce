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

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SqsQueueTest
{
    private final URL queueUrl1;
    private final URL queueUrl2;

    public SqsQueueTest()
        throws MalformedURLException
    {
        queueUrl1 = new URL("http://queue/b602183573352abf933bc7ca85fd0629/One");
        queueUrl2 = new URL("http://queue/3b0ea049a78d4a349993eeeca0c7b508/Two");
    }

    @Test
    public void testEquals()
    {
        SqsQueue queue1 = new SqsQueue(queueUrl1);
        SqsQueue queue2 = new SqsQueue(queueUrl1);
        Assert.assertTrue(queue1.equals(queue2));
    }

    @Test
    public void testNotEquals()
    {
        SqsQueue queue1 = new SqsQueue(queueUrl1);
        SqsQueue queue2 = new SqsQueue(queueUrl2);
        Assert.assertFalse(queue1.equals(queue2));
    }

    @Test
    public void testHashCode()
    {
        SqsQueue queue1 = new SqsQueue(queueUrl1);
        SqsQueue queue2 = new SqsQueue(queueUrl1);
        Assert.assertTrue(queue1.hashCode() == queue2.hashCode());
    }

    @Test
    public void testDifferentHashCodes()
    {
        SqsQueue queue1 = new SqsQueue(queueUrl1);
        SqsQueue queue2 = new SqsQueue(queueUrl2);
        Assert.assertTrue(queue1.hashCode() != queue2.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullParameter()
    {
        SqsQueue queue = new SqsQueue(null);
    }

    @Test
    public void testGetName()
    {
        SqsQueue queue = new SqsQueue(queueUrl1);
        assertEquals("One", queue.getName());
    }
}
