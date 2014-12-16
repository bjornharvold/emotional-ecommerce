/*
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

package com.polarrose.amazon.spring.sqs.impl;

import com.polarrose.amazon.spring.sqs.MessageHandler;
import com.polarrose.amazon.spring.sqs.QueueListener;
import com.polarrose.amazon.sqs.SqsMessage;
import com.polarrose.amazon.sqs.SqsQueue;
import com.polarrose.amazon.sqs.SqsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.concurrent.*;

/**
 * Abstract SqsQueueListener.
 */

public abstract class AbstractQueueListener implements QueueListener, InitializingBean, DisposableBean
{
    protected final Log logger = LogFactory.getLog(getClass());

    private BlockingQueue<Runnable> messageQueue;

    protected BlockingQueue<Runnable> getMessageQueue()
    {
        return messageQueue;
    }

    private ExecutorService executorService;

    protected ExecutorService getExecutorService()
    {
        return executorService;
    }

    private QueuePollerTask pollerTask;
    private ScheduledExecutorService pollerExecutor;

    private SqsService service;

    private Boolean deleteMessageOnRetrieval = false;

    public Boolean getDeleteMessageOnRetrieval() {
        return deleteMessageOnRetrieval;
    }

    public void setDeleteMessageOnRetrieval(Boolean deleteMessageOnRetrieval) {
        this.deleteMessageOnRetrieval = deleteMessageOnRetrieval;
    }

    @Required
    public SqsService getService()
    {
        return service;
    }

    public void setService(SqsService service)
    {
        this.service = service;
    }

    private SqsQueue queue;

    @Required
    public SqsQueue getQueue()
    {
        return queue;
    }

    public void setQueue(SqsQueue queue)
    {
        this.queue = queue;
    }

    private MessageHandler messageHandler;

    @Required
    public MessageHandler getMessageHandler()
    {
        return messageHandler;
    }

    public void setMessageHandler(MessageHandler messageHandler)
    {
        this.messageHandler = messageHandler;
    }

    private int pollInterval = DEFAULT_POLL_INTERVAL;

    public int getPollInterval()
    {
        return pollInterval;
    }

    public void setPollInterval(int pollInterval)
    {
        this.pollInterval = pollInterval;
    }

    private int threadPoolSize = DEFAULT_THREAD_POOL_SIZE;

    public int getThreadPoolSize()
    {
        return threadPoolSize;
    }

    public void setThreadPoolSize(int threadPoolSize)
    {
        this.threadPoolSize = threadPoolSize;
    }

    abstract int calculatePollInterval();

    abstract int calculateFetchSize();

    public void afterPropertiesSet() throws Exception
    {
        if (service == null) {
            throw new IllegalArgumentException("Property 'service' is required");
        }

        if (queue == null) {
            throw new IllegalArgumentException("Property 'queue' is required");
        }

        if (messageHandler == null) {
            throw new IllegalArgumentException("Property 'messageHandler' is required");
        }

        messageQueue = new LinkedBlockingQueue<Runnable>(getThreadPoolSize() * 2);

        executorService = new ThreadPoolExecutor(
            getThreadPoolSize(),
            getThreadPoolSize(),
            0, // TODO What is a better keepalive value? Is this even used when the pool sizes are the same? Guess not.
            TimeUnit.SECONDS,
            messageQueue
        );

        // Start the poller. We execute it once here.

        pollerTask = new QueuePollerTask();
        pollerExecutor = Executors.newSingleThreadScheduledExecutor();
        pollerExecutor.execute(pollerTask);
    }

    public void destroy() throws Exception
    {
        executorService.shutdown();
        pollerExecutor.shutdown();

        // TODO Do we have to wait for the shutdown to complete?
    }

    public class MessageTask implements Runnable
    {
        private final SqsMessage message;

        MessageTask(SqsMessage message)
        {
            this.message = message;
        }

        public void run()
        {
            try {
                messageHandler.handle(message);
            } catch (Throwable t) {
                // TODO Ignore exceptions thrown by the message handler?
                logger.info("MessageHandler threw an exception", t);
            } finally {
                try {
                    if (deleteMessageOnRetrieval) {
                        service.deleteMessage(queue, message);
                    }
                } catch (Throwable t) {
                    // TODO Ignore this? What is a good strategy?
                    logger.info("Can not delete message", t);
                }
            }
        }
    }

    class QueuePollerTask implements Runnable
    {
        public void run()
        {
            try {
                logger.info("SQS Queue poll running");

                int numberOfMessages = calculateFetchSize();
                if (numberOfMessages != 0)
                {
                    List<SqsMessage> messages = service.receiveMessages(queue, numberOfMessages);

                    if (logger.isDebugEnabled()) {
                        logger.debug("Message queue contains " + messageQueue.size() + ", so we polled for " + numberOfMessages + " and received " + messages.size() + " messages");
                    }

                    if (messages != null) {
                        for (SqsMessage message : messages) {
                            executorService.execute(new MessageTask(message));
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("SQS Queue poll failed", e);
            } finally {
                pollerExecutor.schedule(pollerTask, calculatePollInterval(), TimeUnit.SECONDS);
            }
        }
    }
}