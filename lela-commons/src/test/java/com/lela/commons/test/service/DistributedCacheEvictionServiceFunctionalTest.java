/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.test.service;

import com.lela.commons.test.AbstractFunctionalTest;
import com.lela.commons.service.DistributedCacheEvictionService;
import com.polarrose.amazon.spring.sqs.impl.SimpleQueueListener;
import com.polarrose.amazon.sqs.SqsMessage;
import com.polarrose.amazon.sqs.SqsQueue;
import com.polarrose.amazon.sqs.SqsService;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Bjorn Harvold
 * Date: 11/23/11
 * Time: 10:40 PM
 * Responsibility:
 */
public class DistributedCacheEvictionServiceFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(DistributedCacheEvictionServiceFunctionalTest.class);

    private static final int QUEUE_TIMEOUT = 8000;

    private static final String ITEM_KEY = "itemKey";
    private static final String ITEM_MESSAGE_BODY = "{\"list\":[{\"key\":\"itemKey\",\"type\":\"ITEM\"}]}";

    private static final String STORE_KEY = "storeKey";
    private static final String STORE_MESSAGE_BODY = "{\"list\":[{\"key\":\"itemKey\",\"type\":\"ITEM\"}]}";

    @Autowired
    private SqsService sqsService;

    @Autowired
    private DistributedCacheEvictionService distributedCacheEvictionService;

    private SqsQueue queue = null;

    private SimpleQueueListener listener = null;
    private MessageHandler handler = null;

    @Before
    public void setUp() {
        try {
            log.warn("Running test setup");

            queue = new SqsQueue(new URL("http://queue.amazonaws.com/lela-test-cache-queue"));

//            handler = new MessageHandler();
//
//            listener = new SimpleQueueListener();
//            listener.setMessageHandler(handler);
//            listener.setPollInterval(1);
//            listener.setQueue(queue);
//            listener.setThreadPoolSize(10);
//            listener.setService(sqsService);
//
//            listener.afterPropertiesSet();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }
//
//    @After
//    public void tearDown() {
//        try {
//            log.warn("Running test tear down");
//            listener.destroy();
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            fail(e.getMessage());
//        }
//    }

    @Test
    public void testEvict() {
//        Random random = new Random();
//        try {
//
//            List<CacheEviction> list = new ArrayList<CacheEviction>();
//            CacheEvictions evictions = new CacheEvictions();
//            evictions.setList(list);
//
//            CacheEviction eviction = new CacheEviction(CacheType.ITEM, ITEM_KEY);
//            list.add(eviction);
//
//            // Push the cache evict request
//            distributedCacheEvictionService.evict(evictions);
//
//            // Ensure that the queue has the message
//            Thread.sleep(2000);
//            SqsMessage message = sqsService.receiveMessage(queue);
//            assertNotNull("Message is null", message);
//            assertEquals("Message body is incorrect", ITEM_MESSAGE_BODY, message.getBody());
//
////            synchronized (handler) {
////                log.debug("Waiting for timeout of: " + QUEUE_TIMEOUT);
////                handler.wait(QUEUE_TIMEOUT);
////                log.debug("Waking from wait()");
////            }
////
////            assertTrue("Nothing received from queue", handler.invoked);
////            assertNotNull("Message is null", handler.message);
////            assertEquals("Message body is incorrect", ITEM_MESSAGE_BODY, handler.message.getBody());
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            fail(e.getMessage());
//        }
//        finally {
//            //handler.reset();
//        }
    }

//    @Test
//    public void testStoreEvict() {
//        Random random = new Random();
//        try {
//
//            List<String> list = new ArrayList<String>();
//            list.add(STORE_KEY);
//
//            distributedCacheEvictionService.evictStores(list);
//
//            synchronized (handler) {
//                log.debug("Waiting for timeout of: " + QUEUE_TIMEOUT);
//                handler.wait(QUEUE_TIMEOUT);
//                log.debug("Waking from wait()");
//            }
//
//            assertTrue("Nothing received from queue", handler.invoked);
//            assertNotNull("Message is null", handler.message);
//            assertEquals("Message body is incorrect", ITEM_MESSAGE_BODY, handler.message.getBody());
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            fail(e.getMessage());
//        }
//        finally {
//            handler.reset();
//        }
//    }

    public static class MessageHandler implements com.polarrose.amazon.spring.sqs.MessageHandler {

        public boolean invoked = false;
        public SqsMessage message = null;

        public void handle(SqsMessage message) {
            log.info("Received a message: body = " + message.getBody());
            this.message = message;
            this.invoked = true;

            // Wake up the test
            synchronized (this) {
                notifyAll();
            }
        }

        public void reset() {
            invoked = false;
            message = null;
        }
    }


}
