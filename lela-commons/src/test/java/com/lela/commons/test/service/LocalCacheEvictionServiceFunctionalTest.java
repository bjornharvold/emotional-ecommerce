/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lela.commons.test.AbstractFunctionalTest;
import com.polarrose.amazon.spring.sqs.impl.SimpleQueueListener;
import com.polarrose.amazon.sqs.SqsMessage;
import com.polarrose.amazon.sqs.SqsQueue;
import com.polarrose.amazon.sqs.SqsService;

/**
 * Created by Bjorn Harvold
 * Date: 11/23/11
 * Time: 10:40 PM
 * Responsibility:
 */
public class LocalCacheEvictionServiceFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(LocalCacheEvictionServiceFunctionalTest.class);
    private static final String TEST = "Test";

    @Autowired
    private SqsService sqsService;

    private SqsQueue queue = null;

    private MessageHandler firstHandler = null;
    private MessageHandler secondHandler = null;
    private MessageHandler thirdHandler = null;
    
    private SimpleQueueListener listener = null;
    private SimpleQueueListener listener1 = null;
    private SimpleQueueListener listener2 = null;
    
    @Before
    public void setUp() {
    	try {
            queue = new SqsQueue(new URL("http://queue.amazonaws.com/lela-test-cache-queue"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testSQSQueue() {
        Random random = new Random();
        try {


            listener = new SimpleQueueListener();
            DelayedMessageHandler handler = new DelayedMessageHandler();
            listener.setMessageHandler(handler);
            listener.setPollInterval(30);
            listener.setQueue(queue);
            listener.setThreadPoolSize(10);
            listener.setService(sqsService);

            listener.afterPropertiesSet();  
            /*
            List<SqsQueue> queues = sqsService.listQueues();

            if (queues != null) {
                for (SqsQueue q : queues) {
                    log.warn(q.getName() + " : " + q.getUrl().toString());
                }
            }
            */

            int n = random.nextInt(10);

            for (int i = 1; i <= n; i++) {
                String s = "Hello, world. This is message " + i + " in a batch of " + n;
                log.info(s);
                SqsMessage message = sqsService.sendMessage(queue, s);
                assertNotNull("Message is null", message);
            }

            Thread.sleep((random.nextInt(13) + 3) * 100L);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        } finally {
            try {
                listener.destroy();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                fail(e.getMessage());
            }
        }
    }

    @Ignore
    @Test
    public void testSQSQueueConsumption() {
        try {
        	//Set up the listeners

            listener = new SimpleQueueListener();
            firstHandler = new MessageHandler("first");
            listener.setMessageHandler(firstHandler);
            listener.setPollInterval(30);
            listener.setQueue(queue);
            listener.setThreadPoolSize(10);
            listener.setService(sqsService);

            listener.afterPropertiesSet();        
            
            secondHandler = new MessageHandler("second");
            listener1 = new SimpleQueueListener();
            listener1.setMessageHandler(secondHandler);
            listener1.setPollInterval(30);
            listener1.setQueue(queue);
            listener1.setThreadPoolSize(10);
            listener1.setService(sqsService);
            listener1.afterPropertiesSet();
            
            thirdHandler = new MessageHandler("third");
            listener2 = new SimpleQueueListener();
            listener2.setMessageHandler(thirdHandler);
            listener2.setPollInterval(30);
            listener2.setQueue(queue);
            listener2.setThreadPoolSize(10);
            listener2.setService(sqsService);
            listener2.afterPropertiesSet();              

        	
        	String s = "Hello, world. This is a message";
        	SqsMessage message = sqsService.sendMessage(queue, s);
        	assertNotNull("Message is null", message);
        	log.info("Sent one message " + message.getBody());
        	
        	//Send another message
        	s = "Hello, world. This is another message";
        	message = sqsService.sendMessage(queue, s);
        	assertNotNull("Message is null", message);
        	log.info("Sent another message " + message.getBody());
        	   	
        	//Wait till the message has been consumed by all listeners
            Thread.sleep(290000);
            
            int totalUniqueMessagesReceivedByFirstHandler = firstHandler.getReceivedMessagesMap().size();
            int totalUniqueMessagesReceivedBySecondHandler = secondHandler.getReceivedMessagesMap().size();
            int totalUniqueMessagesReceivedByThirdHandler = thirdHandler.getReceivedMessagesMap().size();
            assertEquals(6, totalUniqueMessagesReceivedByFirstHandler + totalUniqueMessagesReceivedBySecondHandler + totalUniqueMessagesReceivedByThirdHandler);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        } finally {
            try {
                listener.destroy();
                listener1.destroy();
                listener2.destroy();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                fail(e.getMessage());
            }
        }
    }
    
    @Test
    public void testSendingMessage(){
    	String s = "Hello, this is a test message";
    	SqsMessage message = sqsService.sendMessage(queue, s);
    	assertNotNull("Message is null", message);
    	log.info("Sent one message " + message.getBody());
    	   	
    }
    
    public static class MessageHandler implements com.polarrose.amazon.spring.sqs.MessageHandler {
    	private Map<String, SqsMessage> receivedMessagesMap = new HashMap<String, SqsMessage>();
    	private String name;
    	public MessageHandler(String name){
    		this.name = name;
    	}
    	
        public void handle(SqsMessage message) {
            log.info("Received a message: body = " + message.getBody());
            assertNotNull("Message is null", message);
            
            receivedMessagesMap.put(message.getId(), message);
        }

		public Map<String, SqsMessage> getReceivedMessagesMap() {
			return receivedMessagesMap;
		}
    }
    public static class DelayedMessageHandler implements com.polarrose.amazon.spring.sqs.MessageHandler {
 
    	
        public void handle(SqsMessage message) {
            log.info("Received a message: body = " + message.getBody());
            assertNotNull("Message is null", message);
            assertEquals("Message body is incorrect", TEST, message.getBody());
            
            fail("Force it!");
            simulateMessageProcessingTime();
        }

        private void simulateMessageProcessingTime() {
            Random random = new Random();

            try {
                long sleepTime = (random.nextInt(10) + 1) * 250L;
                log.info("Sleeping for: " + sleepTime);
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
                fail(e.getMessage());
            }

        }
    }

}
