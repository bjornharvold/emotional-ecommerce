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

package com.polarrose.amazon.sqs.impl;

import java.util.ArrayList;
import java.util.List;

import com.polarrose.amazon.aws.AwsAccount;
import com.polarrose.amazon.sqs.SqsMessage;
import com.polarrose.amazon.sqs.SqsQueue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class SqsServiceImplTest
{
    private static final String TESTQUEUE_PREFIX = "SqsServiceImplTestTestQueue";

    private SqsServiceImpl simpleQueueService;

    @Before
    public void setupSimpleQueueService()
    {
        AwsAccount account = new AwsAccount(
            System.getProperty("com.polarrose.amazon.AccessKeyId"),
            System.getProperty("com.polarrose.amazon.SecretAccessKey")
        );

        simpleQueueService = new SqsServiceImpl();
        simpleQueueService.setAccount(account);
    }

    @Test
    public void testCreateQueue()
        throws Exception
    {
        SqsQueue queue = simpleQueueService.createQueue(createRandomQueueName());
        assertNotNull(queue);
    }

    @Test
    public void testGetQueueVisibilityTimeout()
        throws Exception
    {
        SqsQueue queue = simpleQueueService.createQueue(createRandomQueueName());
        assertNotNull(queue);

        int visibilityTimeout = simpleQueueService.getVisibilityTimeout(queue);
        assertEquals(30, visibilityTimeout);
    }

    @Test
    public void testDeleteQueue()
        throws Exception
    {
        SqsQueue queue = simpleQueueService.createQueue(createRandomQueueName());
        assertNotNull(queue);

        simpleQueueService.deleteQueue(queue);
    }

    @Test
    public void testSendMessage()
        throws Exception
    {
        SqsQueue queue = simpleQueueService.createQueue(createRandomQueueName());
        assertNotNull(queue);

        SqsMessage message = simpleQueueService.sendMessage(queue, "Hallo, wereld!");
        assertNotNull(message);

        assertNotNull(message.getId());

        assertNotNull(message.getBody());
        assertEquals("Hallo, wereld!", message.getBody());
    }

    @Test
    public void testSendReceiveDeleteMessage() throws InterruptedException
    {
        // Create a queue

        SqsQueue queue = simpleQueueService.createQueue(createRandomQueueName());
        assertNotNull(queue);

        // Send a message

        SqsMessage message1 = simpleQueueService.sendMessage(queue, "Hello World");
        assertNotNull(message1);

        // Receive the message

        SqsMessage message2 = null;

        boolean ok = false;
        for (int i = 0; i < 12; i++) {
            message2 = simpleQueueService.receiveMessage(queue, 5);
            if (message2 != null) {
                assertEquals(message1.getId(),  message2.getId());
                assertEquals(message1.getBody(),  message2.getBody());
                ok = true;
                break;
            } else {
                Thread.sleep(5000L);
            }
        }

        assertTrue(ok);
        assertNotNull(message2);

        simpleQueueService.deleteMessage(queue, message2);
    }

    @Test
    public void testSendMessageWithEncodings()
        throws Exception
    {
        // Create a queue

        SqsQueue queue = simpleQueueService.createQueue(createRandomQueueName());
        assertNotNull(queue);

        // Send a message
        
        SqsMessage message1 = simpleQueueService.sendMessage(queue, "[~`!@#$%^&*()-_=+]}[{\\|'\";:/?.>,<] - [http://developer.amazonwebservices.com/connect/thread.jspa?threadID=27353&tstart=0]");
        assertNotNull(message1);

        // Receive the message

        boolean ok = false;
        for (int i = 0; i < 12; i++) {
            SqsMessage message2 = simpleQueueService.receiveMessage(queue, 5);
            if (message2 != null) {
                assertEquals(message1.getId(),  message2.getId());
                assertEquals(message1.getBody(),  message2.getBody());
                ok = true;
                break;
            } else {
                Thread.sleep(5000L);
            }
        }

        assertTrue(ok);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSendMessageWithControlCharacters()
        throws Exception
    {
        // Create a queue

        SqsQueue queue = simpleQueueService.createQueue(createRandomQueueName());
        assertNotNull(queue);

        // Create a crazy message

        char[] data = new char[256];
        for (int i = 0; i < 256; i++) {
            data[i] = (char) i;
        }

        String message = new String(data);

        // Send a message

        SqsMessage message1 = simpleQueueService.sendMessage(queue, message);
        assertNotNull(message1);

        // Receive the message

        boolean ok = false;
        for (int i = 0; i < 12; i++) {
            SqsMessage message2 = simpleQueueService.receiveMessage(queue, 5);
            if (message2 != null) {
                assertEquals(message1.getId(),  message2.getId());
                assertEquals(message1.getBody(),  message2.getBody());
                ok = true;
                break;
            } else {
                Thread.sleep(5000L);
            }
        }

        assertTrue(ok);
    }

    // This is not supposed to work, because of a bug in SQS 2008-01-01.
    // See http://developer.amazonwebservices.com/connect/thread.jspa?threadID=23084&tstart=0

//    @Test
//    public void testSendMessageWithUTF8()
//        throws Exception
//    {
//        SqsQueue queue = simpleQueueService.createQueue(createRandomQueueName());
//        assertNotNull(queue);
//
//        SqsMessage message = simpleQueueService.sendMessage(queue, "HŒll¿, wŽr‘ld");
//        assertNotNull(message);
//    }

    @Test(expected = IllegalArgumentException.class)
    public void testSendUTF8Body()
    {
        SqsQueue queue = simpleQueueService.createQueue(createRandomQueueName());
        assertNotNull(queue);

        SqsMessage message = simpleQueueService.sendMessage(queue, "HŒll¿, wŽr‘ld");
    }

    @Test
    public void testSendAndReceiveMessage()
        throws Exception
    {
        // Create a queue

        SqsQueue queue = simpleQueueService.createQueue(createRandomQueueName());
        assertNotNull(queue);

        // Send a message

        SqsMessage message1 = simpleQueueService.sendMessage(queue, "Hallo, wereld!");
        assertNotNull(message1);

        // Receive the message

        boolean ok = false;
        for (int i = 0; i < 12; i++) {
            SqsMessage message2 = simpleQueueService.receiveMessage(queue, 5);
            if (message2 != null) {
                assertEquals(message1.getId(),  message2.getId());
                assertEquals(message1.getBody(),  message2.getBody());
                ok = true;
                break;
            } else {
                Thread.sleep(5000L);
            }
        }

        assertTrue(ok);
    }

    @Test
    public void testReceiveMessages()
        throws Exception
    {
        // Create a queue

        SqsQueue queue = simpleQueueService.createQueue(createRandomQueueName());
        assertNotNull(queue);

        // Send some messages

        for (int i = 1; i <= 5; i++) {
            SqsMessage message1 = simpleQueueService.sendMessage(queue, "TestMessage" + i);
            assertNotNull(message1);
        }

        // Try for a minute

        long startTime = System.currentTimeMillis();
        List<SqsMessage> messages = new ArrayList<SqsMessage>();

        while ((System.currentTimeMillis() - startTime) < 60000L)
        {
            messages.addAll(simpleQueueService.receiveMessages(queue, 5, 5));
            if (messages.size() == 5) {
                break;
            }

            Thread.sleep(1000L);
        }

        assertEquals(5, messages.size());
    }

    @Test
    public void testGetLastRequestId()
    {
        SqsQueue queue1 = simpleQueueService.createQueue(createRandomQueueName());
        assertNotNull(queue1);

        String requestId1 = simpleQueueService.getLastRequestId();
        assertNotNull(requestId1);

        SqsQueue queue2 = simpleQueueService.createQueue(createRandomQueueName());
        assertNotNull(queue2);

        String requestId2 = simpleQueueService.getLastRequestId();
        assertNotNull(requestId1);

        assertNotSame(requestId1,  requestId2);
    }

    @Test
    public void testListQueues()
    {
        List<SqsQueue> queues = simpleQueueService.listQueues();
        assertNotNull(queues);
        assertTrue(queues.size() != 0);
    }

    @Test
    public void testListQueuesWithPrefix()
    {
        // Create three queues with the same prefix

        String queueNamePrefix = createRandomQueueName();

        assertNotNull(simpleQueueService.createQueue(queueNamePrefix + "Een"));
        assertNotNull(simpleQueueService.createQueue(queueNamePrefix + "Twee"));
        assertNotNull(simpleQueueService.createQueue(queueNamePrefix + "Drie"));

        // Find the queues by prefix

        List<SqsQueue> queues = simpleQueueService.listQueues(queueNamePrefix);
        assertNotNull(queues);
        assertTrue(queues.size() == 3);
    }

    // Todo: I was expecting this to return 400 / AWS.SimpleQueueService.NonExistentQueue but it doesn't. File a bug with AWS?

//    @Test(expected = SqsException.class)
//    public void testReceiveFromUnknownQueue()
//        throws MalformedURLException
//    {
//        // Create a queue, then modify the url so that it points to a queue that does not exist
//
//        SqsQueue queue = simpleQueueService.createQueue(createRandomQueueName());
//
//        URL url = new URL(
//            queue.getUrl().getProtocol(),
//            queue.getUrl().getHost(),
//            queue.getUrl().getPort(),
//            queue.getUrl().getFile() + "BestaatNiet"
//        );
//
//        queue.setUrl(url);
//
//        // Now try to delete the non-existent queue
//
//        simpleQueueService.deleteQueue(queue);
//    }

    @Test
    public void testDeleteAllTestQueues()
    {
        // Find the queues by prefix

        List<SqsQueue> queues = simpleQueueService.listQueues(TESTQUEUE_PREFIX);
        assertNotNull(queues);

        for (SqsQueue queue : queues) {
            simpleQueueService.deleteQueue(queue);
        }
    }

    //

    private static String createRandomQueueName()
    {
        return TESTQUEUE_PREFIX + System.currentTimeMillis();
    }
}
